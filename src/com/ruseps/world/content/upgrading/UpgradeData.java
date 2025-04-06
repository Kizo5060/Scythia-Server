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
		REDTROOPERHELM(new Item(11071, 1), new Item(2547, 1), 23, 400),
		REDTROOPERBODY(new Item(11067, 1), new Item(2548, 1), 35, 400),
		REDTROOPERLEGS(new Item(11068, 1), new Item(2549, 1), 23, 400),
		REDTROOPERBOOTS(new Item(11087, 1), new Item(2544, 1), 23, 400),
		SCHOOLGIRLHELM(new Item(894, 1), new Item(900, 1), 25, 200),
		SCHOOLGIRLBODY(new Item(798, 1), new Item(897, 1), 25, 200),
		SCHOOLGIRLLEGS(new Item(895, 1), new Item(898, 1), 25, 200),
		SCHOOLGIRLGLOVES(new Item(799, 1), new Item(899, 1), 25, 200),
		SCHOOLGIRLBOOTS(new Item(896, 1), new Item(901, 1), 25, 200),
		SCALEY1(new Item(18968, 1), new Item(18973, 1), 25, 500),
		SCALEY12(new Item(18969, 1), new Item(18972, 1), 25, 500),
		SCALEY13(new Item(18970, 1), new Item(18971, 1), 25, 500),
		SCALEY21(new Item(18973, 1), new Item(18974, 1), 25, 700),
		SCALEY22(new Item(18972, 1), new Item(18975, 1), 25, 700),
		SCALEY23(new Item(18971, 1), new Item(18976, 1), 25, 700),
		SCALEY31(new Item(18974, 1), new Item(18979, 1), 20, 800),
		SCALEY32(new Item(18975, 1), new Item(18978, 1), 20, 800),
		SCALEY33(new Item(18976, 1), new Item(18977, 1), 20, 800),
		BDRAGON(new Item(18980, 1), new Item(18981, 1), 25, 3500),
		LIGHTNINGH(new Item(938, 1), new Item(18991, 1), 35, 500),
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
		SLAYER6(new Item(19092, 1), new Item(2867, 1), 50, 550),
		MLAYER1(new Item(2858, 1), new Item(13026, 1), 20, 4000),
		MLAYER2(new Item(2869, 1), new Item(13024, 1), 20, 4000),
		MLAYER3(new Item(2859, 1), new Item(13025, 1), 20, 4000),
		MLAYER4(new Item(2857, 1), new Item(13027, 1), 20, 4000),
		MLAYER5(new Item(2856, 1), new Item(13028, 1), 20, 4000),
		MLAYER6(new Item(2867, 1), new Item(13029, 1), 20, 4000),
		ROW(new Item(2709, 1), new Item(2710, 1), 15, 1500),  
		GLOVES(new Item(13080, 1), new Item(13084, 1), 35, 1500),
		OBBY(new Item(13081, 1), new Item(13083, 1), 25, 2500),
		OBBYHELM(new Item(21065, 1), new Item(2711, 1), 25, 1500),
		OBBYPLATE(new Item(21066, 1), new Item(2712, 1), 25, 1500),
		OBBYLEGS(new Item(21067, 1), new Item(2713, 1), 25, 1500),
		OBBYGLOVES(new Item(21068, 1), new Item(2714, 1), 25, 1500),
		OBBYBOOTS(new Item(21069, 1), new Item(2715, 1), 25	, 1500),
		NOFHELM(new Item(13034, 1), new Item(2799, 1), 15	, 6500),
		NOFBODY(new Item(13030, 1), new Item(2800, 1), 15	, 6500),
		NOFLEGS(new Item(13031, 1), new Item(2801, 1), 15	, 6500),
		NOFGLOVES(new Item(13033, 1), new Item(2802, 1), 10	, 6500),
		NOFFEET(new Item(13032, 1), new Item(2803, 1), 10	, 6500),
		PETEGG(new Item(10537, 1), new Item(667, 1), 10	, 6500),
		GODOFSLAYERHELM(new Item(13026, 1), new Item(19030, 1), 100	, 175000),
		GODOFSLAYERBODY(new Item(13024, 1), new Item(19031, 1), 100	, 175000),
		GODOFSLAYERLEGS(new Item(13025, 1), new Item(19032, 1), 100	, 175000),
		GODOFSLAYERBOOTS(new Item(13028, 1), new Item(19034, 1), 100	, 175000),
		GODOFSLAYERGLOVES(new Item(13027, 1), new Item(19033, 1), 100	, 175000),
		GODOFSLAYERSWORD(new Item(10876, 1), new Item(19035, 1), 20	, 10500),
		GODOFSLAYEROFFHAND(new Item(19647, 1), new Item(19036, 1), 15	, 10500),
		DEMONIOHELM(new Item(2774, 1), new Item(12282, 1), 10, 25000),
		DEMONIOBODY(new Item(2778, 1), new Item(12279, 1), 10, 25000),
		DEMONIOLEGS(new Item(2776, 1), new Item(12278, 1), 10, 25000),
		DEMONIOGLOVES(new Item(2780, 1), new Item(8465, 1), 10, 25000),
		DEMONIOBOOTS(new Item(2782, 1), new Item(8467, 1), 10	, 25000),
		OWNERBOOSTER(new Item(18941, 1), new Item(17916, 1), 5, 55500),
		BPASS(new Item(2996, 1), new Item(4067, 1), 35	, 45000),
		BRONZEMBOX(new Item(915, 1), new Item(6855, 1), 15	, 750),
		SILVERMBOX(new Item(6855, 1), new Item(6856, 1), 15	, 1000),
		SECRETSPECMBOX(new Item(6856, 1), new Item(21055, 1), 15, 1500),
		GOLDMBOX(new Item(6856, 1), new Item(6853, 1), 15	, 2500),
		DRSCROLLT2(new Item(12502, 1), new Item(1503, 1), 25 , 5000),
		DRSCROLLT3(new Item(1503, 1), new Item(15671, 1), 15 , 4000),
		UPGRADE10$(new Item(10942, 1), new Item(10934, 1), 5, 1500),
        ; 
	
	private Item required, reward;
	private int chance, coinsRequired;
	
	UpgradeData(Item required, Item reward, int chance, int coinsRequired) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
		this.coinsRequired = coinsRequired;
	}
	
	public static int[] itemList = { 13080, 13081, 2709, 11071, 11067, 11068, 11087, 894, 798, 895, 799, 896, 18968, 18969, 18970, 18973, 
			18972, 18971, 18974, 18975, 18976, 18980, 19048, 19049, 19050, 19051, 19052, 19054, 19055, 19087, 19088, 19089, 19090, 19091, 19092, 2858, 2869, 2859, 
			2857, 2856, 2867, 938, 21065, 21066, 21067, 21068, 21069, 13034, 13030, 13031, 13033, 13032, 10537,13026, 13024, 13025, 13028, 13027, 10876, 19647, 2774, 2778, 
			2776, 2780, 2782, 18941, 12502, 1503, 2996, 915, 6855, 6856,  10942};
    
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
