package com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_two;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Lucario implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC Lucario = (NPC)entity;
		if(Lucario.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Lucario.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Lucario.performAnimation(new Animation(Lucario.getDefinition().getAttackAnimation()));
			Lucario.getCombatBuilder().setContainer(new CombatContainer(Lucario, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Lucario.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Lucario.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1897), pos);
			Lucario.performAnimation(new Animation(401));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Lucario.performAnimation(new Animation(Lucario.getDefinition().getAttackAnimation()));
					Lucario.getCombatBuilder().setContainer(new CombatContainer(Lucario, victim, 1, 1, CombatType.MELEE, false));
					Lucario.setChargingAttack(false);
					Lucario.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Lucario.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Lucario.performAnimation(new Animation(barrage ? 11989 : 14600));
			Lucario.getCombatBuilder().setContainer(new CombatContainer(Lucario, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Lucario, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Lucario, victim, 195, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Lucario.performAnimation(new Animation(401));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Lucario.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Lucario.getCombatBuilder(), new CombatContainer(Lucario, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(197));
								}
							}
						}
						Lucario.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Lucario) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}


	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 5;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}