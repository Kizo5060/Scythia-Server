package com.ruseps.world.content.new_raids_system.raids_loot.raids_eight;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.collectionLogs.CollectionLogs;
import com.ruseps.world.entity.impl.player.Player;

public class RaidsEightLoot
{	
    public static int raidsKey9 = 85;
    public static int ultraLoots[] = { 13033, 13032, 8668, 8672, 8674, 9077, 17666, 13035, 10537, 10942, 18057, 14443 };
    public static int amazingLoots[] = { 13034, 13030, 13031, 8676, 8680, 8678, 8670, 3510, 17668, 17660, 10942 };
    public static int commonLoots[] = { 19992 };
    public static int Loots[][] = {commonLoots,ultraLoots,amazingLoots};
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }
    
    public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(1000)) 
			return;
        if (player.getInventory().contains(85)) {
        	player.getDailyTaskManager().submitProgressToIdentifier(27, 1);
            player.getInventory().delete(85, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {  
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Ruby Raids Chest...");
                    giveReward(player);
                    player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_250_RUBY_RAIDS, 1);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_750_RUBY_RAIDS, 1);
					player.setRubyRaidsOpened(player.getRubyRaidsOpened() + 1);
                    player.getBattlePass().addExperience(350 + Misc.getRandom(250)); //(garunteed 50, chance of 1-250 after);
                    this.stop();
                    
                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #8 Key to open this chest!");
            return;
        }

    }
    
    public static void giveReward(Player player) {
        int chance = Misc.getRandom(1000);
        
        if (chance >= 950 && chance < 1000) { // Ultra Rare items (10% chance)
            int ultraDrops = getRandomItem(ultraLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.RUBY_RAID, new Item(ultraDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(ultraDrops, 1);
                player.sendMessage("@or3@[Ruby Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(ultraDrops, 1);
                World.sendMessage("@or3@[Ruby Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
            }
        } else if (chance >= 900 && chance < 950) { // Amazing items (10% chance)
            int amazingDrops = getRandomItem(amazingLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.RUBY_RAID, new Item(amazingDrops));

            if (player.getInventory().getFreeSlots() == 0) {
                player.getBank(0).add(amazingDrops, 1);
                player.sendMessage("@or3@[Ruby Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward, it has been sent to the bank.");
            } else {
                player.getInventory().add(amazingDrops, 1);
                World.sendMessage("@or3@[Ruby Raids Chest]@bla@ " + player.getUsername() + " has received an Amazing Reward from the chest!!");
            }
        } else { // Common items (80% chance)
            int commonDrops = getRandomItem(commonLoots);
	        player.getCollectionLogManager().addItem(CollectionLogs.RUBY_RAID, new Item(commonDrops),5);

            player.getInventory().add(commonDrops, 5);
        }
    }
}
