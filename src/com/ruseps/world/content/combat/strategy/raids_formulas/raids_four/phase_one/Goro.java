package com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_one;
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

public class Goro implements CombatStrategy{
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
		NPC Goro = (NPC)entity;
		if(Goro.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Goro.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Goro.performAnimation(new Animation(Goro.getDefinition().getAttackAnimation()));
			Goro.getCombatBuilder().setContainer(new CombatContainer(Goro, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Goro.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Goro.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			Goro.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Goro.performAnimation(new Animation(Goro.getDefinition().getAttackAnimation()));
					Goro.getCombatBuilder().setContainer(new CombatContainer(Goro, victim, 1, 1, CombatType.MELEE, false));
					Goro.setChargingAttack(false);
					Goro.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Goro.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Goro.performAnimation(new Animation(barrage ? 1369: 1369));
			Goro.getCombatBuilder().setContainer(new CombatContainer(Goro, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, Goro, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Goro, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Goro.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Goro.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Goro.getCombatBuilder(), new CombatContainer(Goro, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						Goro.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Goro) - 2);
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
		return CombatType.MELEE;
	}
}
