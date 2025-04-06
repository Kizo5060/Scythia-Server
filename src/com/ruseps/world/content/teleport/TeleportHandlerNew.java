package com.ruseps.world.content.teleport;

import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.Player;

public class TeleportHandlerNew {


    public static void openInterface(Player player, int tier) {
        resetInterface(player);
        buildTeleports(tier, player);

        if (player.teleports.size() == 0) {
            player.getPacketSender().sendMessage("This tab doesnt have any teleports yet!");
        } else {
            buildInterface(0, player);
        }
        player.getPacketSender().sendInterface(45800);
    }

    public static void resetInterface(Player player) {
        for (int j = 0; j < 60; j++) {
            player.getPacketSender().sendString(45901 + j, "");
        }

        int childId = 45701;

        for (int i = 0; i < 50; i++) {
            player.getPacketSender().sendItemOnInterface(childId, 0, 0);
            childId++;
        }
    }

    public static void buildTeleports(int tier, Player player) {
        player.teleports = TeleportEnum.dataByTier(tier);
        player.tier = tier;

        for (int j = 0; j < player.teleports.size(); j++) {
            player.getPacketSender().sendString(45901 + j, "" + player.teleports.get(j).getTeleportName());
        }
    }

    /** sends drops to interface **/
    public static void buildInterface(int index, Player player) {
        player.teleportIndex = index;
        TeleportEnum teleport = player.teleports.get(index);
        NpcDefinition npcdef = NpcDefinition.forId(teleport.getNpcId());
        player.getPacketSender().sendNpcHeadOnInterface(teleport.getNpcId(), 0);

        player.getPacketSender().sendString(45803, "" + getTitle(player.tier));

        player.getPacketSender().sendString(45807, "Name: " + npcdef.getName());
        player.getPacketSender().sendString(45808, "Combat level: " + calculateCombat(npcdef.getCombatLevel(), player) + " " + npcdef.getCombatLevel());
        player.getPacketSender().sendString(45809, "Hp: " + npcdef.getHitpoints());
        player.getPacketSender().sendString(45810, "Max Hit: " + npcdef.getMaxHit());
        player.getPacketSender().sendString(45843, "Difficulty: " + teleport.getDifficulty());

        int containerId = 45701;

        for (int i = 0; i < 100; i++) {
            player.getPacketSender().sendItemOnInterface(containerId, 0, 0);
            containerId++;
        }

        System.out.println("Sending drops for " + teleport.getNpcId());

        int childId = 45701;
        if (NPCDrops.forId(teleport.getNpcId()) != null) {
            if (NPCDrops.forId(teleport.getNpcId()).getDropList().length > 0) {
                for (int i = 0; i < NPCDrops.forId(teleport.getNpcId()).getDropList().length; i++) {
                    player.getPacketSender().sendItemOnInterface(childId, NPCDrops.forId(teleport.getNpcId()).getDropList()[i].getId(), NPCDrops.forId(teleport.getNpcId()).getDropList()[i].getItem().getAmount());
                    childId++;
                }
            }

        }
    }


    public static String getTitle(int tier) {
        switch (tier) {
            case 1:
                return "Monster list";
            case 2:
                return "Boss list";
            case 3:
                return "Minigame list";
            case 4:
                return "Multi Boss list";
            case 5:
                return "City list";
            case 6:
                return "Other list";
            default:
                return "Error";
        }
    }

    public static String calculateCombat(int npcCombat, Player player) {
        if (npcCombat < 75) {
            return "@gre@";
        } else if (npcCombat < 100) {
            return "@yel@";
        } else if (npcCombat < 250) {
            return "@red@";
        } else {
            return "@mag@";
        }
    }
}
