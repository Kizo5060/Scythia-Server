package com.ruseps.net.packet.impl;

import com.ruseps.model.Locations.Location;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class DuelAcceptancePacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		System.out.println("hi");
		if (player.getConstitution() <= 0)
			return;
		player.getSkillManager().stopSkilling();
		int index = packet.getOpcode() == OPCODE ?  (packet.readShort() & 0xFF) : packet.readLEShort();
		if(index > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(index);
		if (target == null) 
			return;
		if(player.getLocation()!= Location.DUEL_ARENA) {
			player.getDicing().requestDice(target);
		} else {
		if(target.getIndex() != player.getIndex())
			player.getDueling().challengePlayer(target);
		}
	}
	
	public static final int OPCODE = 128;
}
