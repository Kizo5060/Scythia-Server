package com.ruseps.world.content.collectionlog;

import com.ruseps.model.Item;

public enum CollectionLogNpcs
{
	/** MONSTERS **/
	RYUK(4990, 30560),
	JESUS(4991, 30561),
	SIMBA(4992, 30562),
	KID_SORA(4999, 30563),
	SULLY(4994, 30564),
	MEGA_CHARIZARD(4981, 30565),
	SAURON(4997, 30566),
	SQUIDWARD(4993, 30567),
	ICE_DEMON(4980, 30568),
	EVE(4271, 30569),
	GIMLEE(4265, 30570),
	BLOOD_ELEMENTAL(4267, 30571),
	TIKI_DEMON(4268, 30572),
	ARAGORN(4270, 30573),
	RAYQUAZA(4275, 30574),
	LEGOLAS(3008, 30575),
	/** BOSSES **/
	DARTH_MAUL(5048, 30576),
	DIAMOND_HEAD(4998, 30577),
	DARIUS(4263, 30578),
	DEADLY_ROBOT(4264, 30579),
	ZELDORADO(4606, 30580),
	HEATBLAST(4266, 30581),
	KEVIN_FOUR_ARMS(4269, 30582),
	RA(4272, 30583),
	DARK_KNIGHT(5048, 30584),
	BAD_BITCH(4274, 30585),
	CANNONBOLT(3010, 30586),
	RED_ASSASSIN(3011, 30587),
	IT(3014, 30588),
	YVALTAL(190, 30589),
	UPGRADE(185, 30590),
	MOUNTAIN_DWELLER(4387, 30591),
	DARK_MAGICIAN_GIRL(1506, 30592),
	OBELISK(1510, 30593),
	BLOOD_QUEEN(1508, 30594),
	FOUR_ARMS(1509, 30595),
	/**Multi**/
	GALACTIC_TITAN(1016, 30596),
	OCANTE(1417, 30597),
	LIMES(1017, 30598),
	SLIFER(1018, 30599),
	FALLEN_GOD(1039, 30600),
	KNIGHT_OF_TORMENT(1038, 30601),
	THE_LITCH(1494, 30602),
	SPUDERMAN(3012, 30603),
	DR_STRANGE(3007, 30604),
	GODS_RULER(1511, 30605),
	GOLDEN_FREEZA(5148, 30606),;

	
	private int npcId;
	private int buttonId;
	private Item[] drops;
	
	CollectionLogNpcs(int npcId, int buttonId)
	{
		this.npcId = npcId;
		this.buttonId = buttonId;
	}
	
	public int getNpcId() 
	{
		return npcId;
	}
	
	public int getButtonId() 
	{
		return buttonId;
	}
	
	public Item[] getDrops() 
	{
		return drops;
	}
	
	 public static CollectionLogNpcs getNpcById(int npcId)
	 {
		 for (CollectionLogNpcs npc : CollectionLogNpcs.values())
		 {
			 if (npc.getNpcId() == npcId)
			 {
				 return npc;
	         }
	     }
	        return null;
	 }
}
