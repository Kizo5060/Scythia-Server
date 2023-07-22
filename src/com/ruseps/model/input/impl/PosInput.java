package com.ruseps.model.input.impl;

import java.sql.PreparedStatement;

import com.ruseps.GameSettings;
import com.ruseps.model.input.Input;
import com.ruseps.util.NameUtils;
import com.ruseps.world.entity.impl.player.Player;


public class PosInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendClientRightClickRemoval();
		player.getPlayerOwnedShopManager().updateFilter(syntax);
		
	}
}
