package com.ruseps.world.content.combat.strategy.world_boss;

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

public class Inherited implements CombatStrategy {

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
		NPC Inherited = (NPC)entity;
		if(Inherited.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Inherited.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Inherited.performAnimation(new Animation(Inherited.getDefinition().getAttackAnimation()));
			Inherited.getCombatBuilder().setContainer(new CombatContainer(Inherited, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Inherited.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Inherited.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1176), pos);
			Inherited.performAnimation(new Animation(11589));
			Inherited.performGraphic(new Graphic(2009));
			Inherited.forceChat("You think you can hurt me!!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Inherited.moveTo(pos);
					Inherited.performAnimation(new Animation(Inherited.getDefinition().getAttackAnimation()));
					Inherited.getCombatBuilder().setContainer(new CombatContainer(Inherited, victim, 1, 1, CombatType.MELEE, false));
					Inherited.setChargingAttack(false);
					Inherited.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Inherited.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Inherited.performAnimation(new Animation(barrage ? 9012 : 9012));
			Inherited.performGraphic(new Graphic (1625));
			Inherited.forceChat("I only Serve The Witch Queen shes a Beast!");
			Inherited.getCombatBuilder().setContainer(new CombatContainer(Inherited, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, Inherited, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Inherited, victim, 2001, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(10);
							victim.forceChat("She's Got me by the balls!!!!");
							victim.performGraphic(new Graphic(2051));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							Inherited.performAnimation(new Animation(11589));
							Inherited.forceChat("Witch Save Me!!!");
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Inherited.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Inherited.getCombatBuilder(), new CombatContainer(Inherited, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1390));
									Inherited.forceChat("Have you seen The Witch I can't Seem To Find Her!!");
								}
							}
						}
						Inherited.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Inherited) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 9012;
		
		return anim;
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
		return CombatType.MIXED;
	}
}