package com.ruseps.world.content.new_raids_system.raids_loot.raids_five_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsFiveChest {
	
    public static int raidsKey5 = 2750;
    public static int rareLoots[] = {2749, 2751, 2753, 2756, 2754, 2755 };
    public static int ultraLoots[] = { 2765, 2766, 2767, 2773, 2775, 2777, 2779, 2781, 2784, 2787 };
    public static int amazingLoots[] = {2759, 2760, 2761, 2762, 2764, };
    public static int commonLoots[] = { 2768, 2769, 2770, 2771, 2772, 2789  };
    
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey5)) {
            player.getInventory().delete(raidsKey5, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Raids #5 Chest...");
                    giveReward(player);
                    this.stop();
                    
                }
            });
        } else {
            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #5 Key to open this chest!");
            return;
        }
    }
    
	public static void giveReward(Player player) {
		int chance = Misc.getRandom(1000);
		
		if (chance >= 980) { //Rare Items
			int rareDrops = getRandomItem(rareLoots);
				player.getInventory().add(rareDrops, 1);
				player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!");
		} else if (chance >= 900) {//Ultra Rare items
				player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
		} else if (chance >= 850) {//Amazing items
				player.sendMessage("@or3@[Silver Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!!");
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
