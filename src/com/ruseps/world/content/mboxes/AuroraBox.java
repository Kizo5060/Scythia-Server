package com.ruseps.world.content.mboxes;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.GameSettings;
import com.ruseps.model.container.impl.Equipment;


public class AuroraBox 
{
	private final Player plr;
	private final int BOX = 21055;
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

	public AuroraBox(Player plr) {
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
						{ 8656, 8657, 8658, 8659, 8863, 8864, 8865,11661, 11662, 11679, 11680, 11681, 11682, 20656, 11658,  },//Common, 0
						{ 938, 937, 19098, 19099, 19100, 798, 799, 894, 895, 896, 5090, 5091, 5092, 20650, 20651, 20652, },
						{ 19067, 19068, 19069, 19070, 19071, 19072, 20653, 20654, 938, 937, 11651, 5085, 5086, 5087, 5088, 5089, 8518 },
						{ 19048, 19049, 19050, 19051, 19052, 19053, 19054, 19055, 940, 10991, 10992, 10993, 10994, 10995, 10480, 10822, },
						{ 19056, 19057, 19058, 19059, 19060, 19061, 19062, },
						{ 19082, 19080, 19084, 21023, 21024, 21025, 21026, 21027, 21028 },
						{ 21056, 19085, 21004, 21005, 21006,  10824, 10826, 11206, 11208, } };

		int[] common = { 8656, 8657, 8658, 8659, 8863, 8864, 8865,  };
		int[] uncommon = { 938, 937, 19098, 19099, 19100, };
		int[] veryUncommon = { 19067, 19068, 19069, 19070, 19071, 19072, };
		int[] rare = { 19048, 19049, 19050, 19051, 19052, 19053, 19054, 19055, };
		int[] veryRare = { 19056, 19057, 19058, 19059, 19060, 19061, 19062, };
		int[] extremelyRare = { 19082, 19080, 19084, 21023, 21024, 21025, 21026, 21027, 21028  };
		int[] legendary = { 21056, 19085, 21004, 21005, 21006, };


		int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
		int[] tier = all[Misc.random(6)];
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
		plr.auroraBoxx = false;
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
			plr.sendMessage("<col=FFA500>" + plr.getUsername() + " has just won a " + "@bla@" + name);
		}else {
			World.sendMessage("<img=41><col=510000><shad=1>[Scythia Mbox]<img=41><col=0b5394><shad=1>" + plr.getUsername() + " won a @red@" + name + " <col=0b5394>from a @red@Aurora MBox");
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
		plr.auroraBoxx = true;
		
		//resetInterface(plr);
		plr.getPA().sendInterface(INTERFACE_ID);
	}
}