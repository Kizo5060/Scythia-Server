package com.ruseps.world.content.new_raids_system.instances;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.Skill;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.achievements.AchievementData;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.raids_system.RaidsConstants;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class DarkDementionRaids {
	public static boolean isPhase2 = false;
	public static boolean isPhase3 = false;
	
    public static void startRaidsTwo(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Dark Dimension Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Dark Dimension Raid.");
            return;
        }
        boolean kcFlag = false;
      if(party.getOwner() == p) {
    	    for (Player member : party.getPlayers()){
        		if(member.getAchievementTracker().getProgressFor(AchievementData.COMPLETE_MORTAL_KOMBAT_RAIDS_150_TIMES) < 150) {
        			kcFlag = true;
        			if(member == party.getOwner()) {
        				member.getPacketSender().sendMessage("You need 150 MK Raids completion to start Dark Dimension Raid");
        				continue;
        			}
        			member.getPacketSender().sendMessage(member.getUsername()+" needs 150 MK Raids Raids completion to start Dark Dimension Raid");
        			p.getPacketSender().sendMessage(member.getUsername()+" needs 150 MK Raids Raids completion to start Dark Dimension Raid");
        		}
        	}
        	if(kcFlag) {
        		return;
        	}
        }
        p.sendMessage("<img=10>@blu@Phase One has begun!!");
    
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(3372, 3232 + Misc.getRandom(3), height));
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
                if (tick == 3) 
                {
                    startTask(party, height);
             
                for (Player memberr : party.getPlayers()) 
                {
                    memberr.getPacketSender().sendCameraNeutrality();
                }
            }
                if(tick == 10 ) {
                	 for (NPC npc : World.getNpcs()) 
                     {
                     	if (npc != null && npc.getPosition().getZ() == party.getHeight()) 
                         {
                     		npc.getCombatBuilder().attack(p);
                     		npc.forceChat("HAHA, So In the duel arena we meet!");
                         }
                     }
                }
                if(tick == 5) {
                	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_DD_PHASE_ONE_INSTANCE));
                	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.RDD_P1_ID, new Position(3378, 3232, height)));
                }
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to Dark Dimension Raids! Goodluck!");
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_DD_PHASE_TWO_INSTANCE));
        player.moveTo(new Position(3343, 3230 + Misc.getRandom(3), height));
    	party.setHeight(height);
        party.setInstanceLevel(height);
     	player.getPointsHandler().incrementRaidsTwoPoints(15);
    	}
        
    	
    	
    	 System.err.println("LOADING PHASE 2....");
         TaskManager.submit(new Task(2, owner, false) {
         	
  			@Override
  			public void execute() {
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.RDD_P2_ID, new Position(3343, 3238, height)));
  				stop();
  			}
  		});
    	
    	for (NPC npc : World.getNpcs()) {
        	if (npc != null && npc.getPosition().getZ() == party.getHeight()) {
        		npc.getCombatBuilder().attack(owner);
        		npc.forceChat("You Can't Kill Me What Are You Thinking!!!!!");
        		npc.forceChat("Try getting throught these walls!");
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_DD_PHASE_THREE_INSTANCE));
    	party.setHeight(height);
        party.setInstanceLevel(height);
        player.moveTo(new Position(3338, 3213 + Misc.getRandom(3), height));
     	player.getPointsHandler().incrementRaidsTwoPoints(15);
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.RDD_P3_ID, new Position(3345, 3213, height)));
  		    	stop(); 			
  		    }
        });
        
       
	    	for (NPC npc : World.getNpcs()) {
	        	if (npc != null && npc.getPosition().getZ() == party.getHeight()) {
	        		npc.getCombatBuilder().attack(owner);
	        		npc.forceChat("Our Last Wizard!");
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
    
    public static void exitRaidsDD(Player p)
    {   
   	 final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
	 
 	
     p.getPacketSender().sendInterfaceRemoval();
     if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
     {
         DialogueManager.sendStatement(p, "You need to be in a Dark Dimension Raids party to begin.");
         return;
     }

     if (party.getOwner() != p) 
     {
         p.getPacketSender().sendMessage("Only the party leader can exit the Demention Raids Raid.");
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
    			partyMember.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
    			partyMember.getInventory().add(13234, 1); //change it :D
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    		//	partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    		//	partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    			//partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    			partyMember.getEquipment().refreshItems();
    		  	partyMember.getPointsHandler().incrementRaidsOnePoints(40);
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raid Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Dark Dimension Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
             	partyMember.getRaidsSaving().completedRaids2 = true;
    	   }
      }
}
