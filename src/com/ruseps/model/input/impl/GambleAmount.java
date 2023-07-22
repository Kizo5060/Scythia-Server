package com.ruseps.model.input.impl;

import com.google.common.collect.Range;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.input.EnterAmount;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.entity.impl.player.Player;

public class GambleAmount extends EnterAmount {

	private static final Range<Integer> DICE_RANGE = Range.closed(1, 100);
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > 1000000000) {
			player.getPacketSender().sendMessage("You can not gamble over 1b of any item");
			return;
		}
			
		player.getPacketSender().sendInterfaceRemoval();
		int cost = amount;
		
		if(player.getInventory().getAmount(19992) < cost) {
			player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to gamble that amount.");
			return;
		}
		PlayerLogs.log(player.getUsername(), "Player gambled "  +amount + "Coins");
		player.getPacketSender().sendMessage("Rolling...");
		player.performAnimation(new Animation(11900));
		player.performGraphic(new Graphic(2075));
		player.getInventory().delete(19992, amount);
		
		int roll = Misc.getRandom(100);

		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (roll >= 55) {
					player.forceChat("I Rolled A " + roll + " And Have Won!");
					player.getInventory().add(new Item(19992, amount * 2));
				} else {
					player.forceChat("I Rolled A " + roll + " And Have Lost!");
				}
				this.stop();
			}
		});
	
	}
}
