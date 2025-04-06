package com.ruseps.world.content.new_raids_system.raids_loot.raids_one;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsOneChest {
	
    public static int raidsKey = 5585;
    public static int rareLoots[] = {19098, 19100, 19099, 19093, 19096, 1002, 938, 19097};
    public static int ultraLoots[] = {20650, 20651, 20652, 20653, 20654, 20658, 20659, 20660};
    public static int amazingLoots[] = {8871, 8860, 8861, 8862, 999 };
    public static int commonLoots[] = {15501, 18986, 11659, 7080, 5585};
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(3, 1);
        	player.getDailyTaskManager().submitProgressToIdentifier(4, 1);
            player.getInventory().delete(raidsKey, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Pokemon Raids Chest...");
                    giveReward(player);
                    player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_POKEMON_RAIDS, 1);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_POKEMON_RAIDS_50_TIMES, 1);
					player.setPokemonRaidsOpened(player.getPokemonRaidsOpened() + 1);
                    player.getBattlePass().addExperience(50 + Misc.getRandom(50)); //(garunteed 50, chance of 1-150 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #1 Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		if (Misc.getRandom(50) == 20) { //Rare Item
	        int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.POKEMON_RAID, new Item(rareDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(rareDrops, 1);
	            player.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(rareDrops, 1);
	            World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
	        }
	    } else if (Misc.getRandom(40) == 15) {//Ultra Rare items
	        int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.POKEMON_RAID, new Item(ultraDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(ultraDrops, 1);
	            player.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(ultraDrops, 1);
	            World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
	        }
	    } else if (Misc.getRandom(20) == 10) {//Amazing items
	        int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.POKEMON_RAID, new Item(amazingDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(amazingDrops, 1);
	            player.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(amazingDrops, 1);
	            World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
	        }
	    } else if (Misc.getRandom(15) == 5) {//Amazing items
	    	
	        int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.POKEMON_RAID, new Item(commonDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(commonDrops, 1);
	            player.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest, which has been sent to the bank.");
	        } else {
	            player.getInventory().add(commonDrops, 1);
	            World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest!");
	        }
		} else {
				player.getInventory().add(19992, 1 + Misc.getRandom(8));	    
		}
	}
}
