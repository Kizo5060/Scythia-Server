package com.ruseps.world.content.well_of_voting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

//import com.ruseps.tools.discord.DiscordConstant;
//import com.ruseps.tools.discord.DiscordManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.well_of_voting.global_vote_bosses.VoteBossOne;
import com.ruseps.world.entity.impl.player.Player;

public class WellofVoting {

	private static final int AMOUNT_NEEDED = 20; // 20 VOTES
	private static final int LEAST_DONATE_AMOUNT_ACCEPTED = 1; // 1 VOTE

	private static CopyOnWriteArrayList<Player> VOTING_DONATORS = new CopyOnWriteArrayList<Player>();
	private static int VOTES_IN_WELL = 0;

	public static void donate(Player player, int amount) {
		if (VOTES_IN_WELL >= AMOUNT_NEEDED) {
			player.getPA().sendMessage("well is already full.");
			return;
		}
		if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
			DialogueManager.sendStatement(player, "You must donate at least 1 billbag.");
			return;
		}
		if (amount > getMissingAmount()) {
			amount = getMissingAmount();
		}
		boolean usePoints = player.getPointsHandler().getVotingPoints() >= amount;
		if (player.getPointsHandler().getVotingPoints() < amount) {
			DialogueManager.sendStatement(player, "You don't even have 1 Voting Point?");
			return;
		}
		
		player.getPointsHandler().decrementVotingPoints(1);
		
		if (VOTING_DONATORS.contains(player)) {
			VOTING_DONATORS.add(player);
		}
		VOTES_IN_WELL += amount;
		amount = player.getPointsHandler().getVotingPoints();
		if (amount > 5) {
			DialogueManager.sendStatement(player, "We need a total of 20 votes to spawn the Boss!");
			World.sendMessage("<img=5> <col=6666FF>" + player.getUsername() + " has donated "
					+ Misc.insertCommasToNumber("" + amount + "") + " Votes to the Well of Voting Bosses!");
		}
		DialogueManager.sendStatement(player, "Thank you for your Voting Donation");
		if (getMissingAmount() <= 0) {
			World.sendMessage("<img=5> <col=6666FF>The Voting World Boss has initiated, thanks for filling it up!");
			VoteBossOne.sequence();
			setDefaults();
		}
		player.getPA().sendString(16566, player.getUsername() + "(" + Misc.formatRunescapeStyle(amount) + ")");
		WellofVotingInterface.donators.add(player.getUsername());
	}

	public static void setDefaults() {
		VOTING_DONATORS.clear();
		VOTES_IN_WELL = 0;
	}

	public static int getMissingAmount() {
		return (AMOUNT_NEEDED - VOTES_IN_WELL);
	}
}
