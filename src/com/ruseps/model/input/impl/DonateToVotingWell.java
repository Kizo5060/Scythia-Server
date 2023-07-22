package com.ruseps.model.input.impl;

import com.ruseps.model.input.EnterAmount;
import com.ruseps.world.content.well_of_voting.WellofVoting;
import com.ruseps.world.entity.impl.player.Player;

public class DonateToVotingWell extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		new WellofVoting();
		WellofVoting.donate(player, amount);
	}
}
