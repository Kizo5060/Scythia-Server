package com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_two;
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

public class WingedMonster implements CombatStrategy{
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
		NPC WingedMonster = (NPC)entity;
		if(WingedMonster.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(WingedMonster.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			WingedMonster.performAnimation(new Animation(WingedMonster.getDefinition().getAttackAnimation()));
			WingedMonster.getCombatBuilder().setContainer(new CombatContainer(WingedMonster, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(WingedMonster.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			WingedMonster.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			WingedMonster.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					WingedMonster.performAnimation(new Animation(WingedMonster.getDefinition().getAttackAnimation()));
					WingedMonster.getCombatBuilder().setContainer(new CombatContainer(WingedMonster, victim, 1, 1, CombatType.MELEE, false));
					WingedMonster.setChargingAttack(false);
					WingedMonster.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			WingedMonster.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			WingedMonster.performAnimation(new Animation(barrage ? 1369: 1369));
			WingedMonster.getCombatBuilder().setContainer(new CombatContainer(WingedMonster, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, WingedMonster, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(WingedMonster, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							WingedMonster.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(WingedMonster.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(WingedMonster.getCombatBuilder(), new CombatContainer(WingedMonster, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						WingedMonster.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(WingedMonster) - 2);
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
