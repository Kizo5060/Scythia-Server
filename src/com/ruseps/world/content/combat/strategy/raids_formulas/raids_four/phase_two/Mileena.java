package com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_two;
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

public class Mileena implements CombatStrategy{
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
		NPC Mileena = (NPC)entity;
		if(Mileena.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(Mileena.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			Mileena.performAnimation(new Animation(Mileena.getDefinition().getAttackAnimation()));
			Mileena.getCombatBuilder().setContainer(new CombatContainer(Mileena, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(Mileena.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			Mileena.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			Mileena.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					Mileena.performAnimation(new Animation(Mileena.getDefinition().getAttackAnimation()));
					Mileena.getCombatBuilder().setContainer(new CombatContainer(Mileena, victim, 1, 1, CombatType.MELEE, false));
					Mileena.setChargingAttack(false);
					Mileena.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			Mileena.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			Mileena.performAnimation(new Animation(barrage ? 1369: 1369));
			Mileena.getCombatBuilder().setContainer(new CombatContainer(Mileena, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, Mileena, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(Mileena, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							Mileena.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(Mileena.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(Mileena.getCombatBuilder(), new CombatContainer(Mileena, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						Mileena.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Mileena) - 2);
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
