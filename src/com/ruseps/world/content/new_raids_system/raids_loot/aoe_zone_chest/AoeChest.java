package com.ruseps.world.content.new_raids_system.raids_loot.aoe_zone_chest;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;


public class AoeChest {
	
    public static int chestKey6 = 4003;
    public static int rareLoots[] = { 19920, 20134, 20150, 20154, 20156, 20158, 20160, 20162  };
    public static int ultraLoots[] = {20106, 20108, 20110, 20112, 20114, 20116  };
    public static int amazingLoots[] = { 7080, 20132, 20128, 18852, 18854, 18856, 18858, 18860, 18864, 18868  };
    public static int commonLoots[] = { 20120, 20122, 20124, 941, 944 };
    
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(chestKey6)) {
            player.getInventory().delete(chestKey6, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Aoe Zone Chest...");
                    giveReward(player);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Aoe Zone Key to open this chest!");
            return;
        }

    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 980) { //Rare Items
			int rareDrops = getRandomItem(rareLoots);
				player.getInventory().add(rareDrops, 1);
				player.sendMessage("@or3@[Aoe Zone Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!");
		} else if (chance >= 990) {//Ultra Rare items
				player.sendMessage("@or3@[Aoe Zone Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
		} else if (chance >= 950) {//Amazing items
				player.sendMessage("@or3@[Aoe Zone Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!!");
			int amazingDrops = getRandomItem(amazingLoots);
				player.getInventory().add(amazingDrops, 1);
		}  else if (chance >= 800) {//Amazing items
			int commonDrops = getRandomItem(commonLoots);
				player.getInventory().add(commonDrops, 1);
		} else {
				player.getInventory().add(19992, 4 + Misc.getRandom(8));	    
		}
	}
}
