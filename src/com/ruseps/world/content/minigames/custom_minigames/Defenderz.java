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
	    if (Misc.getRandom(150) == 1) {
	        int defenderId = getDefender(player);
	        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(defenderId), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
	        switch (defenderId) {
	            case 2822:
	                player.sendMessage("A @red@T1 Defenderz has been dropped!");
	                break;
	            case 2823:
	                player.sendMessage("An @red@T2 Defenderz has been dropped!");
	                break;
	            case 2824:
	                player.sendMessage("A @red@T3 Defenderz has been dropped!");
	                break;
	            case 2825:
	                player.sendMessage("A @red@T4 Defenderz has been dropped!");
	                break;
	            case 2826:
	                player.sendMessage("A @red@T5 Defenderz has been dropped!");
	                break;
	            case 2827:
	                player.sendMessage("An @red@T6 Defenderz has been dropped!");
	                break;
	            case 2828:
	                player.sendMessage("A @red@T7 Defenderz has been dropped!");
	                break;
	            case 2829:
	                player.sendMessage("A @red@T8 Defenderz has been dropped!");
	                break;
	            case 2830:
	                player.sendMessage("A @red@T9 Defenderz has been dropped!");
	                break;
	            case 2831:
	                player.sendMessage("The @red@Final Defenderz has been dropped!");
	                break;
	            default:
	                break;
	        }
	    }
	}
	}	    
	    
