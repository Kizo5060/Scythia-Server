package com.ruseps.world.content.DiscordBots;

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

	public void initialize() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setStatus(OnlineStatus.ONLINE);

		
		builder.setToken("75c2b189d1b6915124217a3b75773bc191cb7fb08817d8222f73e583aa05834a");
		//builder.setGame(Game.of(GameType.STREAMING, "NexArch", "https://discordapp.com/api/webhooks/711317393841979411/dQAQcY4ebd6zK2sG1wxiwg8iiC98bG2LNX_YgxmdWOz3XsaMsVVL5EDD_muSFqggOauR"));
		builder.addEventListener(this);
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	private final String DO_COMMAND = "::";


	public void sendMessage(String message){
	}


	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String message = event.getMessage().getContentDisplay();

		String[] command = message.split(" ");

		if (command[0].equalsIgnoreCase(DO_COMMAND + "players")) {
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("NexArch");
			embed.setColor(Color.DARK_GRAY);

			embed.setDescription("");
			embed.addField("There are currently " + World.getPlayers().size() + " online!", "", false);
			event.getChannel().sendMessage(embed.build()).queue();
		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "vote")) {

		
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Voting!");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField("Do ::vote in-game", "https://nexarch.teamgames.io/services/vote", false);
			embed.addField("Or head too LINK_HERE to vote", "https://nexarch.teamgames.io/services/vote", false);
			embed.addField("Vote on all the sites", "", false);
			embed.addField("Do ::reward 1 all", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "store")) {

			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("NexArch Store");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField("Do ::donate in-game", "https://nexarch.teamgames.io/services/store", false);
			embed.addField("Or head too LINK_HERE", "", false);
			embed.addField("Purchase whatever you would like", "", false);
			embed.addField("Do ::claim in-game!", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}

		if (command[0].equalsIgnoreCase(DO_COMMAND + "website")) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("NexArch Website");
			embed.setColor(Color.DARK_GRAY);
			embed.setDescription("");
			embed.addField(event.getMember().getAsMention() + " http://server.com", "", false);
			event.getChannel().sendMessage(embed.build()).queue();

		}
		
		if (command[0].equalsIgnoreCase(DO_COMMAND + "help")) {
			System.out.println("Command executed");
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(" Commands");
			embed.setColor(Color.RED);
			embed.setDescription("");
			embed.addField("::store - links you to our donation store.", "", false);
			embed.addField("::players - tells you how many players are in-game.", "", false);
			embed.addField("::vote - links you to our voting page", "", false);
			embed.addField("::website - links you to our website", "", false);
			embed.addField("::highscore - links you to our highscores", "", false);
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
			JDA builder = new JDABuilder(AccountType.BOT).setToken("NDc5MDU5MDUyNDE2OTkxMjQ0.Xqusqw.7F0xLow_NgOode-LC7ohocMRq_g").build();
			 builder.addEventListener(this);
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
