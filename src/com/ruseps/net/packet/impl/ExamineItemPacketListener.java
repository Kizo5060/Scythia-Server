package com.ruseps.net.packet.impl;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class ExamineItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readShort();
		if(item == 995 || item == 19994) {
			player.getPacketSender().sendMessage("Mhmm... Shining coins...");
			return;
		}
		if (item == 19085) {
			ItemDefinition itemDef = ItemDefinition.forId(item);
			if (itemDef != null) {
				player.getPacketSender().sendMessage("@gre@<shad=0>You currently have "
						+ Misc.format(player.getUltBowCharges()) + " bow tokens stored.");
			}
		}
		ItemDefinition itemDef = ItemDefinition.forId(item);
		if(itemDef != null) {
			if(player.getRights()== PlayerRights.OWNER||player.getRights()== PlayerRights.DEVELOPER ) {
				player.getPacketSender().sendMessage("@gre@<shad=0>Item ID: " + itemDef.getId());
			}
			player.getPacketSender().sendMessage(itemDef.getDescription());
			for (Skill skill : Skill.values()) {
				if (itemDef.getRequirement()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
					player.getPacketSender().sendMessage("@red@WARNING: You need " + new StringBuilder().append(skill.getName().startsWith("a") || skill.getName().startsWith("e") || skill.getName().startsWith("i") || skill.getName().startsWith("o") || skill.getName().startsWith("u") ? "an " : "a ").toString() + Misc.formatText(skill.getName()) + " level of at least " + itemDef.getRequirement()[skill.ordinal()] + " to wear this.");
				}
			}
		}
	}

}