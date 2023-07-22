package com.ruseps.world.content;

import com.google.common.collect.Range;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Item;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Handles the action of gambling an item through the gambling NPC.
 * 
 * @author Andys1814.
 */
public class GamblingAction {

	/**
	 * A constant range that represents the roll-able dice range for this
	 * gambling action.
	 */
	private static final Range<Integer> DICE_RANGE = Range.closed(1, 100);

	/**
	 * Handles the action of gambling an item. This method will psuedo-randomly
	 * select a value from {@link #DICE_RANGE}'s range and determine the
	 * success/failure of the gamble and function accordingly.
	 * 
	 * @param player
	 *            The {@link Player} that gambled the item.
	 * @param item
	 *            The item that the {@link Player} gambled.
	 */
	
	public static void handleGambleItem(Player player, Item item) {
		int itemId = item.getId();
		int itemAmount = item.getAmount();
		player.getPacketSender().sendMessage("Disabled. Click the npc to dice cash only!");
		
	}

}
