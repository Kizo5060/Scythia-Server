package com.ruseps.model.definitions;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.skill.impl.summoning.Familiar;
import com.ruseps.world.entity.impl.player.Player;

public class DropUtils {

    private static final int[][] DRITEMS = {
		{ 2572, 5 }, // Row
		{ 996, 25 }, // op amulet
		{ 965, 25 }, // op ring
		{ 1007, 25 }, // op Boots
		{ 11142, 15 }, // Spirit Cape
		{ 11143, 15 }, // Darklord Cape
		{ 1035, 13 }, // Karamaja Gloves 4
		{ 1033, 13 }, // RoseBlood Gloves
		{ 1031, 13 }, // Ringmaster Boots
		{ 11144, 15 }, // Ring of Wealth
		{ 11145, 15 }, // Ring of Devotion
		{ 11146, 15 }, // kingship ring
		{ 902, 10 }, // Mermaid
		{ 903, 10 }, // Mermaid
		{ 904, 10 }, // Mermaid
		{ 905, 10 }, // Mermaid
		{ 906, 10 }, // Mermaid
		{ 1019, 17 }, //
		{ 1029, 17 }, //
		{ 1021, 17 }, //
		{ 1025, 17 }, // Goku Pieces
		{ 1023, 17 }, //
		{ 1027, 17 }, //
		{ 19970, 5 }, //
		{ 19972, 5 },
		{ 19974, 5 }, //fate justiticar helm
		{ 19976, 5 }, //fate justiticar plate
		{ 19978, 5 }, //fate justiticar legs
		{ 1413, 50 }, //OWNER CAPE
		{ 19005, 100 }, //NexArch Owner Head
		{ 19002, 100 }, //NexArch Owner Body
		{ 19003, 100 }, //NexArch Owner Trousers
		{ 19004, 100 }, //NexArch Owner Gloves
		{ 19016, 100 }, //NexArch Owner Boots
		{ 19073, 5 },
		{ 19074, 5 },
		{ 19075, 5 },
		{ 19076, 15 },
		{ 897, 5 },
		{ 898, 5 },
		{ 899, 5 },
		{ 900, 5 },
		{ 901, 5 },
		{ 19086, 50 },
		{ 19087, 5 },
		{ 19088, 5 },
		{ 19089, 5 },
		{ 19090, 5 },
		{ 19091, 5 },
		{ 19092, 10 },
		{1002, 15},
		{964, 25},
		{2765, 20},//plat armour
		{2766, 20},
		{2767, 20},//plat armour
		{2867, 5},//upgrade slayer
		{2869, 5},
		{2859, 5},
		{2858, 5},
		{2857, 5},
		{2856, 5},// slayer upgraded
		{2709, 10},
		{2710, 15},
		{13026, 10}, //Master of Slayer
		{13024, 10}, //Master of Slayer
		{13025, 10}, //Master of Slayer
		{13028, 10}, //Master of Slayer
		{13027, 10}, //Master of Slayer
		{13029, 10}, //Master of Slayer
		{2728, 75}, //Slayer Owner Wings
		{13034, 20}, //Mystic Star
		{13030, 20}, //Mystic Star
		{13031, 20}, //Mystic Star
		{13032, 20}, //Mystic Star
		{13033, 20}, //Mystic Star
		{13035, 20}, //Mystic Star
		{8676, 20}, //Meliodas
		{8680, 20}, //Meliodas
		{8678, 20}, //Meliodas
		{8672, 20}, //Meliodas
		{8674, 20}, //Meliodas
		{8668, 20}, //Meliodas
		{2799, 25}, //Princess No Fuck
		{2800, 25}, //Princess No Fuck
		{2801, 25}, //Princess No Fuck
		{2803, 25}, //Princess No Fuck
		{2802, 25}, //Princess No Fuck
		{21048, 20}, //Death reaper
		{21049, 20},
		{21050, 20},
		{21051, 20},
		{21052, 20},
		{21053, 20},
		{21054, 20},//Death Reaper
		{ 19073, 25 },//Nitro Helm
		{ 19074, 25 },//Nitro Body
		{ 19075, 25 },//Nitro Legs
		{ 19081, 25 },//Nitro Gloves
		{ 19076, 25 },//Nitro Aura
		{ 18957, 65 },//diamond aura
		{ 18960, 40 },//gold aura
		{ 18961, 35 },// silver aura
		{ 18962, 100 },//dragonstone aura
		{ 18963, 75 },//ruby aura
		{ 18958, 10 },// bronze aura
		{ 2821, 325 },//rainbow aura
		{ 705, 10 }, //Beta Tester Aura

	};

	private static final int[] COLLITEMS = {
			996, //col Op Amulet
			1002, //col burning Amulet
			19888, //demons collector
			14596, //youtuber amulet
			20657, //tribrid amulet
	};
	
	private static final int[][] DRPETS = {
			{174, 125}, //Ruby Dragon
			{3033, 15}, //chaos ele
			{3040, 12},
			{3032, 45}, //jad pet
			{3034, 65}, //corp pet
			{4276, 12},
			{5279, 20},
			{3664, 25},
			{1002, 15},
			{996, 25},
			{3060, 5},
			{3059, 5},
			{3057, 8},
			{3054, 10},
			{3053, 14},
			{3052, 18},
			{3040, 20},
			{3050, 22},
			{3033, 25},
			{3047, 5},
			{5089, 5},
			{87, 10},
			{5277, 15},
			{5281, 15},
			{5278, 20},
			{5276, 20},
			{5051, 25},
			{4263, 30},
			{3058, 5},
			{3016, 35},
			{5053, 40},
			{465, 35},
			{3035, 40}, //kree pet
			{3037, 25}, //commander pet
			{3036, 55}, //k'ril pet
			{5272, 65},
			{1042, 85 }, //Blue eyes
			{13021, 85 }, //Blue eyes
			{10002, 75}, //Pinata Pet
			{10001, 75}, //2x Pet
			{963, 30},
			{708, 85}, //DS zone Pet
			{703, 50}, //SHARKY
			{709, 70}, //Ruby zone Pet
			{1771, 35}, //gold raids pet
			{5431, 70 }, //donator boss pet
			{6731, 80}, //Slayer Pet
			{3000, 100}, //Mini Zeus Pet
			{182, 120}, //Baby Yoda Pet
	};
	
	private static final int[][] DR_ITEMS_IN_INVENTORY = {

			{ 18957, 65 },//diamond
			{ 18960, 40 },//gold
			{ 18961, 35 },// silver
			{ 18962, 100 },//dragonstone
			{ 18963, 75 },//ruby
			{ 18958, 10 },// bronze
			{ 2821, 325 },//rainbow

		
	};
	
	private static final int[][] DR_ITEMS_IN_COSMETIC = { // ( ITEM_ID, DR_BONUS, SLAYER_BONUS,DAMAGEBONUS}
			
			{ 19073, 20 },//Nitro Helm
			{ 19074, 20 },//Nitro Body
			{ 19075, 20 },//Nitro Legs
			{ 19081, 20 },//Nitro Gloves
			{ 19079, 20 },//Nitro boots
			{ 19076, 20 },//Nitro Aura
			{ 910, 20 },//Nitro Cape
			{ 11724, 15 },//Bandos Chest
			{ 11726, 15 },//Bandos Tasset
			{ 11718, 15 },//Armadyl Helm
			{ 11720, 15 },//Armadyl Chest
			{ 11722, 15 },//Armadyl Legs
			{ 3486, 15 },//Gilded Helm
			{ 3481, 15 },//Gilded Chest
			{ 3483, 15 },//Gilded Legs
			{ 3485, 15 },//Gilded Skirt
			{ 19341, 15 },//Dragon Helm
			{ 19342, 15 },//Dragon Chest
			{ 19343, 15 },//Dragon Legs
			{ 13462, 15 },//Dragon Boot
			{ 10350, 15 },//3rd Age Helm
			{ 10348, 15 },//3rd Age Chest
			{ 10346, 15 },//3rd Age Legs
			{ 19314, 15 },//3rd Age Shield
			{ 19317, 15 },//3rd Age Helm
			{ 19320, 15 },//3rd Age Chest
			{ 19308, 15 },//3rd Age Legs
			{ 19311, 15 },//3rd Age Shield
			{ 911, 50},//Dragonstone Master Cape
			{ 8465, 30},//Demonio Gloves
			{ 8467, 30, 5},//Demonio Boots
			{ 12278, 30, 5},//Demonio Legs
			{ 12279, 30, 5},//Demonio Body
			{ 12282, 30, 5},//Demonio Helm
			{ 19029, 20},//GFreeza Gloves
			{ 909, 20},//GFreeza Boots
			{ 19028, 15},//GFreeza Legs
			{ 19027, 15},//GFreeza Body
			{ 19026, 15},//GFreeza Helm
			{ 19025, 15},//Freeza Glaive
			{ 10481, 5 },//Ironman Clan Helm
			{ 913, 5 },//Ironman Clan Body
			{ 19024, 5 },//Ironman Clan Legs
			{ 914, 5 },//Ironman ClanGloves
			{ 10483, 5 },//Ironman Clan boots
			{ 916, 5 },//Ironman Clan Cape
			{ 2765, 5},//Plat Donor Helm
			{ 2766, 5},//Plat Donor Body
			{ 2767, 5},//Plat Donor Legs
			{ 13017, 10},//Diamond Donor Helm
			{ 13018, 10},//Diamond Donor Body
			{ 13019, 10},//Diamond Donor Legs
			{ 13012, 5},//Galaxy Donor Helm
			{ 13010, 5},//Galaxy Donor Body
			{ 13011, 5},//Galaxy Donor Legs
			{ 13014, 5},//Galaxy Donor Boots
			{ 13013, 5},//Galaxy Donor Gloves
			{ 18902, 25},//Hallow Ichigo Helm
			{ 18904, 25},//Hallow Ichigo Body
			{ 14010, 25},//Hallow Ichigo Legs
			{ 20002, 25},//Hallow Ichigo Boots
			{ 19111, 25},//Hallow Wings
			{ 7028, 25},//Hallow Ichigo Gloves
			{ 20531, 15},//Zangetsu
			{ 18906, 10},//Destro God Body
			{ 10907, 10},//Destro God Helm
			{ 10908, 10},//Destro God Legs
			{ 10906, 10},//Destro God Gloves
			{ 10905, 10},//Destro God Boots
			{ 10946, 10},//Beru's Bow
			{ 1413, 50},//Owner Cape
			{ 11665, 10},//Void Me Helm
			{ 11664, 10},//Vooid R Helm
			{ 11663, 10},//Void Ma helm
			{ 8839, 10},//Void top
			{ 8840, 10},//Void legs
			{ 19785, 15},//Elite V top
			{ 19786, 15},//Elite V bot
			{ 8842, 10},//Void Gloves
			{ 19712, 10},//Void Shield
			{ 19780, 10},//Void Sword
			{ 12437, 50, 10, 3},//Extra Hands
			
    }; 
	
	public static int drBonus(Player player) {
		int totalBonus = 0;
	    Equipment playerEquip = player.getEquipment();
	    Familiar playerFamiliar = player.getSummoning().getFamiliar();
		if(player.getDrBoost() > 0) {
			totalBonus += ((player.getDrBoost()> 1500) ? 1500 : player.getDrBoost());
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 21049) { //death reaper
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 21050) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 21051) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 21052) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 21053) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 21054) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21048) {
			totalBonus += 20;
		}
		if(player.getUsername() == "elegance") {
		      totalBonus += 1;
		}
		if(player.getRights() == PlayerRights.PLAYER) {
			totalBonus += 10;
		}
		if(player.getRights() == PlayerRights.OWNER) {
			totalBonus += 1000;
		}
		if(player.getRights() == PlayerRights.DEVELOPER) {
			totalBonus += 1000;
		}
		if(player.getRights() == PlayerRights.SUPPORT) {
			totalBonus += 100;
		}
		if(player.getRights() == PlayerRights.YOUTUBER) {
			totalBonus += 750;
		}
		if(player.getRights() == PlayerRights.MODERATOR) {
			totalBonus += 150;
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			totalBonus += 250;
		}
		if(player.getRights() == PlayerRights.BRONZE_MEMBER) {
			totalBonus += 15;
		}
		if(player.getRights() == PlayerRights.SILVER_MEMBER) {
			totalBonus += 25;
		}
		if(player.getRights() == PlayerRights.GOLD_MEMBER) {
			totalBonus += 50;
		}
		if(player.getRights() == PlayerRights.PLATINUM_MEMBER) {
			totalBonus += 70;
		}
		if(player.getRights() == PlayerRights.DIAMOND_MEMBER) {
			totalBonus += 90;
		}
		if(player.getRights() == PlayerRights.RUBY_MEMBER) {
			totalBonus += 120;
		}
		if(player.getRights() == PlayerRights.DRAGONSTONE_MEMBER) {
			totalBonus += 150;
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			totalBonus += 500;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 1007) { // DR FOR WHATEVER
			totalBonus += 25;
		}
		if(player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 996) { // DR FOR WHATEVER
			totalBonus += 25;
		}
		if(player.getEquipment().get(Equipment.RING_SLOT).getId() == 965) { // DR FOR WHATEVER
			totalBonus += 25;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 1035) { // DR FOR WHATEVER
			totalBonus += 13;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 19005) { // DR FOR WHATEVER
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 1033) { // DR FOR WHATEVER
			totalBonus += 13;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 19003) { // DR FOR WHATEVER
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 1031) { // DR FOR WHATEVER
			totalBonus += 13;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 19002) { // DR FOR WHATEVER
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 964) { // DR FOR WHATEVER
			totalBonus += 25;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18958) { // DR FOR WHATEVER
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18961) { // DR FOR WHATEVER
			totalBonus += 35;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18960) { // DR FOR WHATEVER
			totalBonus += 40;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18957) { // DR FOR WHATEVER
			totalBonus += 65;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18963) { // DR FOR WHATEVER
			totalBonus += 75;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 18962) { // DR FOR WHATEVER
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 2821) { // Rainbow Aura
			totalBonus += 325;
		}
		if(player.getEquipment().get(Equipment.AURA).getId() == 19076) { // Nitro Aura
			totalBonus += 25;	
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 19087) { // DR FOR WHATEVER
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 19089) { // DR FOR WHATEVER
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 19088) { // DR FOR WHATEVER
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 19091) { // DR FOR WHATEVER
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 19016) { // DR FOR WHATEVER
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 19090) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 19004) { //
			totalBonus += 100;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2869) { // DR Upgraded Slayer
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2858) { // DR Upgraded Slayer
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2859) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2856) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2857) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 13024) { // DR Master of Slayer
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 13026) { // DDR Master of Slayer
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 13025) { // DR Master of Slayer
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 13028) { // DR Master of Slayer
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 13027) { // DR Master of Slayer
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13029) { // DR Master of Slayer
			totalBonus += 10;	
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 3502) { // Superior Magic Start
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 3501) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 3503) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 3504) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 3500) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 3499) { // Superior Magic End
			totalBonus += 10;	
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 21045) { // Of the Gods Start
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 21043) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 21044) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 21041) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 21042) { 
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21047) { // Of the Gods End
			totalBonus += 10;	
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 19031) { // DR Slayer God
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 19030) { // DDR Slayer God
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 19032) { // DR Slayer God
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 19034) { // DR Slayer God
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 19033) { // DR Slayer God
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10598) { // DR Slayer God
			totalBonus += 20;		
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2867) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.RING_SLOT).getId() == 2709) { // god row ring
			totalBonus += 10;
		}
		if(player.getEquipment().get(Equipment.RING_SLOT).getId() == 2710) { // God (i)
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 1002) { // DR FOR WHATEVER
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2766) { // Plat Armour
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2765) { //
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 19073) { //
			totalBonus += 25;
	    }
		if (playerEquip.get(Equipment.BODY_SLOT).getId() == 19074) {
		        totalBonus += 25;
		}
		if (playerEquip.get(Equipment.LEG_SLOT).getId() == 19075) {
		        totalBonus += 25;
		}
		if (playerEquip.get(Equipment.FEET_SLOT).getId() == 19079) {
		        totalBonus += 25;
		}
		if (playerEquip.get(Equipment.HANDS_SLOT).getId() == 19081) {
		        totalBonus += 25;
		}
		if (playerEquip.get(Equipment.CAPE_SLOT).getId() == 910) {
	        totalBonus += 25;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2767) { //
			totalBonus += 20;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 900) { // bikini
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 897) { // bikini
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 898) { // bikini
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 901) { // bikini
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 899) { // bikini
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 1413) { // OWNERCAPE
			totalBonus += 50;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2728) { // Slayer Owner cape
			totalBonus += 75;	
		}
		if(player.getInventory().contains(18958)) { // Bronze aura inventory
			totalBonus += 10;
		}
		if(player.getInventory().contains(18961)) { // silver aura inventory
			totalBonus += 35;
		}
		if(player.getInventory().contains(18960)) { // gold aura inventory
			totalBonus += 40;
		}
		if(player.getInventory().contains(18957)) { // diamond aura inventory
			totalBonus += 65;
		}
		if(player.getInventory().contains(18963)) { // ruby aura inventory
			totalBonus += 75;
		}
		if(player.getInventory().contains(18962)) { // Dragonstone aura inventory
			totalBonus += 100;
		}
		if(player.getInventory().contains(2821)) { // Rainbow aura inventory
			totalBonus += 325;
		}
		if(player.getInventory().contains(12502)) { // Bonus DR Scroll t1
			totalBonus += 10;
		}
		if(player.getInventory().contains(1503)) { // Bonus DR Scroll t2
			totalBonus += 25;
		}
		if(player.getInventory().contains(15671)) { // Bonus DR Scroll t3
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 904) { // mermaid
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 902) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 903) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 906) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 905) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.RING_SLOT).getId() == 2572) { //row
			totalBonus += 5;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2724) { // mermaid
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2725) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2726) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2729) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2727) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 1667) { // Crazy Set
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 1015) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 1686) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2894) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 1009) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 1666) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 13034) { // Mystic Star
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 13030) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 13031) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 13032) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 13033) {
			totalBonus += 20;
		}	
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13035) {
			totalBonus += 20;	
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2812) { // Range Master
			totalBonus += 35;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2818) {
			totalBonus += 35;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2814) {
			totalBonus += 35;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2816) {
			totalBonus += 35;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2815) {
			totalBonus += 35;
		}	
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2817) {
			totalBonus += 35;	
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 1667) { // Crazy
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 1015) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 1686) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 1009) {
			totalBonus += 20;
		}	
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 1666) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 3505) { // Razer Ranger
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 3506) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 3507) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 3508) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 3509) {
			totalBonus += 10;
		}	
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 8676) { // Meliodas
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 8680) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 8678) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 8672) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 8674) {
			totalBonus += 20;
		}	
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 8668) {
			totalBonus += 20;	
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 1050) { // SANTA
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 14595) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 14603) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 14605) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 14602) {
			totalBonus += 10;
		}	
		
		if (WellOfGoodwill.isActive()) {
            totalBonus += 50;	
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3510) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8670) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13023) {
			totalBonus += 100;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13022) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 7083) {
			totalBonus += 100;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19012) {
			totalBonus += 100;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19078) {
			totalBonus += 100;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2680) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 1013) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19035) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3525) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 3526) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.AURA).getId() == 2730) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2799) { // Princess No Fuck
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2800) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2801) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2803) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2802) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2724) { // Soul Master
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2725) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2726) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2729) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2727) {
			totalBonus += 50;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 13017) { // diamond donor set
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 13018) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 13019) {
			totalBonus += 10;
		}
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2804) {
			totalBonus += 30;
		}
		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() ==
				(176)) {
			totalBonus += 60; // diamond dragon
			
		}
		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() ==
				(19)) {
			totalBonus += 50; // Pinata Pet
		}

		switch (player.getRights()) {
		default:
			break;
		}
		
		if (playerFamiliar != null) {
			for(int[] pet : DRPETS)
				if(playerFamiliar.getSummonNpc().getId() == pet[0])
					totalBonus += pet[1];
		}

		if(player.isDoubleRateActive())
			totalBonus += 100;
		
		for(int[] cosmetic : DR_ITEMS_IN_COSMETIC) {
            for(int i = 0; i < 14; i++) {
                if(player.getCosmeticEquipment()[i] == cosmetic[0]) {
                    totalBonus += cosmetic[1];

                }
            }
        }
		return totalBonus;
	}
	
	public static boolean hasCollItemEquipped(Player player) {
		for(int itemId : COLLITEMS)
			if(player.getEquipment().contains(itemId))
				return true;
		
		return false;
	}
	
}