package com.ruseps.world.content.skill.impl.crafting;

import com.ruseps.model.Item;
import com.ruseps.model.Skill;
import com.ruseps.world.content.skill.impl.fletching.ArrowData;
import com.ruseps.world.content.skill.impl.fletching.BoltData;
import com.ruseps.world.entity.impl.player.Player;

public class BattleStaffs {
	
	
	public static void makeBattleStaff(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		BattleStaffData staff = BattleStaffData.forStaff(item2);
		if(staff != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) >= staff.getLevelReq()) {
			player.getInventory().delete(new Item(staff.getItem1()).setAmount(1), player.getInventory().getSlot(staff.getItem1()), true);
			player.getInventory().delete(new Item(staff.getItem2()).setAmount(1), player.getInventory().getSlot(staff.getItem2()), true);
			player.getInventory().add(staff.getOutcome(), 1);
			player.getSkillManager().addExperience(Skill.CRAFTING, (int) (staff.getXp() * 2.5));

		} else {
			player.getPacketSender().sendMessage("You need a Crafting level of at least "+staff.getLevelReq()+" to craft this.");
		}

	}
	
	}
}
