package com.ruseps.world.content.minigames.custom_minigames;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
/** @AUTHOR MAC DADDY **/
public class Weaponz {

	private static final int[] WEAPONZ = new int[] {2833, 2835, 2837, 2839, 2841, 2842, 2843, 2844, 2847, 2846 };
	
	public static int getWeaponz(Player player)
	{
		int foundIndex = -1;
		for(int i = 0; i < WEAPONZ.length; i++) {
			if(player.getInventory().contains(WEAPONZ[i]) || player.getEquipment().contains(WEAPONZ[i]))
			{
				foundIndex = i;
			}
		}		
		if(foundIndex != 9) {
			foundIndex++;
		}
		return WEAPONZ[foundIndex];
	}
	
	public static void handleDrop(Player player, NPC npc) {
			if(Misc.getRandom(150) == 1) {
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(getWeaponz(player)), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
		}
	}
}
