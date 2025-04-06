package com.ruseps.engine.task.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.GameMode;
import com.ruseps.model.Locations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.Ankou;
import com.ruseps.world.content.TrioBosses;
import com.ruseps.world.content.Wildywyrm;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.content.KillsTracker.KillsEntry;
import com.ruseps.world.content.PlayerPunishments.Jail;
import com.ruseps.world.content.achievements.AchievementData;
import com.ruseps.world.content.TreasureChest;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.strategy.impl.GiantMole;
import com.ruseps.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.content.minigames.custom_minigames.Defenderz;
import com.ruseps.world.content.minigames.custom_minigames.Magicz;
import com.ruseps.world.content.minigames.custom_minigames.Rangez;
import com.ruseps.world.content.minigames.custom_minigames.Weaponz;
import com.ruseps.world.entity.impl.npc.impl.SmooziesLoot;
import com.ruseps.world.content.minigames.impl.RaichuInstance;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_eight.RaidsEightLoot;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_one.R1P1NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_one.R1P1NPC2;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC2;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC3;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_two.R1P2NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_two.R1P2NPC2;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_two_loot.phase_one.R2P1NPC1;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.raids_system.RaidsConstants;
import com.ruseps.world.content.skill.impl.slayer.SlayerTasks;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.*;
import com.ruseps.world.entity.impl.npc.impl.SantaLoot;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything
 * an npc does before and after their death animation (including it), 
 * such as dropping their drop table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {
	/*
	 * The array which handles what bosses will give a player points
	 * after death
	 */
	private Set<Integer> BOSSES = new HashSet<>(Arrays.asList(
			1042, 1417, 1511, 3007, 1039, 5148, 1038, 1494, 3012, 1018, 185, 4387, 190, 4263 , 1506, 1508, 1509, 1016, 1510, 4998,5048,4606, 4264, 4266, 4269, 4272, 3009, 4274, 3009, 4274, 3009, 3010, 3011, 3014,50,2001,11558,4540, 1158, 8133, 3200, 13447, 8549,3851,1382,8133,13447,2000,2009,2006
	)); //use this array of npcs to change the npcs you want to give boss points
	
	/**
	 * The NPCDeathTask constructor.
	 * @param npc	The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute() {
		try {
			npc.setEntityInteraction(null);
			switch (ticks) {
			case 2:
			
				npc.getMovementQueue().setLockMovement(true).reset();
				killer = npc.getCombatBuilder().getKiller(npc.clearDamageMapOnDeath());
				final RaidsParty party = killer.getMinigameAttributes().getRaidsAttributes().getParty();
				
				if (npc.getId() == 706) {
					R2P1NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				/** CUSTOM NPC DEATHS **/
				if(npc.getId() == 13447) {
					Nex.handleDeath();
				}
				if(npc.getId() == 4340) {
					RaichuLoot.handleDrop(npc);
				}
				if(npc.getId() == 5763) {
					Defenderz.handleDrop(killer, npc);
				}
				if(npc.getId() == 2237) {
					Weaponz.handleDrop(killer, npc);
				}
				if(npc.getId() == 1415) {
					Magicz.handleDrop(killer, npc);
				}
				if(npc.getId() == 1414) {
					Rangez.handleDrop(killer, npc);
				}
				if (npc.getId() == 8540) {
					SantaLoot.handleDrop(npc);
				}
				/** RAIDS 1 Phase 1 DROPS **/
				if (npc.getId() == RaidsConstants.R1P1_FIRST_NPC_ID) {
					R1P1NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R1P1_SECOND_NPC_ID) {
					R1P1NPC2.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				/** RAIDS 1 Phase 2 DROPS **/
				if (npc.getId() == RaidsConstants.R1P2_THIRD_NPC_ID) {
					R1P2NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R1P2_FOURTH_NPC_ID) {
					R1P2NPC2.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				/** RAIDS 1 Phase 3 DROPS **/
				if (npc.getId() == RaidsConstants.R1P3_FIFTH_NPC_ID) {
					R1P3NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R1P3_SIXTH_NPC_ID) {
					R1P3NPC2.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R1P3_SEVENTH_NPC_ID) {
					R1P3NPC3.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 701) {;
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 5159) {
					R1P3NPC3.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }	
				}
				if (npc.getId() == 5158) {
					R1P3NPC3.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				
				//Achievements
		
				if (npc.getId() == 4981) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_CHARIZARD_100_TIMES, 1);
				}
				if (npc.getId() == 4268) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_TIKI_DEMON_250_TIMES, 1);
				}
				if(npc.getId() == 1417) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.DEFEAT_OCTANES_1000_TIMES, 1);		
				}
				if(npc.getId() == 1038) {
				killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.DEFEAT_KNIGHT_OF_TORMENT_10K_TIMES, 1);		
				}
				if (npc.getId() == 4274) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_BAD_BITCH_1500_TIMES, 1);
					
				}
				if (npc.getId() == 3012) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_SPUDERMAN_40K_TIMES, 1);
				
				}
				if (npc.getId() == 1494) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_LITCH_25K_TIMES, 1);
			
				}
				if (npc.getId() == 1509) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_FOUR_ARMS_2500_TIMES, 1);
				}
				
				if (npc.getId() == 5157) {
					R1P3NPC3.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R2P1_FIRST_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R2P1_SECOND_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				/** RAIDS 1 Phase 2 DROPS **/
				if (npc.getId() == RaidsConstants.R2P2_THIRD_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R2P2_FOURTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				/** RAIDS 1 Phase 3 DROPS **/
				if (npc.getId() == RaidsConstants.R2P3_FIFTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R2P3_SIXTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R2P3_SEVENTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 1502) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 1505) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }	
				}
				if (npc.getId() == 8498) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P1_FIRST_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P2_FOURTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P3_FIFTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P4_EIGHTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P5_ELEVENTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R3P5_TWLEVE_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P1_FIRST_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P1_SECOND_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P2_THIRD_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P2_FOURTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P3_FIFTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R4P3_SIXTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R5P1_FIRST_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R5P2_SECOND_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R5P3_THIRD_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R5P4_FOURTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R5P4_FIFTH_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R6P1_FIRST_NPC_ID) {
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == RaidsConstants.R7P1_FIRST_NPC_ID) {
					for (Player member : party.getPlayers()) {
						member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					}
				}
				if (npc.getId() == 200 ) {
					R2P1NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 199 ) {
					R2P1NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				if (npc.getId() == 198 ) {
					R2P1NPC1.handleDrop(npc);
					 for (Player member : party.getPlayers()) {
						 member.getMinigameAttributes().getRaidsAttributes().incrementKillcount();
					 }
				}
				
				if (npc.getId() == SmooziesLoot.NPC_ID) {				
					SmooziesLoot.handleDrop(npc);
				}
				if (npc.getId() == VaderLoot.NPC_ID) {
					VaderLoot.handleDrop(npc);
				}
				if (npc.getId() == OctaneLoot.NPC_ID) {
					OctaneLoot.handleDrop(npc);
				}
				if (npc.getId() == ReaperLoot.NPC_ID) {
					ReaperLoot.handleDrop(npc);
				}
				if (npc.getId() == DemonLoot.NPC_ID) {
					DemonLoot.handleDrop(npc);
				}
				if (npc.getId() == OwnerBossLoot.NPC_ID) {
					OwnerBossLoot.handleDrop(npc);	
				}
				if(npc.getId() == DonationBossLoot.NPC_ID) {
					DonationBossLoot.handleDrop(npc);
				}
				if (npc.getId() == DragonLoot.BLUE_NPC_ID) {
					DragonLoot.handleDrop(npc);
				}
				if (npc.getId() == DragonLoot.GREEN_NPC_ID) {
					DragonLoot.handleDrop(npc);
				}
				if (npc.getId() == DragonLoot.PURPLE_NPC_ID) {
					DragonLoot.handleDrop(npc);
				}
				if (npc.getId() == DragonLoot.RED_NPC_ID) {
					DragonLoot.handleDrop(npc);
				}
				if (npc.getId() == TelosLoot.RED_NPC_ID) {
					TelosLoot.handleDrop(npc);
				}
				if (npc.getId() == TelosLoot.GREEN_NPC_ID) {
					TelosLoot.handleDrop(npc);
				}
				if (npc.getId() == TelosLoot.BLUE_NPC_ID) {
					TelosLoot.handleDrop(npc);
				}
				if (npc.getId() == BlessedSpartanLoot.NPC_ID) {
					BlessedSpartanLoot.handleDrop(npc);
				}
				if (npc.getId() == InheritedLoot.NPC_ID) {
					InheritedLoot.handleDrop(npc);
				}
				if(npc.getId() == 5049) {	
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_SNOWMAN, 1);	
				}
				if(npc.getId() == 4990) {	
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_RYUK, 1);	
				}
				if(npc.getId() == 1416) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.DEFEAT_INHERITED_BOSS, 1);	
				}
				if(npc.getId() == 4867) {
					killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.DEFEAT_LAVA_DEMON_BOSS, 1);	
				}
				break;
			case 0:
				if(killer != null) {

					boolean boss = (npc.getDefaultConstitution() > 2000);
					if(!Nex.nexMinion(npc.getId()) && npc.getId() != 1158 && !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
						
						KillsTracker.submitById(killer, npc.getId(), true, boss);
						KillsTracker.submitById(killer, npc.getId(), false, boss);
						if(boss) {
							//Achievements.doProgress(killer, AchievementData.DEFEAT_500_BOSSES);
						}
					}
					if (BOSSES.contains(npc.getId())) {
					    int additionalPoints = 1; // Default points

					    PlayerRights rights = killer.getRights();

					    if (rights == PlayerRights.RUBY_MEMBER || rights == PlayerRights.DRAGONSTONE_MEMBER || rights == PlayerRights.ADMINISTRATOR|| rights == PlayerRights.OWNER) {
					        additionalPoints = 5;
					    } else if (rights == PlayerRights.DIAMOND_MEMBER || rights == PlayerRights.MANAGER || rights == PlayerRights.MODERATOR) {
					        additionalPoints = 3;
					    }

					    killer.setBossPoints(killer.getBossPoints() + additionalPoints);

					    if (killer.kcMessage) {
					        killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss Points!");
					    }
					}
					
					if (killer.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
					    SlayerTasks currentTask = killer.getSlayer().getSlayerTask();
					    
					    // Check if the NPC being killed matches the current Slayer task NPC
					    if (currentTask.getNpcId() == npc.getId()) {
					        if (Misc.getRandom(999) == 0) { // 0.05% chance
					            // Spawn Superior NPC here
					            int superiorNPCId = getRandomSuperiorNPCId(); // Implement this method to get a random Superior NPC ID
					            if (superiorNPCId != -1) {
					                NPC superiorNPC = new NPC(superiorNPCId, npc.getPosition().copy());
					                World.register(superiorNPC);
					            }
					        }
					    }
					}
					
					if(npc.getId() == 3847) {
						killer.resetKraken();
						killer.getKrakenRespawn().reset();
					}
					/** ACHIEVEMENTS **/
					switch(killer.getLastCombatType()) {
					case MAGIC:
						killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_MAGIC, 1);
						   if(killer.getGameMode()== GameMode.IRONMAN ||killer.getGameMode()== GameMode.HARDCORE_IRONMAN || killer.getGameMode() == GameMode.GROUP_IRONMAN) {
							   if(killer.getAchievementTracker().getProgressFor(AchievementData.VISIT_POS) != 1)
									killer.sendMessage("Visit POS Has Been Auto Completed For You Since You're An Ironman");
					        	killer.getAchievementTracker().progress(AchievementData.VISIT_POS, 1);
					        }
						break;
					case MELEE:
						killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_MELEE, 1);
						if(killer.getGameMode()== GameMode.IRONMAN ||killer.getGameMode()== GameMode.HARDCORE_IRONMAN || killer.getGameMode() == GameMode.GROUP_IRONMAN) {
							if(killer.getAchievementTracker().getProgressFor(AchievementData.VISIT_POS) != 1)
								killer.sendMessage("Visit POS Has Been Auto Completed For You Since You're An Ironman");
				        	killer.getAchievementTracker().progress(AchievementData.VISIT_POS, 1);
				        	
				        }
						break;
					case RANGED:
						killer.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_RANGED, 1);
						if(killer.getGameMode()== GameMode.IRONMAN ||killer.getGameMode()== GameMode.HARDCORE_IRONMAN || killer.getGameMode() == GameMode.GROUP_IRONMAN) {
							if(killer.getAchievementTracker().getProgressFor(AchievementData.VISIT_POS) != 1)
								killer.sendMessage("Visit POS Has Been Auto Completed For You Since You're An Ironman");
				        	killer.getAchievementTracker().progress(AchievementData.VISIT_POS, 1);
				        }
						break;
					}
					
					if(killer.getInstanceManager().inInstance && killer.getPosition().getZ() == npc.getPosition().getZ()) {
				   	//	npc.setShouldRespawn(false);
						for(Map.Entry<Position, NPC> entry : killer.getInstanceManager().npcMap.entrySet()) {
							if(npc == entry.getValue()) {
								entry.setValue(null);
							}
						}
						killer.getInstanceManager().currKc++;
						if(killer.getInstanceManager().currKc % 50 == 0) {
							killer.sendMessage("<shad=1>@blu@You have @red@" + Integer.toString( killer.getInstanceManager().kcAllowed - killer.getInstanceManager().currKc) + " @blu@Spawns left.");
						}
						
						if(killer.getInstanceManager().currKc <= killer.getInstanceManager().kcAllowed) {
							killer.getInstanceManager().respawnNpc(killer, npc);
						}
						else {							
							killer.getInstanceManager().endInstance(killer, true);
						}
							
					}
					

					/** LOCATION KILLS **/
					if(npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}
					
					int X2_KC_PET = 10001;
					
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() ==
							(X2_KC_PET)) {
							killer.addNpcKillCount(npc.getId());
							killer.addNpcKillCount(npc.getId());
					} else {
						killer.addNpcKillCount(npc.getId());
					}
				
					killer.getCustomTasks().handleNpcKill(npc.getId());
					
                    String npcName = Misc.formatText(npc.getDefinition().getName());

                    killer.getStarterProgression().handleKill(npc.getId());
			
                    if(killer.kcMessage != false) {
						killer.sendMessage("<shad=1>@mag@You currently have " + killer.getNpcKillCount(npc.getId())
								+ " kills of <shad=1>@bla@" + npcName);
					}
					
					if(npc.getId() == 1973) {
						TrioBosses.handleSkeleton(killer, npc.getPosition());
					}
					if(npc.getId() == 75) {
						TrioBosses.handleZombie(killer, npc.getPosition());
					}
					if(npc.getId() == 103) {
						TrioBosses.handleGhost(killer, npc.getPosition());
					}
					if (World.minigameHandler.handleNpcDeath(npc)) {
						stop();
						return;
					}
					
					
					if(npc.getId() == 1531) {
						if(killer.getAmountJailed() <= 1) {
							Jail.unjail(killer);
							
							killer.getPA().sendMessage("@red@You have been unjailed. Don't do it again!");
						} else {
						killer.setJailAmount(killer.getAmountJailed() -1);
						killer.getPA().sendMessage(killer.getAmountJailed()+ " / "+killer.getTotalAmountJailed()+" imps still have to be killed.");
								
								
						}
					}
					
					/*
					 * End Trio Bosses
					 */
					boolean isX2 = false;
					if (killer.getSummoning().getFamiliar() != null) {
					    isX2 = killer.getSummoning().getFamiliar().getSummonNpc().getId() == X2_KC_PET;
					}
					killer.getDailyTaskManager().submitProgressToIdentifier(0, isX2 ? 2 : 1);
					killer.getDailyTaskManager().submitProgressToIdentifier(1, isX2 ? 2 : 1);
					killer.getDailyTaskManager().submitProgressToIdentifier(2, isX2 ? 2 : 1);
					killer.getDailyTaskManager().submitProgressToIdentifier(24, isX2 ? 2 : 1);
					killer.getDailyTaskManager().submitProgressToIdentifier(29, isX2 ? 2 : 1);
					
					/** PARSE DROPS **/
					if(npc.getId() == 3334) {
						Wildywyrm.handleDrop(npc);
					} 
					
					if(npc.getId() == 4383) {
						Ankou.giveLoot(killer, npc, npc.getPosition());
					//killer.getPacketSender().sendMessage("gothere");
					
					} else {
						NPCDrops.dropItems(killer, npc);
					}

					/** SLAYER **/
					
					killer.getSlayer().killedNpc(npc);
				}
				stop();
				break;
			}
			ticks--;
		} catch(Exception e) {
			e.printStackTrace();
			stop();
		}
	}
	
	private int getRandomSuperiorNPCId() {
	    // Return the specific Superior NPC ID 
	    return 499;
	}	

	@Override
	public void stop() {
		setEventRunning(false);

		npc.setDying(false);

		//respawn
		if(npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.DUNGEONEERING && npc.shouldRespawnNpc()) {
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime()));
		}

		World.deregister(npc);

		if(npc.getId() == 1158 || npc.getId() == 1160) {
			KalphiteQueen.death(npc.getId(), npc.getPosition());
		}
		if(Nex.nexMob(npc.getId())) {
			Nex.death(npc.getId());
		}
	}
}
