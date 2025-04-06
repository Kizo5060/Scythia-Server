package com.ruseps.world.content.mboxes;

import com.ruseps.GameSettings;
import com.ruseps.model.Item;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class OwnerBox 
{
	private final Player plr;
	private final int BOX = 21056;
	private final int INTERFACE_ID = 47000;
	private final int ITEM_FRAME = 47101;
	private int spinNum = 0;
	private boolean canMysteryBox = true;
	public static int mysteryPrize;
	private int mysteryPrizeTier;

	public int getSpinNum() { return spinNum; }
	public boolean canMysteryBox() { return canMysteryBox; }
	public int getMysteryPrize() { return mysteryPrize; }
	public int getMysteryPrizeTier() { return mysteryPrizeTier; }
	public static int loot[][] = {
			{ 405, 18982, 18983, 18993, 19021, 2545, 18974, 18975, 18976, 10935 },
			{ 6832, 7960, 21056, 6831, 2542, 6830, 18977, 18978, 18979, 19014, 19015, 1413, 19671, 18062, 10943, } };

	public OwnerBox(Player plr) {
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
						{ 11067, 11068, 11071, 11078, 11087, 1007, 1002, 1005, 965, 1000  },//Common, 0
						{ 902, 903, 904, 905, 906, 15246, 897, 898, 899, 900, 901, 21027, 21018, 21019, 21020, 21021, 21022  },
						{ 18440, 18914, 18946, 20527, 20528, 20529, 15246, 21034, 21000, 21001, 21002, 21003 },
						{ 2544, 2546, 2547, 2548, 2549,21048, 21049, 21050, 21051, 21052, 21053, 21054, 18059, 10934 },
						{ 18968, 18969, 18970, 18971, 18972, 18973, 21040, 21041, 21042, 21043, 21044, 21045, 21046, 2104 },
						{ 405, 18982, 18983, 18993, 19021, 2545, 18974, 18975, 18976, 10935 },
						{ 6832, 7960, 21056, 6831, 2542, 6830, 18977, 18978, 18979, 19014, 19015, 1413, 19671, 18062, 10943, } };
				
		int[] common = { 11067, 11068, 11071, 11078, 11087, 1007, 1002, 1005, 965, 1000 };
		int[] uncommon = { 902, 903, 904, 905, 906, 15246, 897, 898, 899, 900, 901, 21027, 21018, 21019, 21020, 21021, 21022 };
		int[] veryUncommon = { 18440, 18914, 18946, 20527, 20528, 20529, 15246, 21034, 21000, 21001, 21002, 21003 };
		int[] rare = { 2544, 2546, 2547, 2548, 2549,21048, 21049, 21050, 21051, 21052, 21053, 21054, 18059, 10934 };
		int[] veryRare = { 18968, 18969, 18970, 18971, 18972, 18973, 21040, 21041, 21042, 21043, 21044, 21045, 21046, 21047};
		int[] extremelyRare = { 405, 18982, 18983, 18993, 19021, 2545, 18974, 18975, 18976, 10935 };
		int[] legendary = { 6832, 7960, 21056, 6831, 2542, 6830, 18977, 18978, 18979, 19014, 19015, 1413, 19671, 18062, 10943, };

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
		plr.getCollectionLogManager().addItem(BOX, new Item(mysteryPrize,1));
		plr.getInventory().add(mysteryPrize, 1);
		plr.ownerBoxx = false;
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
		if(mysteryPrizeTier >= 2) {
			plr.sendMessage("<col=FFA500>" + plr.getUsername() + " you just won a " + "@bla@" + name);
		}else {
			World.sendMessage("<img=40>@red@[RARE DROP]@bla@" + plr.getUsername() + " @bla@Recieved a " + name + " from@red@<shad=1> Owner Box");		}
	
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
		plr.ownerBoxx = true;
		
		//resetInterface(plr);
		plr.getPA().sendInterface(INTERFACE_ID);
	}

}