package com.ruseps.net.packet.impl;

import com.ruseps.model.ChatMessage.Message;
import com.ruseps.model.Flag;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.util.Misc;
import com.ruseps.world.content.PlayerPunishments;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		if(player.isHidePlayer()) {
			return;
		}
		if(PlayerPunishments.isMuted(player.getUsername()) || PlayerPunishments.isIpMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		/*if (Misc.getMinutesPlayed(player) <= 7) {
			player.getPacketSender().sendMessage("You must have played for 7 minutes to chat. -temporary");
			return;*/
		{
			
		}
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
