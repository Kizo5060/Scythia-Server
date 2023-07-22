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

public class PokemonRaids 
{
	public static boolean isPhase2 = false;
	public static boolean isPhase3 = false;
	
    public static void startRaid(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	p.sendMessage("<img=10>@blu@Phase One has begun!!");
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_ONE_PHASE_ONE_INSTANCE));
        	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P1_FIRST_NPC_ID, new Position(3051, 2908, height)));
        	p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P1_SECOND_NPC_ID, new Position(3051, 2895,	height)));
        	p.getRegionInstance().spawnNPC(new NPC(5159, new Position(3055, 2901,	height)));
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Pokemon Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Pokemon Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Pokemon Raid.");
            return;
        }
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(3051, 2901 + Misc.getRandom(3), height));
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
                if(tick == 7 ) {
               	 for (NPC npc : World.getNpcs()) 
                    {
                    	if (npc != null && npc.getPosition().getZ() == party.getHeight()) 
                        {
                    		npc.getCombatBuilder().attack(p);
                    		npc.forceChat("Do your best my fellow Poke Men!!");
                        }
                    }
               }
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to Pokemon Raids! Goodluck!");
    }
 
    public static void sendPhaseTwo(RaidsParty party)//how many times is this supposed to spawn? 1? yes and only 3 are suppose to 
    {
    	
    	Player owner = party.getOwner();
    	
    	final int height = owner.getIndex() * 4;
    	
    	System.err.println("starting phase from... - "+new Throwable());
    	isPhase2 = true;
    	
    	for (Player player : party.getPlayers()) {
        player.getPacketSender().sendInterfaceRemoval();
    	player.sendMessage("<img=10>@blu@Your Party has made it to Phase 2!");
    	player.getRaidsSaving().initPhase2 = true;
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_ONE_PHASE_TWO_INSTANCE));
        player.moveTo(new Position(3032, 2901 + Misc.getRandom(3), height));
    	}
        
    	
    	
    	 System.err.println("LOADING PHASE 2....");
         TaskManager.submit(new Task(2, owner, false) {
         	
  			@Override
  			public void execute() {
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P2_FOURTH_NPC_ID, new Position(3031, 2907, height)));
  				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P2_THIRD_NPC_ID, new Position(3031, 2896,	height)));
  				owner.getRegionInstance().spawnNPC(new NPC(5158, new Position(3026, 2901,	height)));
  				stop();
  			}
  		});
    	
    	for (NPC npc : World.getNpcs()) {
        	if (npc != null && npc.getPosition().getZ() == party.getHeight()) {
        		npc.getCombatBuilder().attack(owner);
        		npc.forceChat("Hold your ground!!!!!");
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
        player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAIDS_ONE_PHASE_THREE_INSTANCE));
        player.moveTo(new Position(3042, 2922 + Misc.getRandom(3), height));
        
    	}
    	
    	TaskManager.submit(new Task(2, owner, false) {//done u sure s
            
 			@Override
 			public void execute() {
 				owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P3_FIFTH_NPC_ID, new Position(3041, 2917, height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P3_SIXTH_NPC_ID, new Position(3041, 2928,	height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R1P3_SEVENTH_NPC_ID, new Position(3039, 2925, height)));
 		    	owner.getRegionInstance().spawnNPC(new NPC(5157, new Position(3042, 2929,	height)));
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
    
    public static void exitRaidsOne(Player p)
    {   
    	 final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
    	 
    	
        p.getPacketSender().sendInterfaceRemoval();
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a Pokemon Raids party to begin.");
            return;
        }

        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can exit the Pokemon Raid.");
            return;
        }
        if (party.getOwner() == p) 
        {
        	// World.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed Pokemon Raids! Congrats!" );
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
    			partyMember.moveTo(new Position(2297, 3331 + Misc.getRandom(3), 0));
    			partyMember.getInventory().add(5585, 1);
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    			partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    			partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    			partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    			partyMember.getEquipment().refreshItems();
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raids One Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Pokemon Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsOne();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
             	partyMember.getRaidsSaving().completedRaids1 = true;
             	partyMember.getCustomTasks().handleMinigameAction("pokemon_raids", 1);
    	   }
      }
 }
