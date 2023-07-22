package com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_three;
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

public class ArmourTitan implements CombatStrategy{
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
		NPC ArmourTitan = (NPC)entity;
		if(ArmourTitan.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(ArmourTitan.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			ArmourTitan.performAnimation(new Animation(ArmourTitan.getDefinition().getAttackAnimation()));
			ArmourTitan.getCombatBuilder().setContainer(new CombatContainer(ArmourTitan, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(ArmourTitan.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			ArmourTitan.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(76), pos);
			ArmourTitan.performAnimation(new Animation(11010));
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					ArmourTitan.performAnimation(new Animation(ArmourTitan.getDefinition().getAttackAnimation()));
					ArmourTitan.getCombatBuilder().setContainer(new CombatContainer(ArmourTitan, victim, 1, 1, CombatType.MELEE, false));
					ArmourTitan.setChargingAttack(false);
					ArmourTitan.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			ArmourTitan.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			ArmourTitan.performAnimation(new Animation(barrage ? 1369: 1369));
			ArmourTitan.getCombatBuilder().setContainer(new CombatContainer(ArmourTitan, victim, 1, 3, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, ArmourTitan, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(ArmourTitan, victim, 103, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && Misc.getRandom(6) <= 3) {
							ArmourTitan.performAnimation(new Animation(1369));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(ArmourTitan.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(ArmourTitan.getCombatBuilder(), new CombatContainer(ArmourTitan, toAttack, 2, CombatType.MELEE, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1687));
								}
							}
						}
						ArmourTitan.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(ArmourTitan) - 2);
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
