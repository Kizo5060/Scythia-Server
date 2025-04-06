package com.ruseps.world.content.new_raids_system.instances;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.Skill;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.raids_system.RaidsConstants;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class DragonStoneRaids {

    public static void startRaidsDS(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        	p.getPacketSender().sendInterfaceRemoval();
        	p.sendMessage("<img=10>@blu@Phase One has begun!!");
        	party.setHeight(height);
            party.setInstanceLevel(height);
        	
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
        	DialogueManager.sendStatement(p, "You need to be in a Dragonstone Raids party to begin.");
        	return;
        }
        if (party.hasEnteredRaids()) 
        {
        	p.getPacketSender().sendMessage("Your party is already in a Dragonstone Raids!");
            return;
        }
        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can start the DragonStone Raid.");
            return;
        }
        final int MIN_DONATION_AMOUNT = 3500; // Replace this with your actual threshold

        // Check if all party members have donated the required amount or have allowed roles
        for (Player member : party.getPlayers()) {
            int donatedAmount = member.getAmountDonated(); // Replace with the actual method to get the donated amount
            if (donatedAmount < MIN_DONATION_AMOUNT) {
                p.getPacketSender().sendMessage("All party members need to be Dragonstone Members or higher to start the Diamond Raid.");
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
            member.moveTo(new Position(2646, 2786 + Misc.getRandom(3), height));
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
                         }
                     }
                }
                
                if(tick == 5) {
                	p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_DS_PHASE_ONE_INSTANCE));
                	NPC npc = new NPC(RaidsConstants.RDS_P1_ID, new Position(2656, 2784, height));
                	npc.setShouldRespawn(false);
                	p.getRegionInstance().spawnNPC(npc);
                }
                
                tick++;
            }
        });
        
        party.sendMessage("<img=10>@blu@Welcome to DragonStone Raids! Goodluck!");
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
    
    public static void exitRaidsDS(Player p)
    {   
    	final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
    	 
    	p.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed DragonStone Raids! Congrats!" );
        p.getPacketSender().sendInterfaceRemoval();
        
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a DragonStone Raids party to begin.");
            return;
        }

        if (party.getOwner() != p) 
        {
            p.getPacketSender().sendMessage("Only the party leader can exit the DragonStone Raid.");
            return;
        }
        party.enteredDungeon(false); 
        
        for(Player partyMember : party.getPlayers()) 
        {
			partyMember.setRegionInstance(null);
			
            	partyMember.moveTo(new Position(2669, 2785));
        
    			partyMember.setRegionInstance(null);
    			partyMember.getPacketSender().sendInterfaceRemoval();
    			partyMember.getPacketSender().sendCameraNeutrality();
    			partyMember.getMovementQueue().setLockMovement(false);
    		 //	partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
    		 //	partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
    		 //	partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		 	//partyMember.getEquipment().refreshItems();
    			partyMember.getInventory().addItem(275, 1);
    			partyMember.getPointsHandler().incrementRaidsOnePoints(60);
    			partyMember.getPacketSender().sendMessage("<img=10>@blu@"+partyMember.getUsername()+" you have received Raid Points!");
                partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
                partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
    			partyMember.getPacketSender().sendMessage("You have completed the Dragonstone Raids and earned yourself and your team a Raids Key! Congrats!");
        		partyMember.setInsideRaids(false);
        		partyMember.getRaidsOne().getRaidsConnector().leaveRaidsDS();
             	partyMember.getRaidsOne().getRaidsConnector().leave(true); 
    	   }
      }
 
}
