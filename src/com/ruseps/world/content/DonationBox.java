package com.ruseps.world.content;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class DonationBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {997, 19908, 15332, 18992, 18994, 18998, 19000, 19980, 18944, 18444, 18912};
	public static final int [] goodRewards = {20100, 20102, 20104, 11896, 10502, 
			11147, 11148, 11149, 7081, 11659, 937, 8518};	
	
	/*
	 * Handles the opening of the donation box
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
			//fk
		player.getInventory().delete(6183, 1);
		giveReward(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
		
	}
	
	/*
	 * Gives the reward base on misc Random chance
	 */
	public static void giveReward(Player player) {
		/*
		 * 1/3 Chance for a good reward
		 */
		if (RandomUtility.RANDOM.nextInt(3) == 2) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
		/*
		 * Adds 5m + a random amount up to 100m every box
		 * Max cash reward = 105m
		 */
		player.getInventory().add(19994, 10 + RandomUtility.RANDOM.nextInt(30));
	}

}
