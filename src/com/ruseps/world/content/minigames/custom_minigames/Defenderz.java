package com.ruseps.world.content.minigames.custom_minigames;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
/** @AUTHOR MAC DADDY **/
public class Defenderz {

	private static final int[] DEFENDERZ = new int[] {2822, 2823, 2824, 2825, 2826, 2827, 2828, 2829, 2830, 2831};
	
	public static int getDefender(Player player)
	{
		int foundIndex = -1;
		for(int i = 0; i < DEFENDERZ.length; i++) {
			if(player.getInventory().contains(DEFENDERZ[i]) || player.getEquipment().contains(DEFENDERZ[i]))
			{
				foundIndex = i;
			}
		}		
		if(foundIndex != 9) {
			foundIndex++;
		}
		return DEFENDERZ[foundIndex];
	}
	
	public static void handleDrop(Player player, NPC npc) {
			if(Misc.getRandom(150) == 1) {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(getDefender(player)), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
		}
	}
}
