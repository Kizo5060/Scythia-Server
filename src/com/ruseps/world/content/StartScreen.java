package com.ruseps.world.content;

import com.ruseps.model.GameMode;
import com.ruseps.model.Item;
import com.ruseps.world.World;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.entity.impl.player.Player;

/* START SCREEN */
public class StartScreen 
{
    public enum GameModes {
        NORMAL("Normal", 52761, -12780, 1, 0,
                new Item[]{new Item(2440, 10), new Item(14793, 250), new Item(2436, 10), new Item(2442, 10),
                        new Item(3025, 10), new Item(15272, 300), new Item(10477, 1), new Item(19888, 1),
                        new Item(10479, 1), new Item(10478, 1), (new Item(4391, 1)), (new Item(5349, 1)),
                        (new Item(19920, 1)), (new Item(21014, 1)), (new Item(10484, 1)),new Item(20531, 1), (new Item(11896, 1))},
                "Play in Normal Mode.", "", "", "", "", "", ""),
        IRONMAN("  Ironman", 52762, -12779, 1, 1,
        		new Item[]{new Item(2440, 10), new Item(14793, 250), new Item(2436, 10), 
                        new Item(3025, 10), new Item(15272, 500), new Item(10481, 1), new Item(913, 1),
                        new Item(19024, 1), (new Item(914, 1)), (new Item(916, 1)), (new Item(10483, 1)),new Item(19888, 1), (new Item(11896, 1)), (new Item(4391, 1)), (new Item(10484, 1)),
                        (new Item(19920, 1)),  new Item(21014, 1), (new Item(20531, 1))},
                "Play NexArch as an Iron man.", "", "", "", "", "", ""),
        ULTIMATE_IRON("  Ultimate Iron", 52763, -12778, 1, 2,
        		new Item[]{new Item(2440, 10), new Item(14793, 250), new Item(2436, 10), 
                        new Item(3025, 10), new Item(15272, 500), new Item(10481, 1), new Item(913, 1),
                        new Item(19024, 1), (new Item(914, 1)), (new Item(916, 1)), (new Item(10483, 1)),new Item(19888, 1), (new Item(11896, 1)), (new Item(4391, 1)), (new Item(10484, 1)),
                        (new Item(19920, 1)),  new Item(21014, 1), (new Item(20531, 1))},
                "Play NexArch as a Ultimate Ironman.", "", "", "", "", "", ""),
        /**(GROUP_IRONMAN("  Group Ironman", 52774, -12763, 1, 3,
                new Item[] new Item(2440, 10), new Item(14793, 250), new Item(2436, 10), new Item(2442, 10),
                        new Item(3024, 10), new Item(15272, 600), new Item(10481, 1), new Item(19888, 1),
                        new Item(913, 1), new Item(19024, 1), (new Item(10484, 1)), (new Item(4391, 1)), (new Item(11896, 1)),
                        (new Item(20523, 1)), (new Item(914, 1)), (new Item(916, 1)), (new Item(10483, 1)), (new Item(21014, 1))},
                "Play NexArch as a Group Ironman.", "", "", "", "", "", ""),
        /**VETERAN("  Veteran", 52778, -12760, 1, 4,
                new Item[]{new Item(2440, 10), new Item(14793, 250), new Item(2436, 10), new Item(2442, 10),
                        new Item(3024, 10), new Item(15272, 600), new Item(995, 2000000), new Item(10477, 1),
                        new Item(10479, 1), new Item(10478, 1), (new Item(10484, 1)), (new Item(4391, 1)),
                        (new Item(1478, 1)), (new Item(20523, 1)), (new Item(21014, 1)), (new Item(21015, 1))},
                "Play NexArch as a Veteran.", "", "", "", "", "", ""),*/
        ;

        private String name;
        private int stringId;
        private int checkClick;
        private int textClick;
        private int configId;
        private Item[] starterPackItems;
        private String line1;
        private String line2;
        private String line3;
        private String line4;
        private String line5;
        private String line6;
        private String line7;

        private GameModes(String name, int stringId, int checkClick, int textClick, int configId, Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5, String line6, String line7) {
            this.name = name;
            this.stringId = stringId;
            this.checkClick = checkClick;
            this.textClick = textClick;
            this.configId = configId;
            this.starterPackItems = starterPackItems;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.line4 = line4;
            this.line5 = line5;
            this.line6 = line6;
            this.line7 = line7;
        }
    }

    public static void open(Player player) {
        sendNames(player);
        player.getPacketSender().sendInterface(52750);
        player.selectedGameMode = GameModes.NORMAL;
        check(player, GameModes.NORMAL);
        sendStartPackItems(player, GameModes.NORMAL);
        sendDescription(player, GameModes.NORMAL);
    }

    public static void sendDescription(Player player, GameModes mode) {
        int s = 52764;
        player.getPacketSender().sendString(s, mode.line1);
        player.getPacketSender().sendString(s + 1, mode.line2);
        player.getPacketSender().sendString(s + 2, mode.line3);
        player.getPacketSender().sendString(s + 3, mode.line4);
        player.getPacketSender().sendString(s + 4, mode.line5);
        player.getPacketSender().sendString(s + 5, mode.line6);
        player.getPacketSender().sendString(s + 6, mode.line7);
    }

    public static void sendStartPackItems(Player player, GameModes mode) {
        final int START_ITEM_INTERFACE = 59025;
        for (int i = 0; i < 28; i++) {
            int id = -1;
            int amount = 0;
            try {
                id = mode.starterPackItems[i].getId();
                amount = mode.starterPackItems[i].getAmount();
            } catch (Exception e) {

            }
            player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
        }
    }

    public static boolean handleButton(Player player, int buttonId) {
        final int CONFIRM = -12767;
        if (buttonId == CONFIRM) {
            if (player.didReceiveStarter() == true) {
                return true;
            }
            
            player.getPacketSender().sendInterfaceRemoval();
            player.setReceivedStarter(true);
            handleConfirm(player);
            addStarterToInv(player);
            ClanChatManager.join(player, "help");
            player.setPlayerLocked(false);
            player.getPacketSender().sendInterface(3559);
            player.getAppearance().setCanChangeAppearance(true);
            player.setNewPlayer(false);
            player.getRefferalHandler().openRefferal(player);
           // World.sendMessage("<img=18><shad=1><col=ffca3c>[New Arrival] " + player.getUsername() + " has just logged into @cya@NexArch<col=ffca3c> for the first time!");
            player.getPacketSender().sendRights();

            return true;
        }
        
        for (GameModes mode : GameModes.values()) {
            if (mode.checkClick == buttonId || mode.textClick == buttonId) {
                selectMode(player, mode);
                return true;
            }
        }
        return false;

    }

    public static void handleConfirm(Player player) {
        if (player.selectedGameMode == GameModes.IRONMAN) {
            GameMode.set(player, GameMode.IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.ULTIMATE_IRON) {
            GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
        } else {
            GameMode.set(player, GameMode.NORMAL, false);
        }
    }

    public static void addStarterToInv(Player player) {
        for (Item item : player.selectedGameMode.starterPackItems) {
            player.getInventory().add(item);
        }
    }

    public static void selectMode(Player player, GameModes mode) {
        player.selectedGameMode = mode;
        System.out.println("Set mode: " + mode);
        check(player, mode);
        sendStartPackItems(player, mode);
        sendDescription(player, mode);
    }

    public static void check(Player player, GameModes mode) {
        for (GameModes gameMode : GameModes.values()) {
            if (player.selectedGameMode == gameMode) {
                player.getPacketSender().sendConfig(gameMode.configId, 1);
                continue;
            }
            player.getPacketSender().sendConfig(gameMode.configId, 0);
        }
    }

    public static void sendNames(Player player) {
        for (GameModes mode : GameModes.values()) {
            player.getPacketSender().sendString(mode.stringId, mode.name);
        }
    }
}
