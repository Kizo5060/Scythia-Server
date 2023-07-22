package com.ruseps.world.content.DiscordBot;

import com.ruseps.world.World;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;


public class DiscordBot extends ListenerAdapter {

	public static JDA jda;

	private final String DO_COMMAND = "::";


	public void sendMessage(String message) {
		
	}


	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String message = event.getMessage().getContentDisplay();

		String[] command = message.split(" ");

		if (command[0].equalsIgnoreCase(DO_COMMAND + "players")) {
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Scythia!");
			embed.setColor(Color.DARK_GRAY);

			embed.setDescription("");
			embed.addField("There are currently " + World.getPlayers().size() + " online!", "", false);
			event.getChannel().sendMessage(embed.build()).queue();
		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "vote")) {

		
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Scythia Voting!");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField("Do ::vote in-game", "", false);
			embed.addField("Or head too http://Scythiarsps.everythingrs.com/services/vote to vote", "", false);
			embed.addField("Vote on all the sites", "", false);
			embed.addField("Do ::reward 1 all", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "store")) {

			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Scythia Store");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField("Do ::donate in-game", "", false);
			embed.addField("Or head too http://Scythiarsps.everythingrs.com/services/store", "", false);
			embed.addField("Purchase whatever you would like", "", false);
			embed.addField("Do ::claim in-game!", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "website")) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Scythia Website");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField(event.getMember().getAsMention() + " http://server.com", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}
		
		if (command[0].equalsIgnoreCase(DO_COMMAND + "help")) {
			System.out.println("Command executed");
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Scythia's Commands");
			embed.setColor(Color.RED);
			embed.setDescription("");
			embed.addField("::store - links you to our donation store.", "", false);
			embed.addField("::players - tells you how many players are in-game.", "", false);
			embed.addField("::vote - links you to our voting page", "", false);
			embed.addField("::website - links you to our website", "", false);
			embed.addField("::osrs - displays information about donating RS Gold", "", false);

			event.getChannel().sendMessage(embed.build()).queue();

		}

		
		if (command[0].equalsIgnoreCase(DO_COMMAND + "osrs")) {
			System.out.println("Command executed");
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Donating RuneScape GP");
			embed.setColor(Color.RED);
			embed.setDescription("");
			embed.addField("Only THE OWNER will deal with RuneScape Donations.", "", false);
			embed.addField("We accept Oldschool gold at $0.70/m, $0.80/M if over 50M, and $1/M if over 100M", "", false);
		
			event.getChannel().sendMessage(embed.build()).queue();

		}
	}
	
	public void runBot() {
		try {
			JDA builder = new JDABuilder(AccountType.BOT).setToken("NzMzNTk2NzcyNDI4OTM5MzY1.XxFdSQ.lcUDG4M2p7IBkGfwiWoUsrHvwBU").build();
			 builder.addEventListener(this);
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
