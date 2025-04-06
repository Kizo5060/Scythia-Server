package com.ruseps.world.content.minigames.custom_minigames;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
/** @AUTHOR MAC DADDY **/
public class Magicz {

	private static final int[] MAGEZ = new int[] {2691, 2692, 2693, 2694, 2695, 2696, 2697, 2698, 2699, 2700};
	
	public static int getMagic(Player player)
	{
		int foundIndex = -1;
		for(int i = 0; i < MAGEZ.length; i++) {
			if(player.getInventory().contains(MAGEZ[i]) || player.getEquipment().contains(MAGEZ[i]))
			{
				foundIndex = i;
			}
		}		
		if(foundIndex != 9) {
			foundIndex++;
		}
		return MAGEZ[foundIndex];
	}
	
	public static void handleDrop(Player player, NPC npc) {
	    if (Misc.getRandom(150) == 1) {
	        int itemId = getMagic(player);
	        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(itemId), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
	        switch (itemId) {
	            case 2691:
	                player.sendMessage("A @red@T1 Magic Weapon has been dropped!");
	                break;
	            case 2692:
	                player.sendMessage("A @red@T2 Magic Weapon has been dropped!");
	                break;
	            case 2693:
	                player.sendMessage("A @red@T3 Magic Weapon has been dropped!");
	                break;
	            case 2694:
	                player.sendMessage("A @red@T4 Magic Weapon has been dropped!");
	                break;
	            case 2695:
	                player.sendMessage("A @red@T5 Magic Weapon has been dropped!");
	                break;
	            case 2696:
	                player.sendMessage("A @red@T6 Magic Weapon has been dropped!");
	                break;
	            case 2697:
	                player.sendMessage("A @red@T7 Magic Weapon has been dropped!");
	                break;
	            case 2698:
	                player.sendMessage("A @red@T8 Magic Weapon has been dropped!");
	                break;
	            case 2699:
	                player.sendMessage("A @red@T9 Magic Weapon has been dropped!");
	                break;
	            case 2700:
	                player.sendMessage("The @red@Final Magic Weapon has been dropped!");
	                break;
	            default:
	                break;
	        }
	    }
	}}
