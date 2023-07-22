package com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_one;

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

public class Starmie implements CombatStrategy {

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
		NPC Starmie = (NPC)entity;
		if(Starmie.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Starmie.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Starmie.performAnimation(new Animation(Starmie.getDefinition().getAttackAnimation()));
			Starmie.getCombatBuilder().setContainer(new CombatContainer(Starmie, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Starmie.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Starmie.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(502), pos);
			Starmie.performAnimation(new Animation(440));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Starmie.performAnimation(new Animation(Starmie.getDefinition().getAttackAnimation()));
					Starmie.getCombatBuilder().setContainer(new CombatContainer(Starmie, victim, 1, 1, CombatType.MELEE, false));
					Starmie.setChargingAttack(false);
					Starmie.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Starmie.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Starmie.performAnimation(new Animation(barrage ? 440 : 440));
			Starmie.getCombatBuilder().setContainer(new CombatContainer(Starmie, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Starmie, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Starmie, victim, 87, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Starmie.performAnimation(new Animation(440));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Starmie.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Starmie.getCombatBuilder(), new CombatContainer(Starmie, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(68));
								}
							}
						}
						Starmie.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Starmie) - 2);
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