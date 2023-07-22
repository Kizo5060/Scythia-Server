package com.ruseps.world.content.gun_game;

import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.GameSettings;
import com.ruseps.model.Area;
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
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.entity.impl.player.Player;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GunGame {
	
	private Area GunGameArea;
	
	private HashMap<Player, Integer> playerScores;

	private final int[] items = {
			20061, // abyssal vine whip
			18349, // C Rapier
			11730, // sara sword
			80, // arma gs
			19994, // zaros gs
	};

	
	public GunGame(int idx) {
		//super(idx);
		this.playerScores = new HashMap<Player, Integer>();
		this.GunGameArea = new Area(new Position(2467, 9673, idx * 4), new Position(2476, 9682, idx * 4));
	}
	
	public static int totalPlayers = 0;
	public static int playersInLobby = 0;
	public static boolean meleeEventRunning = false;
	public static boolean meleeGameRunning = false;
	public static boolean inBattleRoyale = false;
	public static Map<Player, String> playerMeleeMap = new HashMap<Player, String>();
	public static Map<Player, String> playersInMeleeGame = new HashMap<Player, String>();
	
	private static int waitTimer = 200;
	public static final String PLAYING = "PLAYING";
	public static final String WAITING = "WAITING";
	/** REGULAR SPAWNS **/
	public static Position spawn1 = new Position(3321, 9846, 0);
	public static Position spawn2 = new Position(3321, 9850, 0);
	public static Position spawn3 = new Position(3305, 9847, 0);
	public static Position spawn4 = new Position(3309, 9828, 0);
	public static Position spawn5 = new Position(3319, 9829, 0);
	public static Position spawn6 = new Position(3322, 9844, 0);
	public static Position spawn7 = new Position(3316, 9851, 0);
	
    public static LinkedList<Player> ffaPlayers = new LinkedList<>();
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
				World.sendMessage("<img=10>@blu@[BATTLE ROYALE] "+waitTimer / 100 +" minutes until Battle Royale starts!" + " Join now ::battle");
		}
		
		if(waitTimer <= 0 && playersInLobby >= 2) {
			if(!meleeGameRunning)
				startMeleeGame();
		}
		
		if(waitTimer <= 0 && playersInLobby < 2 && !meleeGameRunning ) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE]@red@ Not enough players, so the timer has been reset.");
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE]@red@ ::battle to join the lobby.");
			waitTimer = 200;
		}
		
		if(waitTimer == 0) {
			World.sendMessage("<img=10>@blu@[BATTLE ROYALE] The Event has begun! New contenders, must wait for a new event, or join a secondary or third game.");
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
				player.sendMessage("<img=10>@red@Welcome to Battle Royale!");
			} else {
				player.sendMessage("@red@Bank all your items to play Battle Royale!");
			}
		}

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
    
    public static void startMeleeBattle(Player player) {
        gameType = 1;
        startEvent(player);
        meleeEventRunning = true;
    }

    public static void startEvent(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        lobbyOpened = true;

        if (gameType == 1) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ BATTLE ROYALE ] <col=f0ff00>A Battle Royale event has started by "
            		+ player.getUsername() + " Type ::battle to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ BATTLE ROYALE RULES ] <col=f0ff00>No Overhead Prayers!");
            gameName = "Battle";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::battle");
        }
        
        if (gameType == 2) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ NIGHTMARE ROYALE ] <col=f0ff00>A Nightmare Battle Royale event has started by "
            		+ player.getUsername() + " Type ::nightmare to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ NIGHTMARE ROYALE RULES ] <col=f0ff00>No Overhead Prayers!");
            gameName = "Battle";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::nightmare");
        }
        
        if (gameType == 3) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ BATTLE ROYALE ] <col=f0ff00>A Third Battle Royale event has started by "
            		+ player.getUsername() + " Type ::battle to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ BATTLE ROYALE RULES ] <col=f0ff00>No Overhead Prayers!");
            gameName = "Battle";
            player.getPacketSender().sendMessage("<img=10>@blu@Please use the Portal to enter the Lobby or type ::battle3");
        }
    }

    public static void giveGear(Player player) {
        player.previousSpellbook = player.getSpellbook();
        if (gameType == 1) { // Melee
        	player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725, 1));
    		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(14021, 1));
    		player.getEquipment().set(Equipment.BODY_SLOT, new Item(15022, 1));
    		player.getEquipment().set(Equipment.LEG_SLOT, new Item(15024, 1));
    		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(15026, 1));
    		player.getEquipment().set(Equipment.FEET_SLOT, new Item(15025, 1));
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
		playerMeleeMap.remove(player);
		player.inBattleRoyale = false;
		player.moveTo(GameSettings.DEFAULT_POSITION);
		player.getPacketSender().sendInteractionOption("null", 2, true);
		player.setExperienceLocked(false);
		player.sendMessage("@blu@Thanks for participating in the Battle Royale Minigame");
		player.setSpecialPercentage(100);
        CombatSpecial.updateBar(player);
        player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
	}
    
    public static void removePlayer(Player player) {
		playerMeleeMap.remove(player);
	}
    
    public static boolean checkMeleeEndGame() {
		if (meleeGameRunning) {

			if (playerMeleeMap.size() <= 1) {
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
					"@blu@[BATTLE ROYALE] @blu@" + Misc.formatPlayerName(player.getUsername()) + "@red@ has won the Battle Royale!");
			leaveArena(player);
			player.sendMessage("<img=10>@blu@Congratulations, you won!");
			player.getInventory().add(19864, 10);
		} 
		playerMeleeMap.clear();
    }
    
}
