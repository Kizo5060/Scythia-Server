package com.ruseps.world.content.new_raids_system.raids_loot.raids_seven_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsSevenChest {

    public static int raidsKey8 = 2747;
    public static int rareLoots[] = {3495, 3505, 3506, 3507, 3508, 3509, 19040, 19041, 19042, 19043, 19044, 19046};
    public static int ultraLoots[] = {19045, 19047, 10942, 3499, 3500, 3501, 3502, 3503, 3504, 3498, 3497, 21043,21047, 21045, 21044, 21042, 21041, 17656, 17662};
    public static int thepetLoots[] = {13021, };
    public static int Loots[][] = {rareLoots,ultraLoots,thepetLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }

    public static void openChest(Player player) {
        if(!player.getClickDelay().elapsed(1000))
            return;
        if (player.getInventory().contains(raidsKey8)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(23, 1);
            player.getInventory().delete(raidsKey8, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Diamond Raids Chest...");
                    giveReward(player);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_DIAMOND_RAIDS_250_TIMES, 1);
					player.setDiamondRaidsOpened(player.getDiamondRaidsOpened() + 1);
                    player.getBattlePass().addExperience(250 + Misc.getRandom(200)); //(garunteed 50, chance of 1-250 after);
                    this.stop();

                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #7 Key to open this chest!");
            return;
        }

    }

    public static void giveReward(Player player) {
        int chance = Misc.getRandom(100);

        if (chance >= 80) { // Rare Items (20% chance)
            int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DIAMOND_RAID, new Item(rareDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(rareDrops, 1);
                player.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(rareDrops, 1);
                World.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
            }
        } else if (chance >= 75 && chance < 80) { // Ultra Rare items (5% chance)
            int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DIAMOND_RAID, new Item(ultraDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(ultraDrops, 1);
                player.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(ultraDrops, 1);
                World.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
            }
        } else if (chance >= 74 && chance < 75) { // The Pet (0.5% chance)
            int petDrops = getRandomItem(thepetLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DIAMOND_RAID, new Item(petDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(petDrops, 1);
                player.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received a Special Pet, it has been sent to the bank.");
            } else {
                player.getInventory().add(petDrops, 1);
                World.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received a Special Pet from the chest!");
            }
        } else {
            player.getInventory().add(19992, 4 + Misc.getRandom(8));
        }
    }
}
