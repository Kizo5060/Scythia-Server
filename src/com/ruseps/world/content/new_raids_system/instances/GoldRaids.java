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

public class GoldRaids
{
	public static boolean isPhase2 = false;
	public static boolean isPhase3 = false;
	public static boolean isPhase4 = false;
	
    public static void startRaidsFive(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	p.sendMessage("<img=10>@blu@Phase One has begun!!");
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Gold Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Gold Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Silver Raid.");
            return;
        }
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(3191, 3781 + Misc.getRandom(3), height));
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
                	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_FIVE_PHASE_ONE_INSTANCE));
                	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R5P1_FIRST_NPC_ID, new Position(3191, 3786, height)));
                }
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to Gold Raids! Goodluck!");
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_FIVE_PHASE_TWO_INSTANCE));
        player.moveTo(new Position(3190, 3828 + Misc.getRandom(3), height));
    	party.setHeight(height);
        party.setInstanceLevel(height);
    	}
        
    	
    	
    	 System.err.println("LOADING PHASE 2....");
         TaskManager.submit(new Task(2, owner, false) {
         	
  			@Override
  			public void execute() {
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R5P2_SECOND_NPC_ID, new Position(3191, 3834, height)));
  				
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_FIVE_PHASE_THREE_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3143, 3826 + Misc.getRandom(3), height));
     	
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R5P3_THIRD_NPC_ID, new Position(3143, 3832, height)));
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_FIVE_PHASE_FOUR_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3143, 3787 + Misc.getRandom(3), height));
     
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R5P4_FOURTH_NPC_ID, new Position(3143, 3781, height)));
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R5P4_FIFTH_NPC_ID, new Position(3147, 3782, height)));
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
    
    public static void exitRaidsFive(Player p)
    {   
    	 final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
    	 
    	p.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed Gold Raids! Congrats!" );
    	
        p.getPacketSender().sendInterfaceRemoval();
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a Gold Raids party to begin.");
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
    			partyMember.setRegionInstance(null);
    		    if(partyMember.getRights() != PlayerRights.GOLD_MEMBER){
                	partyMember.moveTo(GameSettings.DEFAULT_POSITION);
                } else {
                	partyMember.moveTo(new Position(2908, 2517 + Misc.getRandom(3), 0));
                }
    			partyMember.getInventory().add(2750, 1);
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    			partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    			partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    			partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    			partyMember.getEquipment().refreshItems();
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raids Two Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Gold Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsFive();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
             	partyMember.getRaidsSaving().completedRaids5 = true;
    	   }
      }
 }
