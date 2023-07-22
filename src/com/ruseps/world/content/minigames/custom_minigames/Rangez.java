package com.ruseps.world.content.minigames.custom_minigames;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
/** @AUTHOR MAC DADDY **/
public class Rangez {

	private static final int[] RANGE = new int[] {2681, 2682, 2683, 2684, 2685, 2686, 2687, 2688, 2689, 2690};
	
	public static int getRangez(Player player)
	{
		int foundIndex = -1;
		for(int i = 0; i < RANGE.length; i++) {
			if(player.getInventory().contains(RANGE[i]) || player.getEquipment().contains(RANGE[i]))
			{
				foundIndex = i;
			}
		}		
		if(foundIndex != 9) {
			foundIndex++;
		}
		return RANGE[foundIndex];
	}
	
	public static void handleDrop(Player player, NPC npc) {
			if(Misc.getRandom(150) == 1) {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(getRangez(player)), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
		}
	}
}
