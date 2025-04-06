package com.ruseps.world.content.new_raids_system.raids_loot.raids_six_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsSixChest {
	
    public static int raidsKey7 = 4015;
    public static int rareLoots[] = { 18949, 18951, 18952, 18953, 18955, 18956 };
    public static int ultraLoots[] = { 3490, 3491, 3492, 3494, 3493, 2679, 18966, };
    public static int amazingLoots[] = { 2848, 2849, 2850, 2851, 2852, 2853, 2854, 2855, 18057, 621, 17652, 17664, 14445};
    public static int boosterLoots[] = { 18943, 18947, 18948, };
    public static int commonLoots[] = { 18939 };
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots,boosterLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey7)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(22, 1);
            player.getInventory().delete(raidsKey7, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Plat Raids Chest...");
                    giveReward(player);
            		player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_PLAT_RAIDS_400_TIMES, 1);
					player.setPlatRaidsOpened(player.getPlatRaidsOpened() + 1);
                    player.getBattlePass().addExperience(150 + Misc.getRandom(250)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #6 Key to open this chest!");
            return;
        }

    }
    
    public static void giveReward(Player player) {
        int chance = Misc.getRandom(1000);
        
        if (chance >= 750) { // Rare Items (25% chance)
            int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.PLAT_RAID, new Item(rareDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(rareDrops, 1);
                player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(rareDrops, 1);
                World.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
            }
        } else if (chance >= 650 && chance < 700) { // Ultra Rare items (10% chance)
            int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.PLAT_RAID, new Item(ultraDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(ultraDrops, 1);
                player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(ultraDrops, 1);
                World.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
            }
        } else if (chance >= 600 && chance < 650) { // Amazing items (5% chance)
            int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.PLAT_RAID, new Item(amazingDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(amazingDrops, 1);
                player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(amazingDrops, 1);
                World.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
            }
        } else if (chance >= 590 && chance < 600) { // Damage Booster (1% chance)
            int boosterDrops = getRandomItem(boosterLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.PLAT_RAID, new Item(boosterDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(boosterDrops, 1);
                player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Damage Booster, it has been sent to the bank.");
            } else {
                player.getInventory().add(boosterDrops, 1);
                World.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Damage Booster from the chest!!");
            }
        } else { // Common items (85% chance)
            int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.PLAT_RAID, new Item(commonDrops));

            player.getInventory().add(commonDrops, 1);
        }
    }
}
