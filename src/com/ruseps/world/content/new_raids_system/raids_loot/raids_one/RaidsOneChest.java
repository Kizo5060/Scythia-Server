package com.ruseps.world.content.new_raids_system.raids_loot.raids_one;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsOneChest {
	
    public static int raidsKey = 5585;
    public static int rareLoots[] = {19098, 19100, 19099, 19093, 19096, 1002, 938, 19097};
    public static int ultraLoots[] = {20650, 20651, 20652, 20653, 20654, 20658, 20659, 20660};
    public static int amazingLoots[] = {8871, 8860, 8861, 8862, 999 };
    public static int commonLoots[] = {15501, 18986, 11659, 7080, 5585};
    
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey)) {
            player.getInventory().delete(raidsKey, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Pokemon Raids Chest...");
                    giveReward(player);
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
				player.getInventory().add(rareDrops, 1);
				World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received a Rare from the chest!");
		} else if (Misc.getRandom(40) == 15) {//Ultra Rare items
				World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
		} else if (Misc.getRandom(20) == 10) {//Amazing items
				World.sendMessage("@or3@[Pokemon Raids Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!!");
			int amazingDrops = getRandomItem(amazingLoots);
				player.getInventory().add(amazingDrops, 1);
		}  else if (Misc.getRandom(15) == 5) {//Amazing items
			int commonDrops = getRandomItem(commonLoots);
				player.getInventory().add(commonDrops, 1);
		} else {
				player.getInventory().add(19992, 1 + Misc.getRandom(8));	    
		}
	}
}
