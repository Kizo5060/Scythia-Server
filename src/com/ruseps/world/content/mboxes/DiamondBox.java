package com.ruseps.world.content.mboxes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class DiamondBox {
	/**
	 * The player object that will be triggering this event
	 */
	private final Player plr;
	private final int BOX = 6854;
	private final int INTERFACE_ID = 47000;
	private final int ITEM_FRAME = 47101;
	private int spinNum = 0;
	private boolean canMysteryBox = true;
	private int mysteryPrize;
	private int mysteryPrizeTier;

	public int getSpinNum() { return spinNum; }
	public boolean canMysteryBox() { return canMysteryBox; }
	public int getMysteryPrize() { return mysteryPrize; }
	public int getMysteryPrizeTier() { return mysteryPrizeTier; }
	public static int loot[][] = {
			 {19045, 19047, 10934, 18059, 3499, 3500, 3501, 3502, 3503, 3504, 3498, 3497, 21043,21047, 21045, 21044, 21042, 21041, 17656, 17662 },
			 {21055 ,21056 ,18689},
			 {18063,13021},
			 { 21056, 19085,18960 },
			 {10943,1413,6854,18957}};

	/**
	 * Constructs a new mystery box to handle item receiving for this player alone
	 * @param plr the player
	 */
	public DiamondBox(Player plr) {
		this.plr = plr;
	}

	public void spin() {
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(BOX)) {
			plr.sendMessage("You require a Diamond box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(BOX, 1);
		// Initiate spin
	 	plr.sendMessage(":spin");
		process();
	}

	public void process() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;
	
		int[] common = { 19082, 19080, 19084, 21023, 21024, 21025, 21026, 21027, 21028, 10935, 18062 };
		int[] uncommon = {3495, 3505, 3506, 3507, 3508, 3509, 19040, 19041, 19042, 19043, 19044, 19046};
		int[] veryUncommon = {  19045, 19047, 10934, 18059, 3499, 3500, 3501, 3502, 3503, 3504, 3498, 3497, 21043,21047, 21045, 21044, 21042, 21041, 17656, 17662 };
		int[] rare = { 21055 ,21056 ,18689};
		int[] veryRare = {18063,13021};
		int[] extremelyRare = { 21056, 19085,18960 };
		int[] legendary = {10943,1413,6854,18957};
 //10$ 10942 
		int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
		/*int[] tier = all[Misc.random(6)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];*/
		
		int[] tier;


		Random r = new Random();
	   int random = r.nextInt(101); // Generates a random number between 0 and 99
	        if (random < 40) {
	        	tier = all[0]; // Common (40% probability)
	        } else if (random < 65) {
	        	tier = all[1]; // Uncommon (25% probability)
	        } else if (random < 75) {
	        	tier = all[2]; // Very Uncommon (10% probability)
	        } else if (random < 84) {
	        	tier = all[3]; // Rare (5% probability)
	        } else if (random < 91) {
	        	tier = all[4]; // Very Rare (5% probability)
	        } else if (random < 97) {
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
	
	public void rewardAll() {
		//plr.sendMessage("@red@Items will be add to your bank, If your inventory is full.");
	//	new Thread(() -> {  
	//	while(plr.getInventory().contains(BOX)) {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;
		
		int[] common = { 19082, 19080, 19084, 21023, 21024, 21025, 21026, 21027, 21028, 10935, 18062 };
		int[] uncommon = {3495, 3505, 3506, 3507, 3508, 3509, 19040, 19041, 19042, 19043, 19044, 19046};
		int[] veryUncommon = {  19045, 19047, 10934, 3499, 3500, 3501, 3502, 3503, 3504, 3498, 18059, 3497, 21043,21047, 21045, 21044, 21042, 21041, 17656, 17662 };
		int[] rare = { 21055 ,21056 ,18689};
		int[] veryRare = {18063,13021};
		int[] extremelyRare = { 21056, 19085,18960 };
		int[] legendary = {10943,1413,6854,18957 };
		
		int boxId = BOX;
		 int amount = plr.getInventory().getAmount(boxId);
	      //  StarterTasks.doProgress(plr, StarterTasks.StarterTask.OPEN_BOXES, amount);
	        Map<Integer, Integer> rewards = new HashMap<>();
	        for (int i = 0; i < amount; i++) {
	            int reward = -1;
	            int[] tier;
				Random r = new Random();
			   int chance = r.nextInt(101); // Generates a random number between 0 and 99
	          //  int chance = RandomUtility.inclusiveRandom(1, 100);
	            String name = ItemDefinition.forId(boxId).getName();
	            if (chance <= 40) {
	                // Common tier (40%)
	                reward = common[RandomUtility.exclusiveRandom(0, common.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	               // World.sendMessage(message);
	            } else if (chance <= 65) {
	                // Uncommon tier (25%)
	                reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	               // World.sendMessage(message);
	            } else if (chance <= 75) {
	                // Very Uncommon tier (10%)
	                reward = veryUncommon[RandomUtility.exclusiveRandom(0, veryUncommon.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	               // World.sendMessage(message);
	            } else if (chance <= 84) {
	                // Rare tier (9%)
	                reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	              //  World.sendMessage(message);
	            } else if (chance <= 91) {
	                // Very Rare tier (7%)
	                reward = veryRare[RandomUtility.exclusiveRandom(0, veryRare.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	                World.sendMessage(message);
	            } else if (chance <= 97) {
	                // Extremely Rare tier (6%)
	                reward = extremelyRare[RandomUtility.exclusiveRandom(0, extremelyRare.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	                World.sendMessage(message);
	            } else {
	                // Legendary tier (3%)
	                reward = legendary[RandomUtility.exclusiveRandom(0, legendary.length)];
	                String message = "<img=9> <shad=1>@cya@@whi@" + plr.getUsername() + " @cya@has received @whi@"
	                        + ItemDefinition.forId(reward).getName() + "@cya@ from a @whi@" + name;
	                World.sendMessage(message);
	            }

	            rewards.merge(reward, 1, Integer::sum);

	        }
	        plr.getInventory().delete(boxId, amount);
	     //   plr.getProgressionManager().getProgressionTask(13).incrementProgress(0, amount);
	    //    plr.getDailyTaskManager().submitProgressToIdentifier(5, amount);
	     //   plr.getProgressionManager().getProgressionTask(16).incrementProgress(0, amount);
	        boolean bank = amount <= plr.getInventory().getFreeSlots();
	        rewards.forEach((key, value) -> {
	            plr.getCollectionLogManager().addItem(boxId, new Item(key, value), value);
	            if (bank) {
	               plr.addItemToAny(key, value);
	            } else {
	                Item item = new Item(key, value);
	                plr.addItemToAny(key, value);
	            }
	        });
	        plr.getInventory().refreshItems();

	        if (!bank) {
	            plr.sendMessage("<img=9> <shad=1>@blu@Your rewards have been added to your bank.");
	        }
	/*	int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
	
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

		plr.getInventory().delete(BOX);
		reward();
	 	//plr.addItemToAny(PRIZE_ID, 1);
		}
		}).start();*/
		mysteryPrize = -1;
		canMysteryBox = true;
	}
	
	public void reward() {
		if (mysteryPrize == -1) {
			plr.sendMessage(null);
			return;
		}
		plr.getCollectionLogManager().addItem(BOX, new Item(mysteryPrize,1));
		plr.addItemToAny(mysteryPrize,1);

		plr.diamondBoxx = false;
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
		if (name.substring(name.length() - 1).equals("s")) {
			plr.sendMessage("Congratulations, you have won " + tier + name + "@bla@!");
		}
		else {
			plr.sendMessage("Congratulations, you have won a " + tier + name + "@bla@!");
		}
		
		plr.getPA().sendInterfaceRemoval();
		
		// Can now spin again
		canMysteryBox = true;
	}

	public void openInterface() {
		plr.sendMessage(":resetBox");
		spinNum = 0;
		plr.diamondBoxx = true;
		resetInterface(plr);
		plr.getPA().sendInterface(INTERFACE_ID);
	}

}
