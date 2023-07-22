package com.ruseps.world.content.new_raids_system.instances;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.raids_system.RaidsConstants;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class AnimeRaids 
{
	public static boolean isPhase2 = false;
	public static boolean isPhase3 = false;
	
    public static void startRaidsTwo(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	p.sendMessage("<img=10>@blu@Phase One has begun!!");
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Anime Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Anime Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Anime Raid.");
            return;
        }
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(2973, 2844 + Misc.getRandom(3), height));
            member.getPacketSender().sendInteractionOption("null", 2, true);
            member.setInsideRaids(true);
            for (Skill skill : Skill.values())
            member.getSkillManager().setCurrentLevel(skill,
            member.getSkillManager().getMaxLevel(skill));
            member.getSkillManager().stopSkilling();
            member.getPacketSender().sendClientRightClickRemoval();
           
        }
        for (Player player : party.getPlayers()) 
        {
            player.getRaidsSaving().initPhase2 = false;
            player.getRaidsSaving().initPhase3 = false;
            player.getPacketSender().sendCameraNeutrality();
            player.setRaidsParty(party);
            player.setInsideRaids(true);
            player.getPacketSender().sendInteractionOption("null", 2, true);
            party.getPlayersInRaids().add(player);
            player.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
        }
        for (Player memberr : party.getPlayers()) 
        {
            if (memberr != null && memberr.isInsideRaids())
            {
                memberr.getPacketSender().sendCameraShake(2, 2, 1, 1);
            }
        }
        
        TaskManager.submit(new Task(1) 
        {
            int tick = 0;

            @Override
            public void execute() 
            {
                if (tick == 10) 
                {
                    startTask(party, height);
             
                for (Player memberr : party.getPlayers()) 
                {
                    memberr.getPacketSender().sendCameraNeutrality();
                }
            }
                if(tick == 20 ) {
                	 for (NPC npc : World.getNpcs()) 
                     {
                     	if (npc != null && npc.getPosition().getZ() == party.getHeight()) 
                         {
                     		npc.getCombatBuilder().attack(p);
                     		npc.forceChat("Do your best my fellow Anime!");
                         }
                     }
                }
                if(tick == 15) {
                	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_TWO_PHASE_ONE_INSTANCE));
                	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P1_FIRST_NPC_ID, new Position(2976, 2846, height)));
                	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P1_SECOND_NPC_ID, new Position(2973, 2850,	height)));
                	p.getRegionInstance().spawnNPC(new NPC(1502, new Position(2970, 2845,	height)));
                }
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to Anime Raids! Goodluck!");
    }
 
    public static void sendPhaseTwo(RaidsParty party)//how many times is this supposed to spawn? 1? yes and only 3 are suppose to 
    {
    	
    	Player owner = party.getOwner();
    	
    	final int height = owner.getIndex() * 4;
    	isPhase2 = true;
    	
    	for (Player player : party.getPlayers()) {
        player.getPacketSender().sendInterfaceRemoval();
    	player.sendMessage("<img=10>@blu@Your Party has made it to Phase 2!");
    	player.getRaidsSaving().initPhase2 = true;
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_TWO_PHASE_TWO_INSTANCE));
        player.moveTo(new Position(2995, 2867 + Misc.getRandom(3), height));
    	party.setHeight(height);
        party.setInstanceLevel(height);
     	player.getPointsHandler().incrementRaidsTwoPoints(15);
    	}
        
    	
    	
    	 System.err.println("LOADING PHASE 2....");
         TaskManager.submit(new Task(2, owner, false) {
         	
  			@Override
  			public void execute() {
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P2_FOURTH_NPC_ID, new Position(2999, 2869, height)));
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P2_THIRD_NPC_ID, new Position(2994, 2869,	height)));
  				owner.getRegionInstance().spawnNPC(new NPC(1505, new Position(2996, 2871,	height)));
  				stop();
  			}
  		});
    	
    	for (NPC npc : World.getNpcs()) {
        	if (npc != null && npc.getPosition().getZ() == party.getHeight()) {
        		npc.getCombatBuilder().attack(owner);
        		npc.forceChat("You Can't Kill Me What Are You Thinking!!!!!");
            }
        }
    
    	
}
    
    public static void sendPhaseThree(RaidsParty party)
    {
    	Player owner = party.getOwner();
    	
    	final int height = owner.getIndex() * 4;
    	
    	isPhase3 = true;
    	for (Player player : party.getPlayers()) {
    	player.getPacketSender().sendInterfaceRemoval();
    	player.sendMessage("<img=10>@blu@Your Party has made it to Phase 3!");
        player.getRaidsSaving().initPhase3 = true;
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_TWO_PHASE_THREE_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(2995, 2829 + Misc.getRandom(3), height));
     	player.getPointsHandler().incrementRaidsTwoPoints(15);
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P3_FIFTH_NPC_ID, new Position(3000, 2827, height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P3_SIXTH_NPC_ID, new Position(2990, 2829,	height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R2P3_SEVENTH_NPC_ID, new Position(2996, 2833, height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(8498, new Position(2994, 2833,	height)));
  		    	stop(); 			
  		    }
        });
        
       
	    	for (NPC npc : World.getNpcs()) {
	        	if (npc != null && npc.getPosition().getZ() == party.getHeight()) {
	        		npc.getCombatBuilder().attack(owner);
	        		npc.forceChat("Unite!");
	        	}
	    	}
        
    
 }
    
    public static void startTask(RaidsParty party, int height) 
    {
    	   TaskManager.submit(new Task(1, party, false) 
           {
               private int tick = 0;
               @Override
               public void execute() 
               {
      
                   if (party.getPlayersInRaidsLocation(party) == 0) 
                   {
                	   party.enteredDungeon(false);
                       for (NPC npc : World.getNpcs()) 
                       {
                       	if (npc != null && npc.getPosition().getZ() == party.getHeight())
                       		World.deregister(npc);
                       }
                            
                   			stop();
                   }
                   tick++;
               }
           });
    }
    
    public static void exitRaidsTwo(Player p)
    {   
   	 final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
	 
 	
     p.getPacketSender().sendInterfaceRemoval();
     if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
     {
         DialogueManager.sendStatement(p, "You need to be in a Anime Raids party to begin.");
         return;
     }

     if (party.getOwner() != p) 
     {
         p.getPacketSender().sendMessage("Only the party leader can exit the Pokemon Raid.");
         return;
     }
     if (party.getOwner() == p) 
     {
     	// World.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed Anime Raids! Congrats!" );
     }
   
     party.enteredDungeon(false);
     
     if (p.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() == p) 
     {
     	p.sendMessage("@blu@<img=10> As Party Leader you have recieved a random amount of Bonus Points up to 5!");
     }
    		for(Player partyMember : party.getPlayers()) 
    		{
    			partyMember.getRaidsSaving().initPhase2 = false;
    			partyMember.getRaidsSaving().initPhase3 = false;
    			partyMember.setRegionInstance(null);
    			partyMember.moveTo(new Position(3038, 2850 + Misc.getRandom(3), 0));
    			partyMember.getInventory().add(21013, 1);
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    			partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    			partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    			partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    			partyMember.getEquipment().refreshItems();
    		  	partyMember.getPointsHandler().incrementRaidsTwoPoints(50);
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raids Two Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Anime Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
             	partyMember.getRaidsSaving().completedRaids2 = true;
             	partyMember.getCustomTasks().handleMinigameAction("anime_raids", 1);
    	   }
      }
 }
