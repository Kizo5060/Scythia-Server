package com.ruseps.world.content.combat.strategy.vote_boss;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class RaichuFormula implements CombatStrategy {

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
		NPC pikarai = (NPC) entity;
		if (pikarai.isChargingAttack() || pikarai.getConstitution() <= 0) {
			pikarai.getCombatBuilder().setAttackTimer(2);
			return true;
		}
		if (Locations.goodDistance(pikarai.getPosition().copy(), victim.getPosition().copy(), 1)
				&& Misc.getRandom(5) <= 3) {
			pikarai.performAnimation(new Animation(pikarai.getDefinition().getAttackAnimation()));
			pikarai.getCombatBuilder().setContainer(new CombatContainer(pikarai, victim, 1, 1, CombatType.MAGIC, true));
		} else {
			pikarai.setChargingAttack(true);
			pikarai.performAnimation(new Animation(getAnimation(pikarai.getId())));
			pikarai.getCombatBuilder()
					.setContainer(new CombatContainer(pikarai, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, pikarai, false) {
				int tick = 0;

				@Override
				public void execute() {

					if (tick == 2) {
						victim.performGraphic(new Graphic(1194));
						pikarai.performGraphic(new Graphic(2009));
					} else if (tick == 3) {
						victim.performGraphic(new Graphic(1854));
						pikarai.performGraphic(new Graphic(2009));
						pikarai.setChargingAttack(false).getCombatBuilder().setAttackTimer(4);
						stop();
					}
					tick++;
				}
			});

		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 1978;
		
		return anim;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 1;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}