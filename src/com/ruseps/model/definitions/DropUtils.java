package com.ruseps.model.definitions;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.container.impl.Equipment;
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
		{2867, 20},//upgrade slayer
		{2869, 15},
		{2859, 15},
		{2858, 15},
		{2857, 15},
		{2856, 15},// slayer upgraded
		{2709, 10},
		{2710, 15},
		{21048, 20}, //Death reaper
		{21049, 20},
		{21050, 20},
		{21051, 20},
		{21052, 20},
		{21053, 20},
		{21054, 20},//Death Reaper
		{ 18957, 65 },//diamond
		{ 18960, 40 },//gold
		{ 18961, 35 },// silver
		{ 18962, 100 },//dragonstone
		{ 18963, 75 },//ruby
		{ 18958, 10 },// bronze
		{ 2821, 325 },
		{ 705, 10 }, //Beta Tester Aura

	};

	private static final int[] COLLITEMS = {
			996, //col Op Amulet
			1002, //col burning Amulet
			19888, //demons collector
			14596,
	};
	
	private static final int[][] DRPETS = {
			{174, 80}, //Ruby Dragon
			{3033, 15}, //heartwrencher 
			{3035, 13}, //hulk 
			{3040, 12},
			{3032, 45}, //jad pet
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
			{5272, 35},
			{963, 30},
			{3000, 35},
	};
	
	private static final int[][] DR_ITEMS_IN_INVENTORY = {
			{5197, 200},
			{ 18957, 65 },//diamond
			{ 18960, 40 },//gold
			{ 18961, 35 },// silver
			{ 18962, 100 },//dragonstone
			{ 18963, 75 },//ruby
			{ 18958, 10 },// bronze
			{ 2821, 325 },


		
	};
	
	public static int drBonus(Player player) {
		int totalBonus = 0;
		Equipment playerEquip = player.getEquipment();
		Familiar playerFamiliar = player.getSummoning().getFamiliar();
		if(player.getDrBoost() > 0) {
			totalBonus += player.getDrBoost();
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
		      totalBonus += 100;
		}
		if(player.getRights() == PlayerRights.PLAYER) {
			totalBonus += 10;
		}
		if(player.getRights() == PlayerRights.OWNER) {
			totalBonus += 80;
		}
		if(player.getRights() == PlayerRights.DEVELOPER) {
			totalBonus = 1000;
		}
		if(player.getRights() == PlayerRights.SUPPORT) {
			totalBonus += 10;
		}
		if(player.getRights() == PlayerRights.VETERAN) {
			totalBonus += 750;
		}
		if(player.getRights() == PlayerRights.MODERATOR) {
			totalBonus += 15;
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			totalBonus += 20;
		}
		if(player.getRights() == PlayerRights.BRONZE_MEMBER) {
			totalBonus += 5;
		}
		if(player.getRights() == PlayerRights.SILVER_MEMBER) {
			totalBonus += 15;
		}
		if(player.getRights() == PlayerRights.GOLD_MEMBER) {
			totalBonus += 30;
		}
		if(player.getRights() == PlayerRights.PLATINUM_MEMBER) {
			totalBonus += 45;
		}
		if(player.getRights() == PlayerRights.DIAMOND_MEMBER) {
			totalBonus += 65;
		}
		if(player.getRights() == PlayerRights.RUBY_MEMBER) {
			totalBonus += 85;
		}
		if(player.getRights() == PlayerRights.DRAGONSTONE_MEMBER) {
			totalBonus += 100;
		}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			totalBonus += 20;
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
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 1033) { // DR FOR WHATEVER
			totalBonus += 13;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 1031) { // DR FOR WHATEVER
			totalBonus += 13;
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
		if(player.getEquipment().get(Equipment.AURA).getId() == 2821) { // DR FOR WHATEVER
			totalBonus += 325;
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
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 19090) { //
			totalBonus += 5;
		}
		if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2869) { // DR Upgraded Slayer
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2858) { // DR Upgraded Slayer
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2859) { //
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2856) { //
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2857) { //
			totalBonus += 15;
		}
		if(player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2867) { //
			totalBonus += 20;
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
		if(player.getInventory().contains(2821)) { // Dragonstone aura inventory
			totalBonus += 325;
		}
		if(player.getInventory().contains(744)) { // silver aura inventory
			totalBonus += 5;
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
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3510) {
			totalBonus += 150;
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
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2799) { // Crazy Set
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 2800) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 2801) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 2803) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2802) {
			totalBonus += 20;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 13026) { // MDMA set
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 13024) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 13025) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 13028) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 13027) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13029) {
			totalBonus += 15;
		}
		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 13017) { // diamond donor set
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 13018) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 13019) {
			totalBonus += 30;
		}
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2804) {
			totalBonus += 20;
		}
		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() ==
				(176)) {
			totalBonus += 60; // diamond dragon
		}
		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() ==
				(3034)) {
			totalBonus += 50; // diamond dragon
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
		return totalBonus;
	}
	
	public static boolean hasCollItemEquipped(Player player) {
		for(int itemId : COLLITEMS)
			if(player.getEquipment().contains(itemId))
				return true;
		
		return false;
	}
}