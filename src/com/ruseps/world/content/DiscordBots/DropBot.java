package com.ruseps.world.content.DiscordBots;

import com.neovisionaries.ws.client.WebSocketFactory;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessage;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

import java.awt.*;
import java.io.IOException;


public class DropBot extends ListenerAdapter {

	public static WebhookClient Client;
	public static WebSocketFactory test;
	public void initialize() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setStatus(OnlineStatus.ONLINE);
		WebSocketFactory test = new WebSocketFactory();
		WebhookClient client = new WebhookClientBuilder("https://discordapp.com/api/webhooks/668707198171480065/sYXIpFC0tpfy5t8z8H-E7D-Q1i-kbHAPWanJ0f6nvJFejE2jlazNHVuyWFqNxHDEpi_E").build();

		DropBot.test = new WebSocketFactory();
		DropBot.Client = client;
	}
	
	public static void sendMessage(DropAnnouncer announcer){

		WebhookMessageBuilder builder5 = new WebhookMessageBuilder();
		WebhookMessage message = builder5.build();
		DropBot.Client.send(message);

		try {
			DropBot.test.createSocket("https://discordapp.com/api/webhooks/668707198171480065/sYXIpFC0tpfy5t8z8H-E7D-Q1i-kbHAPWanJ0f6nvJFejE2jlazNHVuyWFqNxHDEpi_E");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}		public static void sendSuperior(Player player, NPC npc){

		WebhookMessageBuilder builder5 = new WebhookMessageBuilder();
		MessageEmbed firstEmbed = new EmbedBuilder().setColor(Color.MAGENTA).setTitle("Rare Drop").setTitle(":mega: "+player.getUsername()+" has encountered a "+npc.getDefinition().getName()+"!").build();

		builder5.addEmbeds(firstEmbed)
				.setUsername("Viah-Ps Town Crier").setAvatarUrl("https://runesuite.org/uploads/monthly_2019_09/rune_full_helm_icon_by_rs_legendarts-dby332b.png.90b85639e5e01905ada3045c37f3f12d.png");
		WebhookMessage message = builder5.build();
		DropBot.Client.send(message);


		try {
			DropBot.test.createSocket("https://discordapp.com/api/webhooks/668707198171480065/sYXIpFC0tpfy5t8z8H-E7D-Q1i-kbHAPWanJ0f6nvJFejE2jlazNHVuyWFqNxHDEpi_E");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void sendMessageOnline(){

		WebhookMessageBuilder builder5 = new WebhookMessageBuilder();
		MessageEmbed firstEmbed = new EmbedBuilder().setColor(Color.MAGENTA).setTitle(":mega: NexArch has been Updated and is back online!").build();

		builder5.addEmbeds(firstEmbed)
				.setUsername("Scythia Town Crier").setAvatarUrl("https://runesuite.org/uploads/monthly_2019_09/rune_full_helm_icon_by_rs_legendarts-dby332b.png.90b85639e5e01905ada3045c37f3f12d.png");
		WebhookMessage message = builder5.build();
		DropBot.Client.send(message);


		try {
			DropBot.test.createSocket("https://discordapp.com/api/webhooks/668707198171480065/sYXIpFC0tpfy5t8z8H-E7D-Q1i-kbHAPWanJ0f6nvJFejE2jlazNHVuyWFqNxHDEpi_E");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}