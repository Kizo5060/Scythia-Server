package com.ruseps.world.content.combat.restrictions;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.entity.impl.player.Player;

public class KillCTRestrictor {

    public static boolean meetsRequirement(final Player player, final int npcId) {
        final AttackRequirement requirement = AttackRequirement.byId(npcId);
        // If there is no kill requirement, no further consideration is required and you may attack the NPC.
        if (requirement == null) {
            return true;
        }
        // If there is a requirement but your slayer task has the same NPCID, you are still allowed to attack.
        if (player.getSlayer().getSlayerTask().getNpcId() == npcId) {
            return true;
        }

        if (player.getRights() == PlayerRights.YOUTUBER) {
            return true;
        }

        // Check if the double kill pet is summoned
        boolean hasDoubleKillPetSummoned = false;
        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                && player.getSummoning().getFamiliar().getSummonNpc().getId() == 669) {
            hasDoubleKillPetSummoned = true;
        }

        int requiredTotalCT = requirement.getRequiredTotalCT();
        if (hasDoubleKillPetSummoned) {
            requiredTotalCT /= 2;
        }

        // Absolute totals across all NPCs. We want to consider this first to avoid unnecessarily iterating over multiple arrays.
        if (KillsTracker.getTotalKills(player) < requiredTotalCT) {
            player.getPacketSender().sendMessage("@blu@You need " + requiredTotalCT + " total NPC kills to attack this monster.");
            return false;
        }

        final int[] requiredNpcs = requirement.getReqKillId();
        boolean canAttack = true;

        for (int i = 0; i < requiredNpcs.length; i++) {
            final KillsTracker.KillsEntry entry = KillsTracker.entryForID(player, requiredNpcs[i], false);
            int requiredKillCT = requirement.getReqKillCT(i);
            int requiredRunningCT = requirement.getRequiredRunningCT(i);

            // If the player has the double kill pet summoned, halve the required kills
            if (hasDoubleKillPetSummoned) {
                requiredKillCT /= 2;
                requiredRunningCT /= 2;
            }

            // Resettable total for a single monster.
            if (entry.getAmount() < requiredKillCT) {
                player.getPacketSender().sendMessage("@blu@You need " + requiredKillCT + " " + NpcDefinition.forId(requirement.getReqKillId()[i]).getName() + " kills to attack this monster.");
                player.getPacketSender().sendMessage("@blu@You currently have " + entry.getAmount() + " " + NpcDefinition.forId(requirement.getReqKillId()[i]).getName() + " kills.");
                canAttack = false;
            }
            // Running total for a single monster.
            if (entry.getRunningTotal() < requiredRunningCT) {
                player.getPacketSender().sendMessage("@blu@You need a total of " + requiredRunningCT + " " + NpcDefinition.forId(requirement.getReqKillId()[i]).getName() + " kills to attack this monster.");
                player.getPacketSender().sendMessage("@blu@You currently have a total of " + entry.getRunningTotal() + " " + NpcDefinition.forId(requirement.getReqKillId()[i]).getName() + " kills.");
                canAttack = false;
            }
        }

        // If all the checks are passed then
        return player.getRights() == PlayerRights.DEVELOPER || canAttack;
    }
}