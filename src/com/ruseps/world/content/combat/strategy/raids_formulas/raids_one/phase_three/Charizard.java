package com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_three;

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

public class Charizard implements CombatStrategy {

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
		NPC Charizard = (NPC)entity;
		if(Charizard.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Charizard.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Charizard.performAnimation(new Animation(Charizard.getDefinition().getAttackAnimation()));
			Charizard.getCombatBuilder().setContainer(new CombatContainer(Charizard, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Charizard.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Charizard.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1618), pos);
			Charizard.performAnimation(new Animation(401));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Charizard.performAnimation(new Animation(Charizard.getDefinition().getAttackAnimation()));
					Charizard.getCombatBuilder().setContainer(new CombatContainer(Charizard, victim, 1, 1, CombatType.MELEE, false));
					Charizard.setChargingAttack(false);
					Charizard.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Charizard.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Charizard.performAnimation(new Animation(barrage ? 10961 : 10961));
			Charizard.getCombatBuilder().setContainer(new CombatContainer(Charizard, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Charizard, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Charizard, victim, 1155, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Charizard.performAnimation(new Animation(401));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Charizard.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Charizard.getCombatBuilder(), new CombatContainer(Charizard, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1168));
								}
							}
						}
						Charizard.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Charizard) - 2);
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