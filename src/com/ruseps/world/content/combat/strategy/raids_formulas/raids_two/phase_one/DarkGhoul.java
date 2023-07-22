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

public class DarkGhoul implements CombatStrategy{
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
		NPC DarkGhoul = (NPC)entity;
		if(DarkGhoul.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(DarkGhoul.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			DarkGhoul.performAnimation(new Animation(DarkGhoul.getDefinition().getAttackAnimation()));
			DarkGhoul.getCombatBuilder().setContainer(new CombatContainer(DarkGhoul, victim, 1, 1, CombatType.MAGIC, true));
		} else if(!Locations.goodDistance(DarkGhoul.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			DarkGhoul.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			DarkGhoul.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					DarkGhoul.performAnimation(new Animation(DarkGhoul.getDefinition().getAttackAnimation()));
					DarkGhoul.getCombatBuilder().setContainer(new CombatContainer(DarkGhoul, victim, 1, 1, CombatType.MAGIC, false));
					DarkGhoul.setChargingAttack(false);
					DarkGhoul.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			DarkGhoul.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			DarkGhoul.performAnimation(new Animation(barrage ? 1369: 1369));
			DarkGhoul.getCombatBuilder().setContainer(new CombatContainer(DarkGhoul, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, DarkGhoul, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(DarkGhoul, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							DarkGhoul.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(DarkGhoul.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(DarkGhoul.getCombatBuilder(), new CombatContainer(DarkGhoul, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						DarkGhoul.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(DarkGhoul) - 2);
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

