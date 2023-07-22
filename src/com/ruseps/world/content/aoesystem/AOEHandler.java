package com.ruseps.world.content.aoesystem;

import java.util.Iterator;

import com.ruseps.GameSettings;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Locations;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class AOEHandler {

	public static void handleAttack(Character attacker, Character victim, int minimumDamage, int maximumDamage,
			int radius, CombatIcon combatIcon) {

		Iterator<? extends Character> it = null;
		if (attacker.isPlayer() && victim.isPlayer()) {
			it = ((Player) attacker).getLocalPlayers().iterator();
		} else if (attacker.isPlayer() && victim.isNpc()) {
			it = ((Player) attacker).getLocalNpcs().iterator();

			for (Iterator<? extends Character> $it = it; $it.hasNext();) {
				Character next = $it.next();

				if (next == null) {
					continue;
				}

				if (next.isNpc()) {
					NPC n = (NPC) next;
					if (!n.getDefinition().isAttackable() || n.isSummoningNpc()) {
					    ((NPC) next).getDefinition().setAggressive(true);
						continue;
					}
				} else {
					Player p = (Player) next;
					if (p.getLocation() != Locations.Location.WILDERNESS || !Locations.Location.inMulti(p)) {
						continue;
					}
				}

				if (next.getPosition().isWithinDistance(victim.getPosition(), radius) && !next.equals(attacker)
						&& !next.equals(victim) && next.getConstitution() > 0) {
					int calc = RandomUtility.inclusiveRandom(minimumDamage, maximumDamage);
					next.dealDamage(new Hit(calc, Hitmask.RED, combatIcon));
					next.getCombatBuilder().attack(attacker);
					next.getCombatBuilder().addDamage(attacker, calc);
		
				}
			}
		}

	}
}
