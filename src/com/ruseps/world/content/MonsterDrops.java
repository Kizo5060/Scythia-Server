package com.ruseps.world.content;

import java.util.ArrayList;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.DropChance;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class MonsterDrops {
	/*
	 * @Author Levi Patton
	 * @rune-server.org/members/AuguryPS
	 */

	public static void sendNpcDrop(Player player, int id, String name) {
		NPCDrops drops = NPCDrops.forId(NpcDefinition.forName(name).getId());
		if (drops == null) {
			player.sendMessage("No drop table found for " + name);
			return;
		}
		for (int i = 29081; i < 29091; i++) {
			player.getPA().sendFrame126(i, "");
		}

		int line = 29081;

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}

			final DropChance dropChance = drops.getDropList()[i].getChance();

			if (dropChance == DropChance.ALWAYS) {
				player.getPA().sendFrame126(line,
						ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"
								+ Misc.format(drops.getDropList()[i].getItem().getAmount()));
				line++;
			}
		}
		if (line == 29081) {
			player.getPA().sendFrame126(28901 + 4, "\\n\\n\\n\\n\\nNo 100% drops");
		}
		line++;

		ArrayList<String> added = new ArrayList<String>();
		line = 28922;
		for (int i = 28922; i < 28962; i++) {
			player.getPA().sendFrame126(i, "");
		}

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance.ordinal() > DropChance.ALWAYS.ordinal()
					&& dropChance.ordinal() <= DropChance.NOTTHATRARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line, itemName + " x"
								+ Misc.format(drops.getDropList()[i].getItem().getAmount()));;
					line++;
				}
			}
		}
		player.getPA().sendFrame126(28901 + 5, "Regular Drops: " + added.size());
		added.clear();

		for (int i = 29002; i < 29042; i++) {
			player.getPA().sendFrame126(i, "");
		}
		line = 29002;
		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance.ordinal() >= DropChance.RARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line, itemName + " x"
								+ Misc.format(drops.getDropList()[i].getItem().getAmount()));;
					line++;
				}
			}
		}
		player.getPA().sendFrame126(28901 + 6, "Rare Drops: " + added.size());

		player.getPA().sendFrame126(28901 + 7, "");
		player.getPA().sendFrame126(28901 + 3, Misc.formatPlayerName(name));
		player.getPA().sendInterface(28901);
	}
}
