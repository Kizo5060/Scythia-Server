package com.ruseps.world.content.combat.strategy.custom_bosses;

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

public class RubyRedDragon implements CombatStrategy {
	
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
		NPC vader = (NPC)entity;
		
		if(vader.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(vader.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			vader.performAnimation(new Animation(vader.getDefinition().getAttackAnimation()));
			vader.getCombatBuilder().setContainer(new CombatContainer(vader, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(vader.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			vader.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1176), pos);
			vader.performAnimation(new Animation(5023));
			vader.performGraphic(new Graphic(2009));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					vader.performAnimation(new Animation(vader.getDefinition().getAttackAnimation()));
					vader.getCombatBuilder().setContainer(new CombatContainer(vader, victim, 1, 1, CombatType.MELEE, false));
					vader.setChargingAttack(false);
					vader.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			vader.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			vader.performAnimation(new Animation(barrage ? 5023 : 5023));
			vader.performGraphic(new Graphic (2009));
			vader.forceChat("Silly Mortals!");
			vader.getCombatBuilder().setContainer(new CombatContainer(vader, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, vader, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(vader, victim, 198, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(5);
							victim.forceChat("Somehow I've been burned!");
							victim.performGraphic(new Graphic(1265));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							vader.performAnimation(new Animation(5023));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(vader.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(vader.getCombatBuilder(), new CombatContainer(vader, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1314));
									vader.forceChat("AAARRRGGH");
								}
							}
						}
						vader.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(vader) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 5023;
		
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