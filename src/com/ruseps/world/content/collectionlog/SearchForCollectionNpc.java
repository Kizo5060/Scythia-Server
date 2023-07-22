package com.ruseps.world.content.collectionlog;

import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class SearchForCollectionNpc extends Input 
{	
	public void handleSyntax(Player player, String msg)
	{
		player.getCollectionLog().search(player, msg);
	}
	
}
