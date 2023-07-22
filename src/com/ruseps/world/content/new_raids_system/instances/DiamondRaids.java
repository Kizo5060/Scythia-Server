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

public class DiamondRaids
{

    public static void startRaidsSeven(final Player p)
    {
        final int height = p.getIndex() * 4;
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();
        p.getPacketSender().sendInterfaceRemoval();
        p.sendMessage("<img=10>@blu@Phase One has begun!!");
        party.setHeight(height);
        party.setInstanceLevel(height);


        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a Diamond Raids party to begin.");
            return;
        }
        if (party.hasEnteredRaids())
        {
            p.getPacketSender().sendMessage("Your party is already in a Diamond Raids!");
            return;
        }
        if (party.getOwner() != p)
        {
            p.getPacketSender().sendMessage("Only the party leader can start the Diamond Raid.");
            return;
        }
        party.enteredDungeon(true);
        for (Player member : party.getPlayers())
        {
            member.getPacketSender().sendInterfaceRemoval();
            member.setRegionInstance(null);
            member.getMovementQueue().reset();
            member.getClickDelay().reset();
            member.moveTo(new Position(2780, 2977 + Misc.getRandom(3), height));
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
                        }
                    }
                }

                if(tick == 15) {
                    p.setRegionInstance(new RegionInstance(p, RegionInstanceType.RAIDS_SEVEN_PHASE_ONE_INSTANCE));
                    p.getRegionInstance().spawnNPC(new NPC(RaidsConstants.R7P1_FIRST_NPC_ID, new Position(2784, 2983, height)));
                }

                tick++;
            }
        });

        party.sendMessage("<img=10>@blu@Welcome to Diamond Raids! Goodluck!");
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

    public static void exitRaidsSeven(Player p)
    {
        final RaidsParty party = p.getMinigameAttributes().getRaidsAttributes().getParty();

        p.sendMessage("<img=10>@blu@ " + p.getUsername() + " and their Raiding Party have completed Diamond Raids! Congrats!" );

        p.getPacketSender().sendInterfaceRemoval();
        if (p.getMinigameAttributes().getRaidsAttributes().getParty() == null)
        {
            DialogueManager.sendStatement(p, "You need to be in a Diamond Raids party to begin.");
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
            partyMember.setRegionInstance(null);
            
            if(partyMember.getRights() != PlayerRights.DIAMOND_MEMBER){
            	partyMember.moveTo(GameSettings.DEFAULT_POSITION);
            } else {
            	  partyMember.moveTo(new Position(1821, 3107 + Misc.getRandom(3), 2));
            }
            
            partyMember.getInventory().add(2747, 1);
            partyMember.getPacketSender().sendInterfaceRemoval();
            partyMember.getPacketSender().sendCameraNeutrality();
            partyMember.getMovementQueue().setLockMovement(false);
            partyMember.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
            partyMember.getPacketSender().sendDungeoneeringTabIcon(false);
            partyMember.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
            partyMember.getEquipment().refreshItems();
            partyMember.getMinigameAttributes().getRaidsAttributes().setKillcount(0);
            partyMember.getMinigameAttributes().getRaidsAttributes().incrementCompleted();
            partyMember.getPacketSender().sendMessage("You have completed the Diamond Raids and earned yourself and your team a Raids Key! Congrats!");
            partyMember.setInsideRaids(false);
            partyMember.getRaidsOne().getRaidsConnector().leaveRaidsSeven();
            partyMember.getRaidsOne().getRaidsConnector().leave(true);
            partyMember.getRaidsSaving().completedRaids7 = true;
        }
    }
}
