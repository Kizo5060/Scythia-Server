package com.ruseps.world.content.new_raids_system.raids_loot.raids_two_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;

public class RaidsTwoChest {
	
    public static int raidsKey2 = 21013;
    public static int rareLoots[] = { 21000, 21001, 21002, 21003, 21034, 1002, 21016, 21017, 21018, 21019, 21020, 21021, 21022 };
    public static int ultraLoots[] = { 21029, 21030, 21031, 21032, 21033, 21026, 21004, 21005, 21006 };
    public static int amazingLoots[] = { 21023, 21024, 21025, 21027, 21028, 21007, 21008, 21009, 21010, 21011, 21012   };
    public static int commonLoots[] = {19098, 19100, 19099, 19093, 19096, 938, 19097, 915 };
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey2)) {
            player.getInventory().delete(raidsKey2, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(30, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(31, 1);
            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Anime Raids Chest...");
                    giveReward(player);
    				player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_ANIME_RAIDS, 1);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_ANIME_RAIDS_50_TIMES, 1);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_ANIME_RAIDS_100_TIMES, 1);
					player.setAnimeRaidsOpened(player.getAnimeRaidsOpened() + 1);
                    player.getBattlePass().addExperience(50 + Misc.getRandom(150)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #2 Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 999) { //Rare Items
	        int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.ANIME_RAID, new Item(rareDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(rareDrops, 1);
	            player.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(rareDrops, 1);
	            World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
	        }
	    } else if (chance >= 975) {//Ultra Rare items
	        int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.ANIME_RAID, new Item(ultraDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(ultraDrops, 1);
	            player.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(ultraDrops, 1);
	            World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
	        }
	    } else if (chance >= 850) {//Amazing items
	        int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.ANIME_RAID, new Item(amazingDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(amazingDrops, 1);
	            player.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(amazingDrops, 1);
	            World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
	        }
	    } else if (chance >= 800) {//Amazing items
	        int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.ANIME_RAID, new Item(commonDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(commonDrops, 1);
	            player.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest, which has been sent to the bank.");
	        } else {
	            player.getInventory().add(commonDrops, 1);
	            World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest!");
	        }
		} else {
				player.getInventory().add(19992, 4 + Misc.getRandom(8));	    
		}
	}
}