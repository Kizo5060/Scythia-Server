package com.ruseps.world.content.new_raids_system.instances.dragonstone_raids;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;

public class DragonstoneRaidsLoot {	
    public static int raidsKey = 275;
    public static int ultraLoots[] = { 10416, 19078, 19012, 7083, 2724, 2725, 2726, 10901, 18906, 17484, 10942, 10946, 14444 };
    public static int amazingLoots[] = {10418, 18975, 19059, 1046, 13193, 10900, 10902, 2727, 2729, 18653, 3470, 10906};
    public static int commonLoots[] = { 19994 };
    public static int Loots[][] = {ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(275)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(28, 1);
            player.getInventory().delete(275, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Dragonstone Raids Chest...");
                    giveReward(player);
                    player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_DRAGONSTONE_RAIDS_500_TIMES, 1);
					player.setDragonstoneRaidsOpened(player.getDragonstoneRaidsOpened() + 1);
                    player.getBattlePass().addExperience(500 + Misc.getRandom(250)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Dragonstone key to open this chest!");
            return;
        }

    }
    
    public static void giveReward(Player player) {
        int chance = Misc.getRandom(100);
        
        if (chance >= 99 && chance < 100) { // Ultra Rare items (5%chance)
            int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DS_RAID, new Item(ultraDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(ultraDrops, 1);
                player.sendMessage("@red@[Dragonstone Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(ultraDrops, 1);
                World.sendMessage("@red@[Dragonstone Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
            }
        } else if (chance >= 90 && chance < 97){ // Amazing items (15%chance)
            int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DS_RAID, new Item(amazingDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(amazingDrops, 1);
                player.sendMessage("@red@[Dragonstone Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(amazingDrops, 1);
                World.sendMessage("@red@[Dragonstone Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
            }
        } else { // Common items (80% chance)
            int commonDrops = getRandomItem(commonLoots);
            
            player.getInventory().add(commonDrops, 5);
        }
    }


}
