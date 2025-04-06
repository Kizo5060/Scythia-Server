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
        if (Misc.getRandom(150) == 1) {
            int itemId = getWeaponz(player);
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(itemId), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
            switch (itemId) {
                case 2833:
                    player.sendMessage("A @red@T1 Weaponz has been dropped!");
                    break;
                case 2835:
                    player.sendMessage("A @red@T2 Weaponz has been dropped!");
                    break;
                case 2837:
                    player.sendMessage("A @red@T3 Weaponz has been dropped!");
                    break;
                case 2839:
                    player.sendMessage("A @red@T4 Weaponz has been dropped!");
                    break;
                case 2841:
                    player.sendMessage("A @red@T5 Weaponz has been dropped!");
                    break;
                case 2842:
                    player.sendMessage("A @red@T6 Weaponz has been dropped!");
                    break;
                case 2843:
                    player.sendMessage("A @red@T7 Weaponz has been dropped!");
                    break;
                case 2844:
                    player.sendMessage("A @red@T8 Weaponz has been dropped!");
                    break;
                case 2847:
                    player.sendMessage("A @red@T9 Weaponz has been dropped!");
                    break;
                case 2846:
                    player.sendMessage("The @red@Final Weaponz has been dropped!");
                    break;
                default:
                    break;
            }
        }
    }
}