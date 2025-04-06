package com.ruseps.world.content;

import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;

/*
 * @author Aj
 * www.Simplicity-ps.com
 */

public class Reminders {
	
	
    private static final int TIME = 1200000; //20 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Remember to ::vote for rewards every 12 hours!"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Use the command 'drops' for drop tables"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Use the ::help command to ask staff for help"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Remember to spread the word and invite your friends to play!"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Use ::commands to find a list of commands"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>See where you stand on the Hiscores"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Donate to help the server grow!"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Toggle your client settings to your preference in the wrench tab!"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Remeber to ::Vote To get Points to buy dope armour!"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Donators + can use ::title to set a custom loyalty title"},
			{"<img=2>@blu@[SERVER]@bla@<shad=1>Need Help?: Join the Help Channel"},

		
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
			World.sendMessage(currentMessage);
					
					
				}
				
			new Thread(() -> { World.savePlayers(); }).start();
			}
		

          }
  }
