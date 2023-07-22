package com.ruseps;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.*;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BalloonDropParty {

    private static final int STARTING_POINT = 37500;

    private static int amountDonated = 0;
    private static int requiredAmount = 300;
    private static final List<Item> items = new ArrayList<>();

    public static void open(Player player) {
        updateInterface(player);
    }

    public static void addDonation(Player player, int amount) {
        amountDonated += amount;
        updateInterface(player);
    }

    public static void setRequiredAmount(int amount) {
        requiredAmount = amount;
    }

    private static void updateInterface(Player player) {
        player.getPacketSender().sendInterface(STARTING_POINT);
        player.getPacketSender().sendInterfaceSet(STARTING_POINT, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
        player.getPacketSender().resetItemsOnInterface(STARTING_POINT + 11, 50);
        player.getPacketSender()
                .sendItemArrayOnInterface(STARTING_POINT + 11, items.toArray(new Item[0]));
        player.getPacketSender()
                .sendString(STARTING_POINT + 6, "@whi@Hey there! We are currently at @red@$" + amountDonated + "/$" + requiredAmount + "@whi@ for a Drop Party");
    }

    public static void addItem(Player player, Item item) {
        if(!player.getInventory().contains(item.getId())) {
            return;
        }
        items.add(item.copy());
        player.getInventory().delete(item);
        updateInterface(player);
    }

    public static void spawn(Player player) {
        amountDonated = 0;
        player.getPacketSender().sendInterfaceRemoval();
        updateInterface(player);
        final Position startingPosition = player.getPosition();

        int startX = 3367;
        int startY = 3095;
        int offset = 9;

        Position[] positions = new Position[(int) Math.pow((2 * offset + 1), 2) - 1];
        System.out.println(positions.length);

        int index = 0;

        for (int x = startX - offset; x <= startX + offset; x++) {

            for (int y = startY - offset; y <= startY + offset; y++) {

                if (x == startX && y == startY) {
                    continue;
                }
                positions[index] = new Position(x, y);
                index++;
            }
        }
        TaskManager.submit(new Task(1) {
            int currentIndex = 0;

            @Override
            protected void execute() {
                if (currentIndex >= positions.length) {
                    stop();
                }
                int randomIndex = ThreadLocalRandom.current().nextInt(positions.length);
                Position pos = positions[randomIndex];
                int random = ThreadLocalRandom.current().nextInt(7);
                GameObject object = new GameObject(115 + random, pos);
                CustomObjects.spawnGlobalObject(object);
                currentIndex++;
            }
        });
    }

    public static boolean pop(Player player, GameObject object) {
        if (object.getId() < 115 || object.getId() > 121) {
            return false;
        }
        if(items.size() == 0) {
            CustomObjects.globalObjectRemovalTask(object, 1);
            return false;
        }
        player.performGraphic(new Graphic(111));
        boolean gotReward = ThreadLocalRandom.current().nextInt(100) > 65;
        if (!gotReward) {
            return true;
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(items.size());
        Item reward = items.remove(randomIndex);
        GroundItemManager.spawnGroundItem(player,
                new GroundItem(reward, object.getPosition(), player.getUsername(), true, 75, false, 100));
        CustomObjects.globalObjectRemovalTask(object, 1);
        return true;
    }
}
