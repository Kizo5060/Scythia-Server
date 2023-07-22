package com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_one;
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

public class HunterxHunter implements CombatStrategy{
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
		NPC HunterxHunter = (NPC)entity;
		if(HunterxHunter.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(HunterxHunter.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			HunterxHunter.performAnimation(new Animation(HunterxHunter.getDefinition().getAttackAnimation()));
			HunterxHunter.getCombatBuilder().setContainer(new CombatContainer(HunterxHunter, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(HunterxHunter.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			HunterxHunter.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			HunterxHunter.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					HunterxHunter.performAnimation(new Animation(HunterxHunter.getDefinition().getAttackAnimation()));
					HunterxHunter.getCombatBuilder().setContainer(new CombatContainer(HunterxHunter, victim, 1, 1, CombatType.MELEE, false));
					HunterxHunter.setChargingAttack(false);
					HunterxHunter.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			HunterxHunter.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			HunterxHunter.performAnimation(new Animation(barrage ? 1369: 1369));
			HunterxHunter.getCombatBuilder().setContainer(new CombatContainer(HunterxHunter, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, HunterxHunter, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(HunterxHunter, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							HunterxHunter.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(HunterxHunter.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(HunterxHunter.getCombatBuilder(), new CombatContainer(HunterxHunter, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						HunterxHunter.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(HunterxHunter) - 2);
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
