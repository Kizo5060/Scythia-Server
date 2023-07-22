package com.ruseps.net.packet.impl;

import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.World;
import com.ruseps.world.content.gambling.GamblingManager;
import com.ruseps.world.entity.impl.player.Player;

public class GambleInvitePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		System.out.println("Player: "+ player + " Packet: "+ packet);
		player.getSkillManager().stopSkilling();
		int index = packet.readShort() & 0xFF;
		if (index > World.getPlayers().capacity()) {
			return;
		}
		Player otherPlayer = World.getPlayers().get(index);
		if (otherPlayer == null) {
			return;
		}
		if (otherPlayer.getIndex() != player.getIndex()) {
			GamblingManager.acceptInvitation(player, otherPlayer);
		}
	}
}
