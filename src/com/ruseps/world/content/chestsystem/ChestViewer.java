package com.ruseps.world.content.chestsystem;

import java.util.HashMap;
import java.util.Map;

import com.ruseps.model.Item;
import com.ruseps.model.container.ItemContainer;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class ChestViewer {

	private Player player;

	public ChestViewer(Player player) {
		this.player = player;
	} 

	public void display(int boxId, String name, int[] common, int[] uncommon, int[] rare) {
		player.getPacketSender().sendInterface(48030);
		updateInterface(boxId, name, common, uncommon, rare);
	}

	private void updateInterface(int boxId, String name, int[] common, int[] uncommon, int[] rare) {
		resetItems(player, common, uncommon, rare);
		player.getPacketSender().sendString(48035, name);
		player.getPacketSender().sendItemOnInterface(48045, boxId, 0, 1);

		for (int i = 0; i < common.length; i++) {
			player.getPacketSender().sendItemOnInterface(48051, common[i], i, 1);
		}

		for (int i = 0; i < uncommon.length; i++) {
			player.getPacketSender().sendItemOnInterface(48061, uncommon[i], i, 1);
		}

		for (int i = 0; i < rare.length; i++) {
			player.getPacketSender().sendItemOnInterface(48071, rare[i], i, 1);
		}

	}
	
	public static void resetItems(Player player, int[] common, int[] uncommon, int[] rare) {
		int int1 = 48051;
		int int2 = 48061;
		int int3 = 48071;
		
		for (int i = 0; i < common.length; i++) {
			player.getPacketSender().sendItemOnInterface(int1, 0, 0);
		}

		for (int i = 0; i < uncommon.length; i++) {
			player.getPacketSender().sendItemOnInterface(int2, 0, 0);
		}

		for (int i = 0; i < rare.length; i++) {
			player.getPacketSender().sendItemOnInterface(int3, 0, 0);
		}
	}

	public void open(int boxId, int[] common, int[] uncommon, int[] rare) {
		int reward = -1;
		int chance = RandomUtility.inclusiveRandom(0, 1000);
	   //player.sendMessage("chance: " + Integer.toString(chance));
 		/*if (chance > 90) {
			reward = common[RandomUtility.exclusiveRandom(0, common.length)];
		} else if (chance > 35) {
			reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
		} else if (chance > 0) {
			reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
		}*/
		if(chance > 985 && chance <= 1000) {
	       	reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
	     	World.sendMessage("<shad=1>@cya@" + player.getUsername() +"<shad=1>@whi@ Recieved A @cya@" + ItemDefinition.forId(reward).getName()+ "@whi@ From Opening @cya@"+ ItemDefinition.forId(boxId).getName() + "@whi@!");
	       }
	       else 
	       	if(chance > 650 && chance <= 985) {
	       		reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
	       	}
	       	else 
        		reward = common[RandomUtility.exclusiveRandom(0, common.length)];
		
		player.getCollectionLogManager().addItem(boxId, new Item(reward));

		player.getInventory().delete(boxId);
		player.addItemToAny(reward, 1);
	}

	public void openAll(int boxId, int[] common, int[] uncommon, int[] rare) {
	    int amount = player.getInventory().getAmount(boxId);
	    Map<Integer, Integer> rewards = new HashMap<>();
	    for (int i = 0; i < amount; i++) {
	        int reward = -1;
	        int chance = RandomUtility.inclusiveRandom(1, 1000); // Using a larger range for precision
	       // player.sendMessage("chance: " + Integer.toString(chance));
	       /* if (chance <= 650) { // Common rewards (65% chance)
	            reward = common[RandomUtility.exclusiveRandom(0, common.length)];
	        } else if (chance > 650 && chance <= 985) { // Uncommon rewards (35% chance)
	            reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
	        } else if (chance > 985 && chance <= 1000) { // Rare rewards (1% chance)
	            reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
	        }*/
	        
	        if(chance > 985 && chance <= 1000) {
		       	reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
		     	World.sendMessage("<shad=1>@cya@" + player.getUsername() +"<shad=1>@whi@ Recieved A @cya@" + ItemDefinition.forId(reward).getName()+ "@whi@ From Opening @cya@"+ ItemDefinition.forId(boxId).getName()+"@whi@!");
		       }
		       else 
		       	if(chance > 650 && chance <= 985) {
		       		reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
		       	}
		       	else 
	        		reward = common[RandomUtility.exclusiveRandom(0, common.length)];
	        		

			rewards.merge(reward, 1, Integer::sum);

		}
		player.getInventory().delete(boxId, amount);
		boolean bank = amount <= player.getInventory().getFreeSlots();
		rewards.entrySet().forEach(r -> {
			player.getCollectionLogManager().addItem(boxId, new Item(r.getKey(),r.getValue()));
			if (bank) {
				player.getInventory().add(r.getKey(), r.getValue());
			} else {
				player.addItemToAny(r.getKey(), r.getValue());
			}
		});

	}

}
