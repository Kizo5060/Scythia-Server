package com.ruseps.world.content.new_raids_system.instances;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.PlayerRights;
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

public class SilverRaids 
{
	public static boolean isPhase2 = false;
	public static boolean isPhase3 = false;
	public static boolean isPhase4 = false;
	public static boolean isPhase5 = false;
	
    public static void startRaidsThree(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	p.sendMessage("<img=10>@blu@Phase One has begun!!");
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Silver Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Silver Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Silver Raid.");
            return;
        }
        
        final int MIN_DONATION_AMOUNT = 100; // Replace this with your actual threshold

        // Check if all party members have donated the required amount or have allowed roles
        for (Player member : party.getPlayers()) {
            int donatedAmount = member.getAmountDonated(); // Replace with the actual method to get the donated amount
            if (donatedAmount < MIN_DONATION_AMOUNT) {
                p.getPacketSender().sendMessage("All party members need to be Silver Members or higher to start the raid.");
                return;
            }
        }
        
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(3370, 3914 + Misc.getRandom(3), height));
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
            player.getRaidsSaving().initPhase4 = false;
            player.getRaidsSaving().initPhase5 = false;
            
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
                if (tick == 3) 
                {
                    startTask(party, height);
             
                for (Player memberr : party.getPlayers()) 
                {
                    memberr.getPacketSender().sendCameraNeutrality();
                }
            }
                if(tick == 10) {
                	 for (NPC npc : World.getNpcs()) 
                     {
                     	if (npc != null && npc.getPosition().getZ() == party.getHeight()) 
                         {
                     		npc.getCombatBuilder().attack(p);
                     		npc.forceChat("Do your best my fellow Anime!");
                         }
                     }
                }
                if(tick == 5) {
                	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_THREE_PHASE_ONE_INSTANCE));
                	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P1_FIRST_NPC_ID, new Position(3370, 3919, height)));
                }
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to Silver Raids! Goodluck!");
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_THREE_PHASE_TWO_INSTANCE));
        player.moveTo(new Position(3337, 3914 + Misc.getRandom(3), height));
    	party.setHeight(height);
        party.setInstanceLevel(height);
    	}
        
    	
    	
    	 System.err.println("LOADING PHASE 2....");
         TaskManager.submit(new Task(2, owner, false) {
         	
  			@Override
  			public void execute() {
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P2_FOURTH_NPC_ID, new Position(3335, 3922, height)));
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_THREE_PHASE_THREE_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3336, 3952 + Misc.getRandom(3), height));
     	
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P3_FIFTH_NPC_ID, new Position(3336, 3960, height)));
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
    public static void sendPhaseFour(RaidsParty party)
    {
    	Player owner = party.getOwner();
    	
    	final int height = owner.getIndex() * 4;
    	
    	isPhase4 = true;
    	for (Player player : party.getPlayers()) {
    	player.getPacketSender().sendInterfaceRemoval();
    	player.sendMessage("<img=10>@blu@Your Party has made it to Phase 4!");
        player.getRaidsSaving().initPhase4 = true;
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_THREE_PHASE_FOUR_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3366, 3956 + Misc.getRandom(3), height));
     
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P4_EIGHTH_NPC_ID, new Position(3368, 3961, height)));
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
    
    public static void sendPhaseFive(RaidsParty party)
    {
    	Player owner = party.getOwner();
    	
    	final int height = owner.getIndex() * 4;
    	
    	isPhase5 = true;
    	for (Player player : party.getPlayers()) {
    	player.getPacketSender().sendInterfaceRemoval();
    	player.sendMessage("<img=10>@blu@Your Party has made it to The final Phase!");
        player.getRaidsSaving().initPhase5 = true;
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_THREE_PHASE_FIVE_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3354, 3933 + Misc.getRandom(3), height));
     	
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P5_ELEVENTH_NPC_ID, new Position(3353, 3939, height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R3P5_TWLEVE_NPC_ID, new Position(3355, 3940,	height)));
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
    
    public static void exitRaidsThree(Player p)
    {   
    	 final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
    	 
    	p.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed Silver Raids! Congrats!" );
    	
        p.getPacketSender().sendInterfaceRemoval();
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a Silver Raids party to begin.");
            return;
        }

        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can exit the Silver Raid.");
            return;
        }
        party.enteredDungeon(false);
        
        	for(Player partyMember : party.getPlayers()) 
    	{
    			partyMember.getRaidsSaving().initPhase2 = false;
    			partyMember.getRaidsSaving().initPhase3 = false;
    			partyMember.getRaidsSaving().initPhase4 = false;
    			partyMember.getRaidsSaving().initPhase5 = false;
    			partyMember.setRegionInstance(null);
    			
    			if(partyMember.getRights() != PlayerRights.SILVER_MEMBER){
                	partyMember.moveTo(GameSettings.DEFAULT_POSITION);
                } else {
                	partyMember.moveTo(new Position(1245, 1246 + Misc.getRandom(3), 0));
                }
    			
    			partyMember.getInventory().add(18872, 1);
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    			partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    			partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    			partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    			partyMember.getEquipment().refreshItems();
    			partyMember.getPointsHandler().incrementRaidsOnePoints(15);
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raid Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Silver Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsThree();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
             	partyMember.getRaidsSaving().completedRaids3 = true;
    	   }
      }
 }
