package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue.CombatHit;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class Cobra implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		NPC cobra = (NPC)entity;
		int att = Misc.getRandom(3);
		if(att == 1) {
			cobra.prepareSpell(CombatSpells.BLOOD_BARRAGE.getSpell(), victim);
		}
		if(att == 2) {
			cobra.prepareSpell(CombatSpells.ICE_BARRAGE.getSpell(), victim);
		}
		if(att == 3) {
			cobra.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
		}
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}
	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
