package com.ruseps.world.content;

import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;

/*
 * @author Aj
 * www.Simplicity-ps.com
 */

public class SecurityYeller {
	
	
    private static final int TIME = 1454300; //20 minutes - 1454300
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"<col=1aa3ff><shad=1aa3ff>[SECURITY]@bla@ Use ::2fa to fully secure your account from hackings!"},
			{"<col=1aa3ff><shad=1aa3ff>[SECURITY]@bla@ It is always a good idea to have a bank pin! Set one now with the town crier!"},
			{"<col=1aa3ff><shad=1aa3ff>[SECURITY]@bla@ Use ::2fa to fully secure your account from hackings!"},
			{"<col=1aa3ff><shad=1aa3ff>[SECURITY]@bla@ Never use the same password from an old server!"},

		
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
