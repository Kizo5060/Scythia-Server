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

public class Seadra implements CombatStrategy {

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
		NPC Seadra = (NPC)entity;
		if(Seadra.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Seadra.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Seadra.performAnimation(new Animation(Seadra.getDefinition().getAttackAnimation()));
			Seadra.getCombatBuilder().setContainer(new CombatContainer(Seadra, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Seadra.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Seadra.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(68), pos);
			Seadra.performAnimation(new Animation(440));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Seadra.performAnimation(new Animation(Seadra.getDefinition().getAttackAnimation()));
					Seadra.getCombatBuilder().setContainer(new CombatContainer(Seadra, victim, 1, 1, CombatType.MELEE, false));
					Seadra.setChargingAttack(false);
					Seadra.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Seadra.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Seadra.performAnimation(new Animation(barrage ? 440 : 433));
			Seadra.getCombatBuilder().setContainer(new CombatContainer(Seadra, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Seadra, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Seadra, victim, 87, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Seadra.performAnimation(new Animation(433));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Seadra.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Seadra.getCombatBuilder(), new CombatContainer(Seadra, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(502));
								}
							}
						}
						Seadra.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Seadra) - 2);
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