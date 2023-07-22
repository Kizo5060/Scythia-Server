package com.ruseps.world.content.battle_royale;

import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.GameSettings;
import com.ruseps.model.Flag;
import com.ruseps.model.Item;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.world.World;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.entity.impl.player.Player;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BattleRoyale {
	
	public static int totalPlayers = 0;
	public static int playersInLobby = 0;
	public static boolean meleeEventRunning = false;
	public static boolean mageEventRunning = false;
	public static boolean rangeEventRunning = false;
	public static boolean mageGameRunning = false;
	public static boolean rangeGameRunning = false;
	public static boolean meleeGameRunning = false;
	public static Map<Player, String> playerMeleeMap = new HashMap<Player, String>();
	public static Map<Player, String> playerMageMap = new HashMap<Player, String>();
	public static Map<Player, String> playerRangeMap = new HashMap<Player, String>();
	private static int waitTimer = 200;
	public static final String WAITING = "WAITING";
	/** REGULAR SPAWNS **/
	public static Position spawn1 = new Position(3321, 9846, 0);
	public static Position spawn2 = new Position(3321, 9850, 0);
	public static Position spawn3 = new Position(3305, 9847, 0);
	public static Position spawn4 = new Position(3309, 9828, 0);
	public static Position spawn5 = new Position(3319, 9829, 0);
	public static Position spawn6 = new Position(3322, 9844, 0);
	public static Position spawn7 = new Position(3316, 9851, 0);
	/** RANGE SPAWNS **/
	public static Position spawn8 = new Position(3287, 9832, 0);
	public static Position spawn9 = new Position(3291, 9839, 0);
	public static Position spawn10 = new Position(3286, 9848, 0);
	public static Position spawn11 = new Position(3276, 9848, 0);
	public static Position spawn12 = new Position(3277, 9850, 0);
	public static Position spawn13 = new Position(3275, 9849, 0);
	public static Position spawn14 = new Position(3276, 9848, 0);
	/** MAGE SPAWNS **/
	public static Position spawn15 = new Position(3321, 9846, 0);
	public static Position spawn16 = new Position(3321, 9850, 0);
	public static Position spawn17 = new Position(3305, 9847, 0);
	public static Position spawn18 = new Position(3309, 9828, 0);
	public static Position spawn19 = new Position(3319, 9829, 0);
	public static Position spawn20 = new Position(3322, 9844, 0);
	public static Position spawn21 = new Position(3316, 9851, 0);

    public static boolean lobbyOpened = false;
    public static String gameName;
    public static int gameType;
    public static Item reward;
    
    public static boolean checkItems(Player player) {
		if (player.getInventory().getFreeSlots() != 28) {
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().get(i).getId() > 0)
				return false;
		}
		return true;
	}
    
    public static String getState(Player player) {
		return playerMeleeMap.get(player);
	}
    
    public static void meleeLobbySequence() {
		if (meleeGameRunning) {
			
			if(checkMeleeEndGame()) {
				endMeleeGame();
				return;
			}
			return;
		}
	
		if (!meleeEventRunning)
			return;
	
		if(waitTimer > 0) { 
			waitTimer--;
			if (waitTimer % 100 == 0 && waitTimer > 0)
				World.sendMessage("<img=10>@blu@[BATTLE ROYALE MELEE] "+waitTimer / 100 +" minutes until Battle Royale starts!" + " Join now ::battle");
		}
		
		if(waitTimer <= 0 && playersInLobby >= 2) {
			if(!meleeGameRunning)
				startMeleeGame();
		}
		
		if(waitTimer <= 0 && playersInLobby < 2 && !meleeGameRunning ) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MELEE]@red@ Not enough players, so the timer has been reset.");
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MELEE]@red@ ::battle to join the lobby.");
			waitTimer = 200;
		}
		
		if(waitTimer == 0) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MELEE] The Event has begun! New contenders, must wait for a new event, or join a secondary or third game.");
		}
	}
    
    public static void rangeLobbySequence() {
		if (rangeGameRunning) {
			
			if(checkRangeEndGame()) {
				endRangeGame();
				return;
			}
			return;
		}
	
		if (!rangeEventRunning)
			return;
	
		if(waitTimer > 0) { 
			waitTimer--;
			if (waitTimer % 100 == 0 && waitTimer > 0)
				World.sendMessage("<img=10>@blu@[BATTLE ROYALE RANGE] "+waitTimer / 100 +" minutes until Battle Royale Range starts!" +" Join now ::rbattle");
		}
		if(waitTimer <= 0 && playersInLobby >= 2) {
			if(!meleeGameRunning)
				startRangeGame();
		}
		if(waitTimer <= 0 && playersInLobby < 2 && !meleeGameRunning ) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE RANGE]@red@ Not enough players, so the timer has been reset.");
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE RANGE]@red@ ::rbattle to join the lobby.");
			waitTimer = 200;
		}
		if(waitTimer == 0) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE RANGE] The Third Event has begun! New contenders, must wait for a new event.");
		}
	}
    
    public static void mageLobbySequence() {
		if (mageGameRunning) {
		 
			if(checkMageEndGame()) {
				endMageGame();
				return;
			}
			return;
		}
	
		if (!mageEventRunning)
			return;
	
		if(waitTimer > 0) { 
			waitTimer--;
			if (waitTimer % 100 == 0 && waitTimer > 0)
				World.sendMessage("<img=10>@blu@[BATTLE ROYALE MAGE] "+waitTimer / 100 +" minutes until Battle Royale Mage starts!" +" Join now ::mbattle");
		}
		if(waitTimer <= 0 && playersInLobby >= 2) {
			if(!meleeGameRunning)
				startMageGame();
		}
		if(waitTimer <= 0 && playersInLobby < 2 && !meleeGameRunning ) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MAGE]@red@ Not enough players, so the timer has been reset.");
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MAGE]@red@ ::mbattle to join the lobby.");
			waitTimer = 200;
		}
		if(waitTimer == 0) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE MAGE] The Battle Royale Mage Event has begun! New contenders, must wait for a new event.");
		}
	}
   
    public static void enterMeleeLobby(Player player) {
		if (!meleeEventRunning) {
			player.sendMessage("There is no game available right now!");
			return;
		} 
		
		if (getState(player) == null) {
			if (checkItems(player)) {
				player.getPA().closeAllWindows();
				playerMeleeMap.put(player, WAITING);
				totalPlayers++;
				playersInLobby++;
				player.inBattleLobby = true;
				player.setSpecialPercentage(100);
		        CombatSpecial.updateBar(player);
		        player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
		        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
		        Position position = new Position(3297, 9824);
		        player.moveTo(position);
		        player.getPA().sendInterfaceRemoval();
		        Autocasting.resetAutocast(player, false);
				player.sendMessage("<img=10>@red@Welcome to Melee Battle Royale!");
			} else {
				player.sendMessage("@red@Bank all your items to play Melee Battle Royale!");
			}
		}

	}
    
    public static void enterRangeLobby(Player player) {
		if (!rangeEventRunning) {
			player.sendMessage("There is no game available right now!");
			return;
		} 
		
		if (getState(player) == null) {
			if (checkItems(player)) {
				player.getPA().closeAllWindows();
				playerRangeMap.put(player, WAITING);
				totalPlayers++;
				playersInLobby++;
				player.inRangeBattleLobby = true;
				player.setSpecialPercentage(100);
		        CombatSpecial.updateBar(player);
		        player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
		        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
		        Position position = new Position(3297, 9824);
		        player.moveTo(position);
		        player.getPA().sendInterfaceRemoval();
		        Autocasting.resetAutocast(player, false);
				player.sendMessage("<img=10>@red@Welcome to Range Battle Royale!");
			} else {
				player.sendMessage("@red@Bank all your items to play Range Battle Royale!");
			}
		}
	}
    
    public static void enterMageLobby(Player player) {
		if (!mageEventRunning) {
			player.sendMessage("There is no game available right now!");
			return;
		} 
		
		if (getState(player) == null) {
			if (checkItems(player)) {
				player.getPA().closeAllWindows();
				playerMageMap.put(player, WAITING);
				totalPlayers++;
				playersInLobby++;
				player.inMageBattleLobby = true;
				player.setSpecialPercentage(100);
		        CombatSpecial.updateBar(player);
		        player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
		        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
		        Position position = new Position(3297, 9824);
		        player.moveTo(position);
		        player.getPA().sendInterfaceRemoval();
		        Autocasting.resetAutocast(player, false);
				player.sendMessage("<img=10>@red@Welcome to Mage Battle Royale!");
			} else {
				player.sendMessage("@red@Bank all your items to play Mage Battle Royale!");
			}
		}
	}
    
    public static void moveToMageArena(Player player) {
		int chance = RandomUtility.exclusiveRandom(5);
		switch(chance) {
		case 0:
			player.moveTo(spawn8);
			break;
		case 1:
			player.moveTo(spawn9);
			break;
		case 2:
			player.moveTo(spawn10);
			break;
		case 3:
			player.moveTo(spawn11);
			break;
		case 4:
			player.moveTo(spawn12);
			break;
		case 5:
			player.moveTo(spawn13);
			break;
		case 6:
			player.moveTo(spawn14);
			break;
		}
		playersInLobby--;
	}
    
    public static void moveToRangeArena(Player player) {
		int chance = RandomUtility.exclusiveRandom(5);
		switch(chance) {
		case 0:
			player.moveTo(spawn15);
			break;
		case 1:
			player.moveTo(spawn16);
			break;
		case 2:
			player.moveTo(spawn17);
			break;
		case 3:
			player.moveTo(spawn18);
			break;
		case 4:
			player.moveTo(spawn19);
			break;
		case 5:
			player.moveTo(spawn20);
			break;
		case 6:
			player.moveTo(spawn21);
			break;
		}
		playersInLobby--;
	}
    
    public static void moveToMeleeArena(Player player) {
		int chance = RandomUtility.exclusiveRandom(5);
		switch(chance) {
		case 0:
			player.moveTo(spawn1);
			break;
		case 1:
			player.moveTo(spawn2);
			break;
		case 2:
			player.moveTo(spawn3);
			break;
		case 3:
			player.moveTo(spawn4);
			break;
		case 4:
			player.moveTo(spawn5);
			break;
		case 5:
			player.moveTo(spawn6);
			break;
		case 6:
			player.moveTo(spawn7);
			break;
		}
		playersInLobby--;
	}
    
    public static void startMeleeGame() {
		for (Player player : playerMeleeMap.keySet()) {
			meleeEventRunning = false;
			meleeGameRunning = true;
			player.getPA().closeAllWindows();
			player.setExperienceLocked(true);
			moveToMeleeArena(player);
			giveGear(player);
			player.inBattleLobby = false;
			player.inBattleRoyale = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
		}
	}
    
    public static void startMageGame() {
		for (Player player : playerMageMap.keySet()) {
			mageEventRunning = false;
			mageGameRunning = true;
			player.getPA().closeAllWindows();
			player.setExperienceLocked(true);
			moveToMageArena(player);
			giveGear(player);
			player.inBattleLobby = false;
			player.inBattleRoyale = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
		}
	}
    
    public static void startRangeGame() {
		for (Player player : playerRangeMap.keySet()) {
			rangeEventRunning = false;
			rangeGameRunning = true;
			player.getPA().closeAllWindows();
			player.setExperienceLocked(true);
			moveToRangeArena(player);
			giveGear(player);
			player.inBattleLobby = false;
			player.inBattleRoyale = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
		}
	}
    
    public static void startMeleeBattle(Player player) {
        gameType = 1;
        startEvent(player);
        meleeEventRunning = true;
    }
    
    public static void startMageBattle(Player player) {
        gameType = 2;
        startEvent(player);
        mageEventRunning = true;
    }
    
    public static void startRangeBattle(Player player) {
        gameType = 2;
        startEvent(player);
        rangeEventRunning = true;
    }

    public static void startEvent(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        lobbyOpened = true;

        if (gameType == 1) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [MELEE BATTLE ROYALE] <col=f0ff00>A Melee Battle Royale event has started by "
            		+ player.getUsername() + " Type ::battle to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [MELEE BATTLE ROYALE RULES] <col=f0ff00>No Overhead Prayers!");
            gameName = "Melee Battle Royale";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::battle");
        }
        
        if (gameType == 2) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [BATTLE ROYALE MAGE] <col=f0ff00>A Mage Battle Royale event has started by "
            		+ player.getUsername() + " Type ::mbattle to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [MAGE BATTLE ROYALE RULES] <col=f0ff00>No Overhead Prayers!");
            gameName = "Mage Battle Royale";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::mbattle");
        }
        
        if (gameType == 3) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [RANGE BATTLE ROYALE] <col=f0ff00>A Range Battle Royale event has started by "
            		+ player.getUsername() + " Type ::rbattle to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [RANGE BATTLE ROYALE RULES] <col=f0ff00>No Overhead Prayers!");
            gameName = "Range Battle Royale";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::rbattle");
        }
    }

    public static void giveGear(Player player) {
        player.previousSpellbook = player.getSpellbook();
		saveOldStats(player);
		player.getSkillManager().newSkillManager();
		updateSkills(player);

        if (gameType == 1) { // Melee
        	player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725, 1));
    		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14021, 1));
    		player.getEquipment().set(Equipment.BODY_SLOT, new Item(15022, 1));
    		player.getEquipment().set(Equipment.LEG_SLOT, new Item(15024, 1));
    		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(15026, 1));
    		player.getEquipment().set(Equipment.FEET_SLOT, new Item(15025, 1));
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
					.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99).setMaxLevel(Skill.PRAYER, 990)
					.setMaxLevel(Skill.CONSTITUTION, 990);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
        }
        if (gameType == 2) { // Mage
        	player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725, 1));
    		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14021, 1));
    		player.getEquipment().set(Equipment.BODY_SLOT, new Item(15022, 1));
    		player.getEquipment().set(Equipment.LEG_SLOT, new Item(15024, 1));
    		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(15026, 1));
    		player.getEquipment().set(Equipment.FEET_SLOT, new Item(15025, 1));
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
					.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99).setMaxLevel(Skill.PRAYER, 990)
					.setMaxLevel(Skill.CONSTITUTION, 990);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
        }
        if (gameType == 3) { // Range
        	player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725, 1));
    		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14021, 1));
    		player.getEquipment().set(Equipment.BODY_SLOT, new Item(15022, 1));
    		player.getEquipment().set(Equipment.LEG_SLOT, new Item(15024, 1));
    		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(15026, 1));
    		player.getEquipment().set(Equipment.FEET_SLOT, new Item(15025, 1));
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
					.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99).setMaxLevel(Skill.PRAYER, 990)
					.setMaxLevel(Skill.CONSTITUTION, 990);
			for (Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
						.setExperience(skill,
								SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
        }
    }

    public static void leaveArena(Player player) {
		player.getInventory().deleteAll();
		player.getEquipment().deleteAll();
		playerMageMap.remove(player);
		playerMeleeMap.remove(player);
		playerRangeMap.remove(player);
		player.inBattleRoyale = false;

		player.moveTo(GameSettings.DEFAULT_POSITION);
		player.getPacketSender().sendInteractionOption("null", 2, true);
		player.setExperienceLocked(false);
		player.sendMessage("@blu@Thanks for participating in the Battle Royale Minigame");
		player.setSpecialPercentage(100);
        CombatSpecial.updateBar(player);
        BonusManager.update(player);
		player.getSkillManager().getSkills().level = player.oldSkillLevels;
		player.getSkillManager().getSkills().experience = player.oldSkillXP;
		player.getSkillManager().getSkills().maxLevel = player.oldSkillMaxLevels;
		updateSkills(player);
	}
    
    public static void removePlayer(Player player) {
		playerMeleeMap.remove(player);
		playerMageMap.remove(player);
		playerRangeMap.remove(player);
	}
    
    public static boolean checkMeleeEndGame() {
		if (meleeGameRunning) {

			if (playerMeleeMap.size() <= 1) {
				return true;
			}
		}
		return false;
	}
    
    public static boolean checkRangeEndGame() {
		if (rangeGameRunning) {

			if (playerRangeMap.size() <= 1) {
				return true;
			}
		}
		return false;
	}
    
    public static boolean checkMageEndGame() {
		if (mageGameRunning) {

			if (playerMageMap.size() <= 1) {
				return true;
			}
		}
		return false;
	}
    
    public static void endMeleeGame() {
		meleeEventRunning = false;
		meleeGameRunning = false;
		for (Player player : playerMeleeMap.keySet()) {
			World.sendMessage(
					"@blu@[MELEE BATTLE ROYALE] @blu@" + Misc.formatPlayerName(player.getUsername()) + "@red@ has won the Melee Battle Royale! 10x Royale Tokens have been awarded!");
			leaveArena(player);
			player.sendMessage("<img=10>@blu@Congratulations, you won!");
			player.addItemToAny(19864, 10);
		} 
		playerMeleeMap.clear();
    }
    
    public static void endMageGame() {
		mageEventRunning = false;
		meleeGameRunning = false;
		for (Player player : playerMageMap.keySet()) {
			World.sendMessage(
					"@blu@[BATTLE ROYALE MAGE] @blu@" + Misc.formatPlayerName(player.getUsername()) + "@red@ has won the Nightmare Royale!");
			leaveArena(player);
			player.sendMessage("<img=10>@blu@Congratulations, you won!");
			player.getInventory().add(19864, 15);
		} 
		playerMageMap.clear();
    }
    
    public static void endRangeGame() {
		rangeEventRunning = false;
		meleeGameRunning = false;
		for (Player player : playerRangeMap.keySet()) {
			World.sendMessage(
					"@blu@[BATTLE ROYALE RANGE] @blu@" + Misc.formatPlayerName(player.getUsername()) + "@red@ has won the Battle Royale!");
			leaveArena(player);
			player.sendMessage("<img=10>@blu@Congratulations, you won!");
			player.getInventory().add(19864, 10);
		} 
		playerRangeMap.clear();
    }

	public static void updateSkills(Player player) {
		player.getSkillManager().updateSkill(Skill.ATTACK);
		player.getSkillManager().updateSkill(Skill.AGILITY);
		player.getSkillManager().updateSkill(Skill.CONSTITUTION);
		player.getSkillManager().updateSkill(Skill.CONSTRUCTION);
		player.getSkillManager().updateSkill(Skill.COOKING);
		player.getSkillManager().updateSkill(Skill.CRAFTING);
		player.getSkillManager().updateSkill(Skill.DEFENCE);
		player.getSkillManager().updateSkill(Skill.FARMING);
		player.getSkillManager().updateSkill(Skill.FIREMAKING);
		player.getSkillManager().updateSkill(Skill.FISHING);
		player.getSkillManager().updateSkill(Skill.FLETCHING);
		player.getSkillManager().updateSkill(Skill.HERBLORE);
		player.getSkillManager().updateSkill(Skill.HUNTER);
		player.getSkillManager().updateSkill(Skill.MAGIC);
		player.getSkillManager().updateSkill(Skill.MINING);
		player.getSkillManager().updateSkill(Skill.PRAYER);
		player.getSkillManager().updateSkill(Skill.RANGED);
		player.getSkillManager().updateSkill(Skill.RUNECRAFTING);
		player.getSkillManager().updateSkill(Skill.SLAYER);
		player.getSkillManager().updateSkill(Skill.SMITHING);
		player.getSkillManager().updateSkill(Skill.STRENGTH);
		player.getSkillManager().updateSkill(Skill.SUMMONING);
		player.getSkillManager().updateSkill(Skill.THIEVING);
		player.getSkillManager().updateSkill(Skill.WOODCUTTING);
	}

	public static void saveOldStats(Player player) {
		SkillManager.Skills currentSkills = player.getSkillManager().getSkills();
		player.oldSkillLevels = currentSkills.level;
		player.oldSkillXP = currentSkills.experience;
		player.oldSkillMaxLevels = currentSkills.maxLevel;
	}
}