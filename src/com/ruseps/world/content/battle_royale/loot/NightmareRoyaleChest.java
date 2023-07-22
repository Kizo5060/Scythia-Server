package com.ruseps.world.content.battle_royale.loot;

import com.ruseps.model.GameObject;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class NightmareRoyaleChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if(!p.getClickDelay().elapsed(2000)) 
			return;
	
			Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
				for(Item item : loot) {
					p.getInventory().add(item);
			}
					p.getPacketSender().sendMessage("<img=10>@blu@You open the Mage Battle Royale Chest.");
					p.getInventory().add(391, 1);
	}
//nightmare = mage 
	private static final Item[][] itemRewards =  {
			{new Item(10996, 1)},
			{new Item(10998, 1)},
			{new Item(928, 1)}, 
			{new Item(10999, 1)}, 
			{new Item(926, 1)}, 
			{new Item(929, 1)}, 
			{new Item(925, 1)}, 
			{new Item(14484, 1)}, 
			{new Item(930, 1)}, 
			{new Item(7084, 1)}, 
			{new Item(935, 1)}, 
			{new Item(932, 1)}, 
			{new Item(936, 1)}, 
			{new Item(934, 1)}, 
			{new Item(931, 1)}, 
			{new Item(933, 1)},
			{new Item(923, 1)},
			{new Item(920, 1)},
			{new Item(922, 1)},
			{new Item(921, 1)},
			{new Item(924, 1)},
			{new Item(919, 1)},
		};
}
