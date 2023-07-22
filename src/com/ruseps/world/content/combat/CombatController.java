package com.ruseps.world.content.combat;

import com.ruseps.world.content.combat.restrictions.KillCTRestrictor;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class CombatController {

    public static boolean canAttack(Character attacker, Character target) {
        if(attacker instanceof Player) {
            if(target instanceof NPC) {
                if(!canAttack((Player) attacker, (NPC) target))
                    return false;
            }
        }
        return true;
    }

    //Note that because of the checks above this can be considered as a PVM strict method.
    private static boolean canAttack(Player player, NPC npc) {
        return KillCTRestrictor.meetsRequirement(player, npc.getId());
    }

}
