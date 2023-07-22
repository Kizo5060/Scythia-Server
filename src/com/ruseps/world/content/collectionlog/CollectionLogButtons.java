package com.ruseps.world.content.collectionlog;

import com.ruseps.world.entity.impl.player.Player;

public class CollectionLogButtons 
{
	public static boolean isCollectionLogButton(Player player, int buttonId) 
	{	
		for(CollectionLogNpcs npcs : CollectionLogNpcs.values())
		{
			if(npcs.getButtonId() == buttonId) 
			{
				player.getCollectionLog().selectNpc(player, npcs);
				return true;
			}
		}
            
		switch (buttonId) 
		{
		
		case 19870:
			player.getCollectionLog().openInterface(player);
			return true;
		case 30362:
			player.setInputHandling(new SearchForCollectionNpc());
			player.getPacketSender().sendEnterInputPrompt("Type the NPC name that you would like to search for");
			return true;
		}
		return false;
	}
}
