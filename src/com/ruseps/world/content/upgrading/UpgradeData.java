/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruseps.world.content.upgrading;

import com.ruseps.model.Item;

/**
 *
 * @author Infinity 28/11/2019
 * Copyright Megascape.net
 */
public enum UpgradeData {
    /*
    Starting item   Rewarded Item    Chance x/100     Coins Required
    */
		EXOTIC_UNUPGRADED(new Item(20134, 1), new Item(20146, 1), 15, 300),
		ORANTEKATANA(new Item(14018, 1), new Item(11658, 1), 15, 100),
		RANGETANKBODY(new Item(5079, 1), new Item(11208, 1), 25, 400),
		RANGETANKLEGS(new Item(5080, 1), new Item(10824, 1), 25, 400),
		RANGETANKHELM(new Item(5081, 1), new Item(11206, 1), 25, 400),
		REDTROOPERHELM(new Item(11071, 1), new Item(2547, 1), 13, 400),
		REDTROOPERBODY(new Item(11067, 1), new Item(2548, 1), 13, 400),
		REDTROOPERLEGS(new Item(11068, 1), new Item(2549, 1), 13, 400),
		REDTROOPERBOOTS(new Item(11087, 1), new Item(2544, 1), 13, 400),
		SCHOOLGIRLHELM(new Item(894, 1), new Item(900, 1), 15, 200),
		SCHOOLGIRLBODY(new Item(798, 1), new Item(897, 1), 15, 200),
		SCHOOLGIRLLEGS(new Item(895, 1), new Item(898, 1), 15, 200),
		SCHOOLGIRLGLOVES(new Item(799, 1), new Item(899, 1), 15, 200),
		SCHOOLGIRLBOOTS(new Item(896, 1), new Item(901, 1), 15, 200),
		SCALEY1(new Item(18968, 1), new Item(18973, 1), 25, 500),
		SCALEY12(new Item(18969, 1), new Item(18972, 1), 25, 500),
		SCALEY13(new Item(18970, 1), new Item(18971, 1), 25, 500),
		SCALEY21(new Item(18973, 1), new Item(18974, 1), 15, 700),
		SCALEY22(new Item(18972, 1), new Item(18975, 1), 15, 700),
		SCALEY23(new Item(18971, 1), new Item(18976, 1), 15, 700),
		SCALEY31(new Item(18974, 1), new Item(18979, 1), 10, 800),
		SCALEY32(new Item(18975, 1), new Item(18978, 1), 10, 800),
		SCALEY33(new Item(18976, 1), new Item(18977, 1), 10, 800),
		BDRAGON(new Item(18980, 1), new Item(18981, 1), 10, 1000),
		LIGHTNINGH(new Item(938, 1), new Item(18991, 1), 10, 500),
		DOOMED1(new Item(19048, 1), new Item(19056, 1), 25, 450),
		DOOMED2(new Item(19049, 1), new Item(19057, 1), 25, 450),
		DOOMED3(new Item(19050, 1), new Item(19058, 1), 25, 450),
		DOOMED4(new Item(19051, 1), new Item(19059, 1), 25, 450),
		DOOMED5(new Item(19052, 1), new Item(19060, 1), 25, 450),
		DOOMED6(new Item(19054, 1), new Item(19061, 1), 25, 450),
		DOOMED7(new Item(19055, 1), new Item(19062, 1), 25, 450),
		SLAYER1(new Item(19087, 1), new Item(2858, 1), 35, 550),
		SLAYER2(new Item(19088, 1), new Item(2859, 1), 35, 550),
		SLAYER3(new Item(19089, 1), new Item(2869, 1), 35, 550),
		SLAYER4(new Item(19090, 1), new Item(2857, 1), 35, 550),
		SLAYER5(new Item(19091, 1), new Item(2856, 1), 35, 550),
		SLAYER6(new Item(19092, 1), new Item(2867, 1), 35, 550),
		ROW(new Item(2709, 1), new Item(2710, 1), 15, 1500),  
		GLOVES(new Item(13080, 1), new Item(13084, 1), 10, 1500),
		OBBY(new Item(13081, 1), new Item(13083, 1), 10, 5500),
		OBBYHELM(new Item(21065, 1), new Item(2711, 1), 10, 5500),
		OBBYPLATE(new Item(21066, 1), new Item(2712, 1), 10, 5500),
		OBBYLEGS(new Item(21067, 1), new Item(2713, 1), 10, 5500),
		OBBYGLOVES(new Item(21068, 1), new Item(2714, 1), 10, 5500),
		OBBYBOOTS(new Item(21069, 1), new Item(2715, 1), 10, 5500),
		UPGRADE$(new Item(15359, 1), new Item(10942, 1), 5, 5000),
		UPGRADE10$(new Item(10942, 1), new Item(10934, 1), 5, 5500),
		UPGRADE50$(new Item(10934, 1), new Item(10935, 1), 5, 6500),
		UPGRADE100$(new Item(10935, 1), new Item(10943, 1), 5, 7500),
        ; 
	
	private Item required, reward;
	private int chance, coinsRequired;
	
	UpgradeData(Item required, Item reward, int chance, int coinsRequired) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
		this.coinsRequired = coinsRequired;
	}
	
	public static int[] itemList = { 13080, 13081, 2709, 20134, 14018, 5079, 5080, 5081, 11071, 11067, 11068, 11087, 894, 798, 895, 799, 896, 18968, 18969, 18970, 18973, 
			18972, 18971, 18974, 18975, 18976, 18980, 19048, 19049, 19050, 19051, 19052, 19054, 19055, 19087, 19088, 19089, 19090, 
			19091, 19092, 938, 21065, 21066, 21067, 21068, 21069, 15359, 10942, 10934, 10935, 10943 };
    
	public Item getRequired() {
		return required;
	}

	public Item getReward() {
		return reward;
	}

	public int getChance() {
		return chance;
	}
	
	public int getCoinsRequired() {
		return coinsRequired;
	}
	/*
    Upgradable Items
    */
}
