package com.ruseps.world.content.combat.range;

import com.ruseps.util.Misc;
import com.ruseps.world.content.ItemDegrading;
import com.ruseps.world.entity.impl.player.Player;

public class UltimateWingedBow {

	public static boolean loadUltBow(Player player) {
		int max = ItemDegrading.maxUltyBowCharges, charging_token = 12934, ult_bow = 19085,
				invcount = player.getInventory().getAmount(charging_token), storecount = player.getUltBowCharges();

		if (!player.getInventory().contains(ult_bow) || !player.getInventory().contains(charging_token)) {
			return false;
		}

		if (storecount >= max) {
			player.getPacketSender().sendMessage("Your bow can not hold any more tokens.");
			return false;
		}

		if ((invcount + storecount) > max) {
			int toremove = max - storecount;
			player.getInventory().delete(charging_token, toremove);
			player.setUltBowCharges(max);
			player.getPacketSender().sendMessage("You use " + Misc.format(toremove) + " tokens to fill your bow.");
			return true;
		}

		if ((invcount + storecount) < max) {
			player.getInventory().delete(charging_token, invcount);
			player.setUltBowCharges(invcount + storecount);
			player.getPacketSender().sendMessage("You add " + Misc.format(invcount) + " and now have "
					+ Misc.format(player.getUltBowCharges()) + " total bow charges.");
			return true;
		}

		return true;
	}
}
