package com.ruseps.world.content.new_raids_system.raids_loot.raids_seven_loot;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsSevenChest {

    public static int raidsKey8 = 2747;
    public static int rareLoots[] = {3499, 3500, 3501, 3502, 3503, 3504, 3498};
    public static int ultraLoots[] = { 3495, 3505, 3506, 3507, 3508, 3509, 3497 };

    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }

    public static void openChest(Player player) {
        if(!player.getClickDelay().elapsed(1000))
            return;
        if (player.getInventory().contains(raidsKey8)) {
            player.getInventory().delete(raidsKey8, 1);

            TaskManager.submit(new Task(2, player, false) {
                @Override
                public void execute() {
                    player.getPacketSender().sendMessage("<img=10>@blu@Opening the Raids #7 Chest...");
                    giveReward(player);
                    this.stop();

                }
            });
        } else {

            player.getPacketSender().sendMessage("<img=10>@blu@You require a Raids #7 Key to open this chest!");
            return;
        }

    }

    public static void giveReward(Player player) {
        int chance = Misc.getRandom(1000);

        if (chance >= 980) { //Rare Items
            int rareDrops = getRandomItem(rareLoots);
            player.getInventory().add(rareDrops, 1);
            player.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received a Legendary Reward from the chest!");
        } else if (chance >= 900) {//Ultra Rare items
            player.sendMessage("@or3@[Diamond Raids Chest]@bla@ " + player.getUsername() + " has received an Ultra Rare Reward from the chest!");
            int ultraDrops = getRandomItem(ultraLoots);
            player.getInventory().add(ultraDrops, 1);
        } else {
            player.getInventory().add(19992, 4 + Misc.getRandom(8));
        }
    }
}
