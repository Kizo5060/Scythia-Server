package com.ruseps.world.content.combat.strategy.multi_formula;

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

public class B00NY implements CombatStrategy {
	
	boolean transformed = false;
	
	private void transform(NPC npc, int id) {
		npc.setTransformationId(id);
		npc.forceChat("B00NY POWER!");
		npc.getUpdateFlag().flag(Flag.TRANSFORM);
	}
	
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
		NPC b00ny = (NPC)entity;
		
		if(b00ny.getConstitution() > b00ny.getConstitution() / 5) {
			transformed = false;
		}
		if (b00ny.getConstitution() < b00ny.getDefaultConstitution() / 4 && !transformed) {
			transform(b00ny, 4388);
			transformed = true;
		}
		if (b00ny.getConstitution() < b00ny.getDefaultConstitution() / 3 && !transformed) {
			transform(b00ny, 4389);
			transformed = true;
		}
		if(b00ny.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(b00ny.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			b00ny.performAnimation(new Animation(b00ny.getDefinition().getAttackAnimation()));
			b00ny.getCombatBuilder().setContainer(new CombatContainer(b00ny, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(b00ny.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			b00ny.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1179), pos);
			b00ny.performAnimation(new Animation(423));
			b00ny.performGraphic(new Graphic(2009));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					//b00ny.moveTo(pos);
					b00ny.performAnimation(new Animation(b00ny.getDefinition().getAttackAnimation()));
					b00ny.getCombatBuilder().setContainer(new CombatContainer(b00ny, victim, 1, 1, CombatType.MELEE, false));
					b00ny.setChargingAttack(false);
					b00ny.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			b00ny.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			b00ny.performAnimation(new Animation(barrage ? 401 : 426));
			b00ny.performGraphic(new Graphic (2003));
			b00ny.forceChat("So you thought you'd enter my rabbit hole?!");
			b00ny.getCombatBuilder().setContainer(new CombatContainer(b00ny, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, b00ny, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(b00ny, victim, 1355, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(5);
							victim.performGraphic(new Graphic(197));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							b00ny.performAnimation(new Animation(440));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(b00ny.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(b00ny.getCombatBuilder(), new CombatContainer(b00ny, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(247));
									b00ny.forceChat("Hop on this!");
								}
							}
						}
						b00ny.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(b00ny) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 401;
		
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