package com.ruseps.world.content;

import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;

public class DonationBoss {

    private static final int TIME = 1200000; //20 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
    
    

	/*
	 * Sequence called in world.java 
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(World.donated >= 50){
			NPC donatorBoss = new NPC(704, new Position(3424,4010,4));
			donatorBoss.setShouldRespawn(false);
			World.register(donatorBoss);
			World.sendMessage("<img=6><shad=1>@red@  Thanks For The Donations!, Donator Boss Has Spawned!");
			World.sendMessage("<img=6> <shad=1>@bla@ Use ::db To Teleport To The Donator Boss!");
			World.donated -= 50;
		}else {
			
		if(timer.elapsed(TIME)) {
			timer.reset();
			currentMessage = "<shad=1>@red@ We currently have @blu@" + World.donated +"/50$@red@ donated for the Donator Boss spawn!";
			World.sendMessage(currentMessage);
			
          }
	 }
	}
}
