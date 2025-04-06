package com.ruseps.world.content.new_raids_system.raids_loot.raids_three_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsThreeChest {
	
    public static int raidsKey3 = 18872;
    public static int rareLoots[] = { 19106, 19107, 19117, 19118, 19119, 19120, 19121, 19122, };
    public static int ultraLoots[] = { 1031, 1033, 1035, 11896, 21057, 21058, 21059, 21060, 21061, 21062, 21063, 21064, };
    public static int amazingLoots[] = { 19110, 19112, 19113, 19114, 19115, };
    public static int commonLoots[] = { 19103, 19104, 19105, 19108, 19109, };
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey3)) {
            player.getInventory().delete(raidsKey3, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(12, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(13, 1);
            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Silver Raids Chest...");
                    giveReward(player);
                    player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_SILVER_RAIDS_50_TIMES, 1);
					player.setSilverRaidsOpened(player.getSilverRaidsOpened() + 1);
                    player.getBattlePass().addExperience(50 + Misc.getRandom(150)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #3 Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 980) { //Rare Items
	        int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.SILVER_RAID, new Item(rareDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(rareDrops, 1);
	            player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(rareDrops, 1);
	            World.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
	        }
	    } else if (chance >= 900) {//Ultra Rare items
	        int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.SILVER_RAID, new Item(ultraDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(ultraDrops, 1);
	            player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(ultraDrops, 1);
	            World.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
	        }
	    } else if (chance >= 850) {//Amazing items
	        int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.SILVER_RAID, new Item(amazingDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(amazingDrops, 1);
	            player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(amazingDrops, 1);
	            World.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
	        }
	    } else if (chance >= 800) {//Common items
	        int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.SILVER_RAID, new Item(commonDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(commonDrops, 1);
	            player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest, which has been sent to the bank.");
	        } else {
	            player.getInventory().add(commonDrops, 1);
	            World.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest!");
	        }
		} else {
				player.getInventory().add(19992, 4 + Misc.getRandom(8));	    
		}
	}
}
