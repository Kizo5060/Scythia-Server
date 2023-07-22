package com.ruseps.world.content.DiscordBots;

import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;

public class FAGMessageBot {
	
	
	
    private static final int TIME = 900000; //15 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = {
			{"Donate to help the server grow!"},
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			DiscordBot.jda.getTextChannelById("channel_id").sendMessage("```" + currentMessage + "```").queue();
				}
			}
		

          }
	
	

}
