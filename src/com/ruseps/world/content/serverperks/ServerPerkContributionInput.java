package com.ruseps.world.content.serverperks;

import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.model.input.Input;
public class ServerPerkContributionInput extends Input{
	
	@Override
    public void handleAmount(Player player, int amount) {
        ServerPerks.getInstance().contribute(player, amount);
    }
}
