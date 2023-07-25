package com.ruseps.world.content.skill.impl.summoning;

import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class BossPets {

	public enum BossPet {
		
		X2_KC_PET(10001, 10001, 669),
		PET_FOGGY_BOI(3000, 3000, 667),
		PET_CHAOS_ELEMENTAL(3200, 3033, 11995),
		PET_KING_BLACK_DRAGON(50, 3030, 11996,false, false),
		BEGINNER_DIGIMON(5049, 5089, 11896,false, false),
		PET_VEGETA(4600, 5051, 19868,false, false),
		PET_HELI(87, 87, 1685,false, false),
		BABY(182, 182, 19039,false, false),
		LAVA(1514, 1514, 2703,false, false),
		WHITE(1042, 1042, 13021,false, false),
		VADER(466, 465, 2542,false, false),
		EMPEROR(224, 5272, 2546,false, false),
		EVIL_SAYIAN(3664, 5052, 11948,false, false),
		RDRAGON(175, 174, 18981,false, false),
		DDRAGON(163, 176, 18980,false, false),
		PET_TRUMP(3016, 5053, 11949,false,false),
		PET_YODA(4271, 5277, 18985,false,false),
		PET_THANOS(4263, 5279, 18984,false,false),
		PET_CHARIZARD(4981, 5276, 18987,false,false),
		PET_SQUIRTLE(5274, 5278, 18989,false,false),
		PET_PIKACHU(89, 5281, 18986,false,false),
		PET_GENERAL_GRAARDOR(6260, 3031, 11997),
		PET_TZTOK_JAD(2745, 3032, 11978,false,false),
		PET_CORPOREAL_BEAST(8133, 3034, 12001),
		PET_KREE_ARRA(6222, 3035, 12002),
		PET_KRIL_TSUTSAROTH(6203, 3036, 12003),
		PET_COMMANDER_ZILYANA(6247, 3037, 12004),
		PET_DAGANNOTH_SUPREME(2881, 3038, 12005),
		PET_DAGANNOTH_PRIME(2882, 3039, 12006),
		PET_DAGANNOTH_REX(2883, 3040, 11990),
		PET_FROST_DRAGON(51, 3047, 11991),
		PET_TORMENTED_DEMON(8349, 3048, 11992, false, false),
		PET_KALPHITE_QUEEN(1158, 3050, 11993),
		PET_SLASH_BASH(2060, 3051, 11994),
		PET_PHOENIX(8549, 3052, 11989),
		PET_BANDOS_AVATAR(4540, 3053, 11988),
		PET_NEX(13447, 3054, 11987),
		PET_JUNGLE_STRYKEWYRM(9467, 3055, 11986),
		PET_DESERT_STRYKEWYRM(9465, 3056, 11985),
		PET_ICE_STRYKEWYRM(9463, 3057, 11984),
		PET_GREEN_DRAGON(941, 3058, 11983),
		PET_BABY_BLUE_DRAGON(52, 3059, 11982),
		PET_BLUE_DRAGON(55, 3060, 11981),
		PET_BLACK_DRAGON(54, 3061, 11979),
		PET_SCORPIA(2001, 3063, 11975),
		PET_SKOTIZO(7286, 3064, 11967),
		PET_WILDYWRYM(3334, 3066, 11970),
		PET_BORK(7134, 3067, 11971),
		PET_BARRELCHEST(5666, 3068, 11972),
		PET_SIRE(5886, 3070, 11973),
		PET_ROCK(1265, 3069, 11974),
		PET_LIZADMAN(6766, 3065, 11969),
		ROCK_GOLEM(-1, 6723, 13321),
		HERON(-1, 6724, 13320),
		BEAVER(-1, 6726, 13322),
		TANGLEROOT(-1, 6727, 13323),
		ROCKY(-1, 6728, 13324),
		SQUIRREL(-1, 6729, 13325),
		RIFT_GUARDIAN(-1, 6730, 13326),
		HELLPUPY(963, 963, 13247),
		KRAKEN(6640, 6640, 12655),
		BABY_MOLE(5781, 5781, 12646),
		OLMLET(-1, 6731, 13327),
		PET_VENENATIS(2000, 3062, 11976);
		
		
		public final int npcId;
		private final int spawnNpcId; 
		private final int itemId;
		private boolean isPet;
		private boolean isHealing;


		
		BossPet(int npcId, int spawnNpcId, int itemId) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
			this.isPet = true;
			this.isHealing = true;
		}


		BossPet(int npcId, int spawnNpcId, int itemId,boolean isPet,boolean isHealing) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
			this.isPet = isPet;
			this.isHealing = isHealing;
		}


		
		public static BossPet forId(int itemId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getItemId() == itemId) {
					return p;
				}
			}
			return null;
		}
		
		public static BossPet forSpawnId(int spawnNpcId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getSpawnNpcId() == spawnNpcId) {
					return p;
				}
			}
			return null;
		}


		public boolean isCanAttack() {
			return isPet;
		}
		
		public boolean isCanHeal() {
			return isHealing;
		}

		public int getSpawnNpcId() {
			return spawnNpcId;
		}

		public int getItemId() {
			return itemId;
		}
	}
	
	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if(pet != null) {
			if(player.getSummoning().getFamiliar() != null && player.getSummoning().getFamiliar().getSummonNpc() != null && player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
				if(player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.getSpawnNpcId() && player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
					if(player.getInventory().getFreeSlots() <= 0) {
						player.sendMessage("Please first clear up some space in your inventory :");
						return false;
					}
					player.getSummoning().unsummon(true, true);
					player.getPacketSender().sendMessage("You attempt to pick up your pet...");
					return true;
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			} else {
				player.getPacketSender().sendMessage("This is not your pet to pick up.");
			}
		}
		return false;
	}
	
	public static void onLogout(Player player) 
	{
	    try 
	    {
	        if (player.getSummoning().getFamiliar() != null 
	            && player.getSummoning().getFamiliar().getSummonNpc() != null 
	            && player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) 
	        {
	            pickup(player, player.getSummoning().getFamiliar().getSummonNpc());
	        }
	    } catch (Exception e) {
	    	
	        System.out.println("Error in BossPets.java --> onLogout(Player, player) { " + e);
	    }
	}
}
