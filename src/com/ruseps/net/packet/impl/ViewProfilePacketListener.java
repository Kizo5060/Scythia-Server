package com.ruseps.net.packet.impl;

import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.World;
import com.ruseps.world.content.ProfileViewing;
import com.ruseps.world.entity.impl.player.Player;

public class ViewProfilePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0) {
			return;
		}
		Player other = World.getPlayerByIndex(index);
		if (other == null) {
			return;
		}
		if (other.getPosition().getDistance(player.getPosition()) > 5) {
			player.sendMessage("The other player is too far away from you.");
			return;
		}
		ProfileViewing.view(player, other);
	}
}
