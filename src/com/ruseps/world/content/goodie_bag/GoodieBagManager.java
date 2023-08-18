package com.ruseps.world.content.goodie_bag;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.impl.BonusExperienceTask;
import com.ruseps.model.GameMode;
import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.item.Items;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.platinum_tokens.PlatinumTokenConstants;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/15/2019
 **/
@SuppressWarnings("unused")
public class GoodieBagManager {

    public static int GOODIE_BAG_ITEM = 18338;

    public static int GOODIE_BAG_INTERFACE = 61500;

    public static void open(Player player) {
        int childIndex = -4033;
        for(int i = 0; i < 25; i++) {
            player.getPA().sendInterfaceDisplayState(childIndex++, false);
            player.getPA().sendInterfaceDisplayState(childIndex++, true);
        }
        player.getPA().sendInterface(GOODIE_BAG_INTERFACE);
    }

    public static boolean AsaButton(Player player, int buttonId) {
        if(buttonId >= -4033 && buttonId <= -3985) {
            int index = 25 - ((61551 - buttonId) / 2);
            selectBox(player, index, false);
            return true;
        }
        if(buttonId == -3983) {
            player.getPA().sendInterfaceRemoval();
            return true;
        }
        return false;
    }

    public static void openAll(Player player) {
        for(Item item : player.getInventory().getItems()) {
            if(item == null)
                continue;

            if(item.getId() == GOODIE_BAG_ITEM) {
                selectBox(player, 1, true);
            }
        }
    }

    public static void selectBox(Player player, int boxSelected, boolean openAll) {
        if (!player.getInventory().contains(GOODIE_BAG_ITEM)) {
            return;
        }

        int minutes = player.getGameMode() == GameMode.NORMAL ? 20 : 10;
        BonusExperienceTask.addBonusXp(player, minutes);

        player.getPointsHandler().setDungeoneeringTokens(10_000, true);

        int totalPoints = 1;


        if(GameSettings.DOUBLE_POINTS) {
            totalPoints *= 2;
        }

        player.getPointsHandler().setVotingPoints(totalPoints, true);

        player.getInventory().delete(GOODIE_BAG_ITEM, 1);

        Item item;

        int chance = Misc.random(4000);

        boolean superRare = false;
        boolean rare = false;

        if (chance == 0) { //super rare
            item = SUPER_RARE_REWARDS[Misc.random(SUPER_RARE_REWARDS.length - 1)];
            superRare = true;
        } else if (chance <= 75) { //rare
            item = RARE_REWARDS[Misc.random(RARE_REWARDS.length - 1)];
            rare = true;
        } else if (chance <= 1000) { //uncommon
            item = UNCOMMON_REWARDS[Misc.random(UNCOMMON_REWARDS.length - 1)];
        } else { //common
            item = COMMON_REWARDS[Misc.random(COMMON_REWARDS.length - 1)];
        }

        if(rare || superRare) {
            String text = player.getUsername() + " received a " + item.getDefinition().getName() + " "
                    + (item.getAmount() > 1 ? ("x" + item.getAmount() + " ") : "") + "from a Goodiebag!";
            sendAnnouncement("<img=464><shad=CA7936>" + text);
        }

        String alert = ":shortalert:";

        if(superRare) {
            sendAnnouncement(alert + " :n:" + player.getUsername()
                    + " received a " + ItemDefinition.forId(item.getId()).getName() + " from a Goodiebag! ");
        }

        if(!openAll)
            sendRandomItems(player, boxSelected, item);

        player.getInventory().add(item);

        player.sendMessage("<img=464><shad=CA7936>You open the goodie bag and receive 1 vote point, 10k dungeoneering tokens");
        player.sendMessage("<img=464><shad=CA7936> ... and "+item.getAmount()+"x "+item.getDefinition().getName()+"!");

        int itemIdDisplay = item.getId();

        if(item.getDefinition().isNoted()) {
            itemIdDisplay = item.getDefinition().getUnnotedId();
        }

        int finalItemIdDisplay = itemIdDisplay;

        if(!openAll) {
            Dialogue REWARD_DIALOG = new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.ITEM_STATEMENT;
                }

                @Override
                public String[] item() {
                    return new String[]{
                            String.valueOf(finalItemIdDisplay),
                            "180",
                            item.getDefinition().getName()
                    };
                }

                @Override
                public String[] dialogue() {
                    return new String[]{
                            "You selected " + boxSelected + "... and pulled an item from the goodie bag!",
                    };
                }

                @Override
                public boolean closeInterface() {
                    return false;
                }
            };

            DialogueManager.start(player, REWARD_DIALOG);
        }
        PlayerPanel.refreshPanel(player);
    }

    public static void sendRandomItems(Player player, int rewardIndex, Item reward) {
        int childIndex = 61503;
        for(int i = 1; i <= 25; i++) {
            if(i == rewardIndex) {
                player.getPA().sendInterfaceDisplayState(childIndex++, true);
                player.getPA().sendInterfaceDisplayState(childIndex, false);
                player.getPA().sendItemOnInterface(childIndex++, reward.getId(), reward.getAmount());
                continue;
            }

            int chance = Misc.random(4000);

            boolean rare = true;

            Item item;

            if (chance == 800) { //super rare
                item = SUPER_RARE_REWARDS[Misc.random(SUPER_RARE_REWARDS.length - 1)];
            } else if (chance <= 1000) { //rare
                item = RARE_REWARDS[Misc.random(RARE_REWARDS.length - 1)];
            } else if (chance <= 1500) { //uncommon
                item = UNCOMMON_REWARDS[Misc.random(UNCOMMON_REWARDS.length - 1)];
            } else { 
                item = COMMON_REWARDS[Misc.random(COMMON_REWARDS.length - 1)];
            }

            player.getPA().sendInterfaceDisplayState(childIndex++, true);
            player.getPA().sendInterfaceDisplayState(childIndex, false);
            player.getPA().sendItemOnInterface(childIndex++, item.getId(), item.getAmount());

        }
    }

    static void sendAnnouncement(String announcement) {
        for (Player player : World.getPlayers()) {
            if (player != null) {
                if (player.boxAlertEnabled)
                    player.getPacketSender().sendMessage(announcement);
            }
        }

    }

    public static Item[] COMMON_REWARDS = new Item[]{
            new Item(Items.VIKING_FULLHELM, 1),
            new Item(Items.DESERT_EAGLE, 1),
            new Item(Items.VIKING_LEGS, 1),
            new Item(Items.VIKING_BODY, 1),
            new Item(Items.BLOOD_NECKLACE, 1),
            new Item(Items.EXOTIC_RING, 1),
            new Item(Items.EXOTIC_AMULET, 1),
            new Item(Items.DOPE_BOOTS, 1),
            new Item(Items.DOPE_GLOVES, 1),
            new Item(Items.RAN_KNIGHT_BODY, 1),
            new Item(Items.RAN_KNIGHT_LEGS, 1),
            new Item(Items.RAN_KNIGHT_HELM, 1),
            new Item(Items.RAN_KNIGHT_GLOVES, 1),
            new Item(Items.RAN_KNIGHT_BOOTS, 1),
            new Item(Items.RAN_KNIGHT_CAPE, 1),
            new Item(Items.AR_15, 1),
            new Item(Items.MP5_SUB, 1),
            new Item(Items.ASALIAS_HELM, 1),
            new Item(Items.ASALIAS_BODY, 1),
            new Item(Items.ASALIAS_LEGS, 1),
            new Item(Items.ASALIAS_GLOVES, 1),
            new Item(Items.ASALIAS_WINGS, 1),
            new Item(Items.ASALIAS_BOOTS, 1),
            new Item(Items.ASALIAS_2h, 1),
            
    };

    public static Item[] UNCOMMON_REWARDS = new Item[]{
            new Item(Items.RAZ_RANGER_HELM, 1),
            new Item(Items.RAZ_RANGER_BODY, 1),
            new Item(Items.RAZ_RANGER_LEGS, 1),
            new Item(Items.RAZ_RANGER_GLOVES, 1),
            new Item(Items.RAZ_RANGER_BOOTS, 1),
            new Item(Items.ONE_MILL_TOKEN, 200),
            new Item(Items.EXOTIC_SWORDUP, 1),
            new Item(Items.EXOTIC_SWORD, 1),
            new Item(Items.EXOTIC_SHIELD, 1),
            new Item(Items.INFERNAL_OFF_HAND, 1),
            new Item(Items.INFERNAL_MAIN_HAND, 1),
            new Item(Items.RANGE_TANK_HELM, 1),
            new Item(Items.RANGE_TANK_BODY, 1),
            new Item(Items.RANGE_TANK_LEGS, 1),
            new Item(Items.THORS_RING, 1),
            new Item(Items.MAGIC_LIGHT_BODY, 1),
            new Item(Items.WHITE_KNIGHT_TORVA_HELM, 1),
            new Item(Items.WHITE_KNIGHT_TORVA_BODY, 1),
            new Item(Items.WHITE_KNIGHT_TORVA_LEGS, 1),
            new Item(Items.IRONMAN_HELM, 1),
            new Item(Items.IRONMAN_BODY, 1),
            new Item(Items.IRONMAN_LEGS, 1),
            new Item(Items.IRONMAN_BOOTS, 1),
            new Item(Items.MAGIC_LIGHT_LEGS, 1),
            new Item(Items.MAGIC_LIGHT_HELM, 1),
    };

    public static Item[] RARE_REWARDS = new Item[]{
            new Item(Items.TRI_BRID_AMULET, 1),
            new Item(Items.GODS_RANGER_RING, 1),
            new Item(Items.DARTH_VADER_BODY, 1),
            new Item(Items.DARTH_VADER_LEGS, 1),
            new Item(Items.DARTH_VADER_HELM, 1),
            new Item(Items.GOKU_HELM, 1),
            new Item(Items.GOKU_LEGS, 1),
            new Item(Items.GOKU_BODY, 1),
            new Item(Items.GOKU_BOOTS, 1),
            new Item(Items.GOKU_GLOVES, 1),
            new Item(Items.VEGETA_BODY, 1),
            new Item(Items.VEGETA_LEGS, 1),
            new Item(Items.VEGETA_GLOVES, 1),
            new Item(Items.VEGETA_HELM, 1),
            new Item(Items.VEGETA_BOOTS, 1),
            new Item(Items.VEGETA_PET, 1),
            new Item(Items.DEMON_EDGE_HELM, 1),
            new Item(Items.DEMON_EDGE_BODY, 1),
            new Item(Items.DEMON_EDGE_LEGS, 1),
            new Item(Items.SKELE_HELM, 1),
            new Item(Items.SKELE_BODY, 1),
            new Item(Items.SKELE_LEGS, 1),
            new Item(Items.OP_RING, 1),
            new Item(Items.ONE_MILL_TOKEN, 500),
            new Item(Items.DIGIMON_PET, 1),
    };

    public static Item[] SUPER_RARE_REWARDS = new Item[]{
            new Item(Items.YELLOW_DRAGON_CAPE, 1),
            new Item(Items.PURPLE_DRAGON_CAPE, 1),
            new Item(Items.STORM_TROOPER_HELM, 1),
            new Item(Items.STORM_TROOPER_BODY, 1),
            new Item(Items.STORM_TROOPER_LEGS, 1),
            new Item(Items.STORM_TROOPER_GLOVES, 1),
            new Item(Items.STORM_TROOPER_BOOTS, 1),
            new Item(Items.LEVIATHAN_LEGS, 1),
            new Item(Items.LEVIATHAN_BODY, 1),
            new Item(Items.LEVIATHAN_HELM, 1),
            new Item(Items.LEVIATHAN_GLOVES, 1),
            new Item(Items.LEVIATHAN_BOOTS, 1),
            new Item(Items.WHITE_BLASTER, 1),
            new Item(Items.PURPLE_BLASTER, 1),
            new Item(Items.PORTAL_GUN, 1),
            new Item(Items.BLUE_BLASTER, 1),
            new Item(Items.DONATOR_ARMOUR_HELM, 1),
            new Item(Items.INFERNAL_SKULL, 1),
            new Item(Items.OP_AMULET, 1),
            new Item(Items.CHUCKY_DOLL, 1),
            new Item(Items.LIGHTNING_HAMMER, 1),
            new Item(Items.DONATOR_ARMOUR_LEGS, 1),
            new Item(Items.ONE_BILL_TOKEN, 1),
            new Item(Items.DONATOR_ARMOUR_BODY, 1),
            new Item(Items.SCROLL, 1 ),
    };
}
