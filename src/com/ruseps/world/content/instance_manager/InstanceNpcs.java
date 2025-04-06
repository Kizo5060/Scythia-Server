package com.ruseps.world.content.instance_manager;

public enum InstanceNpcs {

	//RYUK(4990, "15k/30k"),
	JESUS(4991, "15k/30k"),
	SIMBA(4992, "15k/30k"),
	KID_SORA(4999, "15k/30k"),
	SULLY(4994, "15k/30k"),
	MEGA_CHARIZARD(4981, "15k/30k"),
	SAURON(4997, "15k/30k"),
	SQUIDWARD(4993, "15k/30k"),
	ICE_DEMON(4980, "15k/30k"),
	EVE(4271, "15k/30k"),
	GIMLEE(4265, "15k/30k"),
	BLOOD_ELEMENTAL(4267, "15k/30k"),
	TIKI_DEMON(4268, "15k/30k"),
	ARAGORN(4270, "15k/30k"),
	RAYQUAZA(4275, "15k/30k"),
	LEGOLAS(3008, "15k/30k"),
	/** BOSSES **/
	DARTH_MAUL(5048, "15k/30k"),
	DIAMOND_HEAD(4998, "15k/30k"),
	DARIUS(4263, "15k/30k"),
	DEADLY_ROBOT(4264, "15k/30k"),
	ZELDORADO(4606, "15k/30k"),
	HEATBLAST(4266, "15k/30k"),
	KEVIN_FOUR_ARMS(4269, "15k/30k"),
	RA(4272, "15k/30k"),
	DARK_KNIGHT(3009, "15k/30k"),
	BAD_BITCH(4274, "15k/30k"),
	CANNONBOLT(3010, "15k/30k"),
	RED_ASSASSIN(5049, "15k/30k"),
	IT(3014, "15k/30k"),
	YVALTAL(190, "15k/30k"),
	UPGRADE(185, "15k/30k"),
	MOUNTAIN_DWELLER(4387, "15k/30k"),
	DARK_MAGICIAN_GIRL(1506, "15k/30k"),
	OBELISK(1510, "15k/30k"),
	BLOOD_QUEEN(1508, "15k/30k"),
	FOUR_ARMS(1509, "15k/30k"),
	/** Multi **/
	GALACTIC_TITAN(1016, "15k/30k"),
	OCANTE(1417, "15k/30k"),
	LIMES(1017, "15k/30k"),
	SLIFER(1018, "15k/30k"),
	FALLEN_GOD(1039, "15k/30k"),
	KNIGHT_OF_TORMENT(1038, "15k/30k"),
	THE_LITCH(1494, "15k/30k"),
	SPUDERMAN(3012, "15k/30k"),
	DR_STRANGE(3007, "15k/30k"),
	GODS_RULER(1511, "15k/30k"),
	GOLDEN_FREEZA(5148, "15k/30k"),
	;
	
	private int npcId;
	private String reqTokens;
	
	InstanceNpcs(int npcId, String reqTokens) {
		this.npcId = npcId;
		this.reqTokens = reqTokens;
	}
	
	public int getNpcId() {
		return npcId;
	}
	public String getReqTokens() {
		return reqTokens;
	}
}