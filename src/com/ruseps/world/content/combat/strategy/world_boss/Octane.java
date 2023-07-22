package com.ruseps.world.content.combat.strategy.world_boss;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
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

public class Octane implements CombatStrategy {
	
	

	
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
		NPC Octane = (NPC)entity;
		
		if(Octane.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Octane.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Octane.performAnimation(new Animation(Octane.getDefinition().getAttackAnimation()));
			Octane.getCombatBuilder().setContainer(new CombatContainer(Octane, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Octane.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Octane.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(2122), pos);
			Octane.performAnimation(new Animation(4230));
			Octane.performGraphic(new Graphic(2122));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					//Octane.moveTo(pos);
					Octane.performAnimation(new Animation(Octane.getDefinition().getAttackAnimation()));
					Octane.getCombatBuilder().setContainer(new CombatContainer(Octane, victim, 1, 1, CombatType.MELEE, false));
					Octane.setChargingAttack(false);
					Octane.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Octane.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Octane.performAnimation(new Animation(barrage ? 4230 : 4230));
			Octane.performGraphic(new Graphic (2009));
			Octane.forceChat("Ahahahahaha");
			Octane.getCombatBuilder().setContainer(new CombatContainer(Octane, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, Octane, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Octane, victim, 195, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(10);
							victim.forceChat("Shit man that really hurt!");
							victim.performGraphic(new Graphic(1177));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							Octane.performAnimation(new Animation(4230));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Octane.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Octane.getCombatBuilder(), new CombatContainer(Octane, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1308));
									Octane.forceChat("Goodnight nubs!");
								}
							}
						}
						Octane.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Octane) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 4230;
		
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
		return CombatType.MELEE;
	}
}