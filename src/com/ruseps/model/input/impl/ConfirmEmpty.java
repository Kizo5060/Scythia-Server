package com.ruseps.model.input.impl;

import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

/*
 * @author ajw
 * www.simplicity-ps.com
 */

public class ConfirmEmpty extends Input {

	/*
	 * Handle the input of the yes/no answer to decide action
	 */
	@Override
	public void handleSyntax(Player player, String syntax) {
		
				
		if (syntax.contentEquals("yes")) {
			 player.getPacketSender().sendInterfaceRemoval();
		     player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
	         player.getSkillManager().stopSkilling();
	         player.getInventory().resetItems().refreshItems();
		} else {
			player.getPacketSender().sendInterfaceRemoval();
		     player.getPacketSender().sendInterfaceRemoval().sendMessage("You do not clear your inventory.");

		}
		
	}
}
