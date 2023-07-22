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

public class Lugia implements CombatStrategy {

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
		NPC Lugia = (NPC)entity;
		if(Lugia.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Lugia.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Lugia.performAnimation(new Animation(Lugia.getDefinition().getAttackAnimation()));
			Lugia.getCombatBuilder().setContainer(new CombatContainer(Lugia, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Lugia.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Lugia.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			Lugia.performAnimation(new Animation(5023));
			Lugia.forceChat("Who dares to interrupt my slumber...?");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Lugia.performAnimation(new Animation(Lugia.getDefinition().getAttackAnimation()));
					Lugia.getCombatBuilder().setContainer(new CombatContainer(Lugia, victim, 1, 1, CombatType.MELEE, false));
					Lugia.setChargingAttack(false);
					Lugia.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Lugia.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Lugia.performAnimation(new Animation(barrage ? 5023 : 5023));
			Lugia.forceChat("SKREEE-AH!");
			Lugia.getCombatBuilder().setContainer(new CombatContainer(Lugia, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Lugia, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Lugia, victim, 88, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Lugia.performAnimation(new Animation(5023));
							Lugia.forceChat("YAH-OO-GA");
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Lugia.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Lugia.getCombatBuilder(), new CombatContainer(Lugia, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(287));
									Lugia.forceChat("SKREEEEEEE!!!");
								}
							}
						}
						Lugia.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Lugia) - 2);
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