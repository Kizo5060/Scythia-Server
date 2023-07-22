package com.ruseps.world.content.well_of_voting;

import java.util.ArrayList;

import com.ruseps.model.input.impl.DonateToVotingWell;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class WellofVotingInterface {

	public WellofVotingInterface(Player player) {
		this.player = player;
	}

	private Player player;

	public static ArrayList<String> donators = new ArrayList<String>();

	public void open() {
		sendStrings();
		player.getPA().sendInterface(16550);
		player.getPA().sendMessage("Donate to active a global boss for everyone to have a go at!");
	}

	public void sendStrings() {
		player.getPA().sendFrame126("" + donators, 16562);
	}

	public void button(int id) {
		switch (id) {
		case 16553:
			player.setInputHandling(new DonateToVotingWell());
			player.getPacketSender().sendInterfaceRemoval()
					.sendEnterAmountPrompt("How much money would you like to contribute with?");
			break;
		}
	}
}
