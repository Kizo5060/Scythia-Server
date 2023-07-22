package com.ruseps.world.content.battle_royale.loot;

import com.ruseps.model.GameObject;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class MeleeBattleRoyaleChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if(!p.getClickDelay().elapsed(2000)) 
			return;
	
			Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
				for(Item item : loot) {
					p.getInventory().add(item);
			}
					p.getPacketSender().sendMessage("<img=10>@blu@You open the Melee Battle Royale Chest.");
					p.getInventory().add(391, 1);
	}

	private static final Item[][] itemRewards =  {
			{new Item(19022, 1)},
			{new Item(19023, 1)},
			{new Item(19024, 1)}, 
			{new Item(19017, 1)}, 
			{new Item(19019, 1)}, 
			{new Item(19018, 1)}, 
			{new Item(19020, 1)}, 
			{new Item(14484, 1)}, 
			{new Item(4151, 1)}, 
			{new Item(7084, 1)}, 
			{new Item(19025, 1)}, 
			{new Item(19914, 1)}, 
			{new Item(19913, 1)}, 
			{new Item(19026, 1)}, 
			{new Item(19027, 1)},
			{new Item(19028, 1)},
			{new Item(19029, 1)}, 
		};
}
