package com.ruseps.world.content.new_raids_system.raids_loot.raids_two_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class RaidsTwoChest {
	
    public static int raidsKey2 = 21013;
    public static int rareLoots[] = { 21000, 21001, 21002, 21003, 21034, 1002, 21016, 21017, 21018, 21019, 21020, 21021, 21022 };
    public static int ultraLoots[] = { 21029, 21030, 21031, 21032, 21033, 21026, 21004, 21005, 21006 };
    public static int amazingLoots[] = { 21023, 21024, 21025, 21027, 21028, 21007, 21008, 21009, 21010, 21011, 21012   };
    public static int commonLoots[] = {19098, 19100, 19099, 19093, 19096, 938, 19097, 915 };
    
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(raidsKey2)) {
            player.getInventory().delete(raidsKey2, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Anime Raids Chest...");
                    giveReward(player);
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
				player.getInventory().add(rareDrops, 1);
				World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received a Rare from the chest!");
		} else if (chance >= 975) {//Ultra Rare items
				World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
		} else if (chance >= 850) {//Amazing items
				World.sendMessage("@or3@[Anime Raids Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!!");
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
