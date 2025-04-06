package com.ruseps.world.content.new_raids_system.raids_loot.raids_four_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsFourChest {
	
    public static int raidsKey4 = 2752;
    public static int rareLoots[] = { 21048, 21049, 21050, 21051, 21052, 21053, 21054, 2749, 2751, 2753, 2756, 2754, 2755, 17658, 17654};
    public static int ultraLoots[] = { 2773, 2775, 2777, 2779, 2781, 2784, 2787, };
    public static int amazingLoots[] = { 2768, 2769, 2770, 2771, 2772, 2789 };
    public static int commonLoots[] = { 11896, 21057, 21058, 21059, 21060, 21061, 21062, 21063, 21064, };
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey4)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(43, 1);
        	player.getDailyTaskManager().submitProgressToIdentifier(41, 1);
        	player.getDailyTaskManager().submitProgressToIdentifier(26, 1);
        	player.getDailyTaskManager().submitProgressToIdentifier(25, 1);
            player.getInventory().delete(raidsKey4, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Mortal Kombat Raids Chest...");
                    giveReward(player);
                	player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_MORTAL_KOMBAT_RAIDS_150_TIMES, 1);
					player.setMkRaidsOpened(player.getMkRaidsOpened() + 1);
                    player.getBattlePass().addExperience(50 + Misc.getRandom(100)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    //
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Mortal Kombat Raids Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 990) { //Rare Items
	        int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.MK_RAID, new Item(rareDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(rareDrops, 1);
	            player.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(rareDrops, 1);
	            World.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
	        }
	    } else if (chance >= 900) {//Ultra Rare items
	        int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.MK_RAID, new Item(ultraDrops));

	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(ultraDrops, 1);
	            player.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(ultraDrops, 1);
	            World.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
	        }
	    } else if (chance >= 850) {//Amazing items
	        int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.MK_RAID, new Item(amazingDrops));

	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(amazingDrops, 1);
	            player.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(amazingDrops, 1);
	            World.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
	        }
	    } else if (chance >= 800) {//Common items
	        int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.MK_RAID, new Item(commonDrops));

	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(commonDrops, 1);
	            player.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(commonDrops, 1);
	            World.sendMessage("@or3@[Mortal Kombat Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest!");
	        }
		} else {
				player.getInventory().add(19992, 4 + Misc.getRandom(8));	    
		}
	}
}
