package com.ruseps.world.content.new_raids_system.instances.dark_demention_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;

public class DarkDementionLoot {

    public static int raidsKey = 13234;
    public static int rareLoots[] = { 19077, 7020, 7024, 20532, 20517, 4080, 7010, 2732, 12003 };
    public static int ultraLoots[] = { 7022, 7026, 20524, 20525, 7014, 7012, 2849, 2854, 621  };
    public static int amazingLoots[] = { 20518, 7018, 7016, 1025, 1023, 2850, 17652, 3491, };
    public static int commonLoots[] = {19045, 19041, 19093, 19096, 19097, 3492 };
    public static int Loots[][] = {commonLoots,rareLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey)) {
            player.getInventory().delete(raidsKey, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(37, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(38, 1); 
            player.getDailyTaskManager().submitProgressToIdentifier(39, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(42, 1);
            player.getDailyTaskManager().submitProgressToIdentifier(44, 1);
            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Dark Dimension Raids Chest...");
                    giveReward(player);
                    player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_DARK_RAIDS_350_TIMES, 1);
					player.darkDementionRaidsOpened++;
                    player.getBattlePass().addExperience(150 + Misc.getRandom(150)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Dark Dimension Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 999) { //Rare Items
	        int rareDrops = getRandomItem(rareLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DD_RAID, new Item(rareDrops));

	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(rareDrops, 1);
	            player.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(rareDrops, 1);
	            World.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!");
	        }
	    } else if (chance >= 975) {//Ultra Rare items
	        int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DD_RAID, new Item(ultraDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(ultraDrops, 1);
	            player.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(ultraDrops, 1);
	            World.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
	        }
	    } else if (chance >= 850) {//Amazing items
	        int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DD_RAID, new Item(amazingDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(amazingDrops, 1);
	            player.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
	        } else {
	            player.getInventory().add(amazingDrops, 1);
	            World.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
	        }
	    } else if (chance >= 800) {//Amazing items
	        int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.DD_RAID, new Item(commonDrops));
	        if (player.getInventory().getFreeSlots() == 0) {
	            player.getBank(0).add(commonDrops, 1);
	            player.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest, which has been sent to the bank.");
	        } else {
	            player.getInventory().add(commonDrops, 1);
	            World.sendMessage("@or3@[Dark Dimension Raids Chest]@bla@ " + player.getUsername() + " received a Common Reward from the chest!");
	        }
		} else {
				player.getInventory().add(19992, 4 + Misc.getRandom(8));	    
		}
	}
}
