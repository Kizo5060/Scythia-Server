package com.ruseps.world.content.mboxes;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.GameSettings;
import com.ruseps.model.Item;
import com.ruseps.model.container.impl.Equipment;


public class PetBox
{
    private final Player plr;
    private final int BOX = 13016;
    private final int INTERFACE_ID = 47000;
    private final int ITEM_FRAME = 47101;
    private int spinNum = 0;
    private boolean canMysteryBox = true;
    public static int mysteryPrize;
    private int mysteryPrizeTier;
    public static int loot[][] = {
            { 11981, 11982, 11983, 18986, },//Common, 0
            { 11984, 11985, 11986, 18989, },
            { 11987, 11988, 11989, 18985, },
            { 11990, 11991, 11992, 11993, 18987 },
            { 11896, 11979, 11994, 11997, 18984, 12004 },
            { 12655, 13247, 11995, 11996, 19868 },
            { 2703, 11949, 1685, 2542, 2546, 11978 } };

    public int getSpinNum() { return spinNum; }
    public boolean canMysteryBox() { return canMysteryBox; }
    public int getMysteryPrize() { return mysteryPrize; }
    public int getMysteryPrizeTier() { return mysteryPrizeTier; }

    public PetBox(Player plr) {
        this.plr = plr;
    }

    public void spin() {
        if (!canMysteryBox) {
            plr.sendMessage("Please finish your current spin.");
            return;
        }
        if (!plr.getInventory().contains(BOX)) {
            plr.sendMessage("You require a mystery box to do this.");
            return;
        }

        plr.getInventory().delete(BOX, 1);
        plr.sendMessage(":spin");
        process();
    }

    public void process() {
        mysteryPrize = -1;
        canMysteryBox = false;

        int rewards2[][] = {
                { 11981, 11982, 11983, 18986, },//Common, 0
                { 11984, 11985, 11986, 18989, },
                { 11987, 11988, 11989, 18985, },
                { 11990, 11991, 11992, 11993, 18987 },
                { 11896, 11979, 11994, 11997, 18984 },
                { 12655, 13247, 11995, 11996, 19868 },
                { 2703, 11949, 1685, 2542, 2546, 11978 } };

        int[] common = { 11981, 11982, 11983, 18986, };
        int[] uncommon = { 11984, 11985, 11986, 18989, };
        int[] veryUncommon = { 11987, 11988, 11989, 18985, };
        int[] rare = { 11990, 11991, 11992, 11993, 18987 };
        int[] veryRare = { 11896, 11979, 11994, 11997, 18984  };
        int[] extremelyRare = { 12655, 13247, 11995, 11996, 19868 };
        int[] legendary = {2703, 11949, 1685, 2542, 2546, 11978 };


        int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
        int[] tier;

		   int random = RandomUtility.exclusiveRandom(0, 100); // Generates a random number between 0 and 99
		   if (random < 40) {
	        	tier = all[0]; // Common (40% probability)
	        } else if (random < 65) {
	        	tier = all[1]; // Uncommon (25% probability)
	        } else if (random < 75) {
	        	tier = all[2]; // Very Uncommon (10% probability)
	        } else if (random < 85) {
	        	tier = all[3]; // Rare (5% probability)
	        } else if (random < 90) {
	        	tier = all[4]; // Very Rare (5% probability)
	        } else if (random < 95) {
	        	tier = all[5]; // Extremely Rare (5% probability)
	        } else {
	        	tier = all[6]; // Legendary (5% probability)
	        }
        int PRIZE_ID = tier[Misc.random(tier.length -1)];

        //final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
        mysteryPrize = PRIZE_ID; // For rewards handling

        // Send items to interface
        // Move non-prize items client side if you would like to reduce server load
        if (spinNum == 0) {
            for (int i=0; i<66; i++){
                tier = all[Misc.random(6)];
                final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
                sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
            }
        } else {
            for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
                tier = all[Misc.random(6)];
                final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
                sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
            }
        }
        spinNum++;
    }

    public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID) {
        if (i == prizeSlot) {
            plr.getPA().mysteryBoxItemOnInterface(PRIZE_ID, 1, ITEM_FRAME, i);
        }
        else {
            plr.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, 1, ITEM_FRAME, i);
        }
    }

    public void reward() {
        if (mysteryPrize == -1) {
            return;
        }

        plr.getInventory().add(mysteryPrize, 1);
        plr.getCollectionLogManager().addItem(BOX, new Item(mysteryPrize,1));
        plr.petBoxx = false;
        // Reward text colour
        String tier = "";
        switch (mysteryPrizeTier) {
            case 0: tier = "<col=336600>"; break; // Common
            case 1: tier = "<col=005eff>"; break; // Uncommon
            case 2: tier = "<col=ff3000>"; break; // Very Uncommon
            case 3: tier = "<col=B80000>"; break; // Rare
            case 4: tier = "<col=ff00ff>"; break; // Very Rare
            case 5: tier = "<col=ffffff>"; break; // Extremely Rare
            case 6: tier = "<col=FFA500>"; break; // Legendary
        }

        // Reward message
        String name = ItemDefinition.forId(mysteryPrize).getName();

        if (mysteryPrizeTier >= 0) {
            plr.sendMessage("<col=FFA500>" + plr.getUsername() + " you just won a " + "@bla@" + name);
        } else {
            World.sendMessage("<img=41><col=510000><shad=1>[Pet box]<img=41><col=0b5394><shad=1>" + plr.getUsername() + " won a @red@" + name + " <col=0b5394>from a @red@Pet Box");
        }
        
        plr.getPA().sendInterfaceRemoval();

        // Can now spin again
        canMysteryBox = true;
    }
    public static void resetInterface(Player player) {
        player.getPA().sendItemOnInterface(47101, 0, 0);
        player.getPA().sendItemOnInterface(47102, 0, 0);
        player.getPA().sendItemOnInterface(47103, 0, 0);
        player.getPA().sendItemOnInterface(47104, 0, 0);
        player.getPA().sendItemOnInterface(47105, 0, 0);
        player.getPA().sendItemOnInterface(47106, 0, 0);
        player.getPA().sendItemOnInterface(47107, 0, 0);
        player.getPA().sendItemOnInterface(47108, 0, 0);
        player.getPA().sendItemOnInterface(47109, 0, 0);
        player.getPA().sendItemOnInterface(47110, 0, 0);
        player.getPA().sendItemOnInterface(47111, 0, 0);
        player.getPA().sendItemOnInterface(47112, 0, 0);
        player.getPA().sendItemOnInterface(47113, 0, 0);
        player.getPA().sendItemOnInterface(47114, 0, 0);
        player.getPA().sendItemOnInterface(47115, 0, 0);
        player.getPA().sendItemOnInterface(47116, 0, 0);
        player.getPA().sendItemOnInterface(47117, 0, 0);
        player.getPA().sendItemOnInterface(47118, 0, 0);
        player.getPA().sendItemOnInterface(47119, 0, 0);
        player.getPA().sendItemOnInterface(47120, 0, 0);
        player.getPA().sendItemOnInterface(47121, 0, 0);
    }

    public void openInterface() {
        plr.sendMessage(":resetBox");
        spinNum = 0;
        plr.petBoxx = true;

        //resetInterface(plr);
        plr.getPA().sendInterface(INTERFACE_ID);
    }
}
