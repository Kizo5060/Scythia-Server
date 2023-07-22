/*package com.ruseps.world.content.combat.strategy.impl;

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

public class LavaDragon implements CombatStrategy {

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
		NPC LavaDragon = (NPC)entity;
		if(LavaDragon.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(LavaDragon.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			LavaDragon.performAnimation(new Animation(LavaDragon.getDefinition().getAttackAnimation()));
			LavaDragon.getCombatBuilder().setContainer(new CombatContainer(LavaDragon, victim, 1, 1, CombatType.MAGIC, true));
		} else if(!Locations.goodDistance(LavaDragon.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			LavaDragon.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			LavaDragon.performAnimation(new Animation(5023));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					LavaDragon.performAnimation(new Animation(LavaDragon.getDefinition().getAttackAnimation()));
					LavaDragon.getCombatBuilder().setContainer(new CombatContainer(LavaDragon, victim, 1, 1, CombatType.MAGIC, false));
					LavaDragon.setChargingAttack(false);
					LavaDragon.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			LavaDragon.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			LavaDragon.performAnimation(new Animation(barrage ? 5023 : 5023));
			LavaDragon.getCombatBuilder().setContainer(new CombatContainer(LavaDragon, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, LavaDragon, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(LavaDragon, victim, 88, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							LavaDragon.performAnimation(new Animation(5023));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(LavaDragon.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(LavaDragon.getCombatBuilder(), new CombatContainer(LavaDragon, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(287));
								}
							}
						}
						LavaDragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(LavaDragon) - 2);
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
}*/