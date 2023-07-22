package com.ruseps.world.content.new_raids_system.raids_loot.raids_six_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsSixChest {
	
    public static int raidsKey7 = 4015;
    public static int rareLoots[] = {2848, 2849, 2850, 2851, 2852, 2853, 2854, 2855, 18941 };
    public static int ultraLoots[] = { 3490, 3491, 3492, 3494, 3493, 18948, 2679, 18966 };
    public static int amazingLoots[] = { 18943, 18947, 18949, 18951, 18952, 18953, 18955, 18956 };
    public static int commonLoots[] = { 18939 };
    
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey7)) {
            player.getInventory().delete(raidsKey7, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Raids #6 Chest...");
                    giveReward(player);
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
		
		if (chance >= 980) { //Rare Items
			int rareDrops = getRandomItem(rareLoots);
				player.getInventory().add(rareDrops, 1);
				player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!");
		} else if (chance >= 900) {//Ultra Rare items
				player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
		} else if (chance >= 850) {//Amazing items
				player.sendMessage("@or3@[Plat Raids Chest]@bla@ " + player.getUsername() + " has received a Rare Reward from the chest!!");
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
