package com.ruseps.world.content.teleport;

import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.world.entity.impl.player.Player;

import java.util.ArrayList;

public enum TeleportEnum 
{
    SNOWMAN(5049,1,"Abominable Snowman",new Position(2520,4839),"@gre@Easy"),
    RYUK(4990,1,"Ryuk",new Position(2465,4779),"@gre@Easy"),
    JESUS(4991,1,"Jesus of Nazareth",new Position(2329,4637,2),"@gre@Medium"),
    SIMBA(4992,1,"Simba",new Position(2524,4762),"@gre@Hard"),
    SORA(4999,1,"Kid Sora",new Position(2848,4059),"@gre@Harder"),
    SULLY(4994,1,"Sully",new Position(2532,4650),"@gre@Pretty Hard"),
    MEGA_CHARIZARD(4981,1,"Mega Charizard",new Position(2523,2542),"@gre@Hardest"),
    SAURON(4997,1,"Sauron",new Position(2588,2593),"@gre@Even Harder"),
    SQUIDWARDOSAURUS(4993,1,"Squidward-o-saurus",new Position(3297,4074),"@gre@Hard AF"),
    ICEDEMON(4980,1,"Ice Demon",new Position(2651,4061, 0),"@gre@Hard AF"),
    EVE(4271,1,"Weaponized Eve",new Position(2670, 3984, 0),"@gre@Forced AF"),
    GIMLEE(4265,1,"Gimlee",new Position(2336,4582),"@gre@Hard AF"),
    BLOOD_ELEMENTAL(4267,1,"Blood Elemental",new Position(3487,2717),"@gre@Hard AF"),
    TIKI_DEMON(4268,1,"Tiki Demon",new Position(3289,4123),"@gre@Hard AF"),
    ARAGORN(4270,1,"Aragorn",new Position(2711, 9510),"@gre@Hard AF"),
    RAYQUAZA(4275,1,"Rayquaza",new Position(3035, 4497),"@gre@Hard AF"),
    LEGOLAS(3008,1,"Legolas",new Position(2409,4691),"@gre@Hard AF"),
    DARTH_MAUL(5048,2,"Darth Maul",new Position(1904,5408),"@mag@Boss"),
    DIAMOND_HEAD(4998,2,"Diamond Head",new Position(2717, 3048),"@mag@Boss"),
    DARIUS(4263,2,"Darius",new Position(2704, 9756),"@mag@Boss"),
    DEADLY(4264,2,"Deadly Robot",new Position(3114,4388),"@mag@Boss"),
    ZELDORADO(4606,2,"Zeldorado",new Position(2809,3378),"@mag@Boss"),
    HEATBLAST(4266,2,"Heatblast",new Position(3561,9948 ,0),"@mag@Boss"),
    KEVIN_FOUR_ARMS(4269,2,"Kevin Four Arms",new Position(2867,9946),"@mag@Boss"),
    RA(4272,2,"Ra the Sun God",new Position(2403,3482),"@mag@Boss"),
    DARKKNIGHT(3009,2,"Dark Knight",new Position(2835,4580),"@mag@Boss"),
    BADDY(4274,2,"Bad Bitch",new Position(2595,4579),"@mag@Boss"),
    CANNONBOLT(3010,2,"Cannonbolt",new Position(2720,9828),"@mag@Boss"),
    RED(3011,2,"Red Assassin",new Position(3284, 4972, 0),"@mag@Boss"),
    IT(3014,2,"Evil Ass Clown It",new Position(2899,3618),"@mag@Boss"),
    YVALTAL(190,2,"Yvaltal",new Position(3359, 5218),"@mag@Boss"),
    UPGRADE(185,2,"Upgrade",new Position(3420, 5276),"@mag@Boss"),
    MOUNTAIN_DWELLER(4387,2,"Mountain Dweller",new Position(2339, 3873),"@mag@Boss"),
    MISSMAGICIAN(1506,2,"Dark Magician Girl",new Position(2777, 3287),"@mag@Boss"),
    OBELISK(1510,2,"Obelisk the Destroyer",new Position(2383, 3890, 0),"@mag@Boss"),
    BLOODQUEEN(1508,2,"Blood Queen",new Position(3039, 4835, 0),"@mag@Boss"),
    FOUR_ARMS(1509,2,"Four Arms",new Position(2652, 3675, 0),"@mag@Boss"),
    DEFENDERZ(5763,3,"Defenderz",new Position(3026, 5230, 0),"@gre@Defenderz Minigame"),
    WEAPONZ(2237,3,"Weaponz",new Position(3056, 5232, 0),"@gre@Weaponz Minigame"),
    MAGIC(1415,3,"Magic",new Position(3058, 5200, 0),"@gre@Magic Weaponz Minigame"),
    RANGE(1414,3,"Range",new Position(3085, 5200, 0),"@gre@Range Weaponz Minigame"),
    BEGINNER_AOE(13458,3,"Beginner Aoe Zone",new Position(2718, 2857, 0),"@yel@AOE"),
    OCTANE(1417,4,"Octane",new Position(2403,2910),"@red@Multi Boss"),
    LIMES(1017,4,"Lt. Limes",new Position(2719,3874),"@red@Multi Boss"),
    SLIFER(1018,4,"Slifer the Sky Dragon",new Position(2208,3802),"@red@Multi Boss"),
    FALLENGOD(1039,4,"Fallen Lord",new Position(3421,3996),"@red@Multi Boss"),
    TORMENT_KNIGHT(1038,4,"Knight of Torment",new Position(3237,3042),"@red@Multi Boss"),
    SAIYAN(4602,5,"Super Sayian Raids",new Position(1823,5154, 2),"@blu@Raid"),
    PokemonRaid(5154,5,"Pokemon Raids",new Position(2297, 3331),"@red@Poke @whi@Raid"),
    AnimeRaid(1500,5,"Anime Raids",new Position(3034, 2839),"@red@Anime @whi@Raid"),
    MortalKombatRaid(1492,5,"Mortal kombat Raids",new Position(2846, 3102),"@red@Mortal @yel@Kombat @whi@Raids"),
    Wr3ckedYou(1416,2,"The Witch Boss",new Position(3355,3034),"@or1@The Witch Boss"),
    EVENTS(1041,2,"Spawned Events",new Position(3555,3106),"@or1@Spawned Events"),
    WILDERNESS(2253, 6,"Wilderness Level 1", new Position(3093,3528),"@or1@Wilderness Level 1"),
    TZHAAR(2253, 6,"Tzhaar Dungeon", new Position(2445,5178),"@or1@Tzhaar Dungeon"),
    FALADOR(2253, 6,"Falador", new Position(2964,3378),"@or1@Falador"),
    CAMELOT(2253, 6,"Camelot", new Position(2757,3477),"@or1@Camelot"),
    VARROCK(2253, 6,"Varrock", new Position(3214,3424),"@or1@Varrock"),
    DUAL_ARENA(2253, 6,"Duel Arena", new Position(3366,3276),"@or1@Duel Arena"),
	BARBARAN_VILLAGE(2253, 6,"Barbarian Villiage", new Position(3083,3423),"@or1@Barbarian Villige");
	
    private TeleportEnum(int npcId, int id, String teleportName, Position position, String difficulty){
        this.npcId = npcId;
        this.id = id;
        this.teleportName = teleportName;
        this.position = position;
        this.difficulty = difficulty;
    }

    void TeleportEnum1(int i, int j, String string, Position position2, String string2) {
		
	}

	private int npcId;
    private String teleportName;
    private Position position;
    private int id;
    private String difficulty;
    private Item[] drops;


    public static ArrayList<TeleportEnum> dataByTier(int tier){
        ArrayList<TeleportEnum> teleports = new ArrayList<>();
        for(TeleportEnum data : TeleportEnum.values()) {
            if(data.id == tier){
                teleports.add(data);
            }
        }
        return teleports;
    }


    public int getNpcId() {
        return npcId;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getTeleportName() {
        return teleportName;
    }

    public String getDifficulty() {
        return difficulty;
    }

	public Item[] getDrops() {
		return drops;
	}

	public void setDrops(Item[] drops) {
		this.drops = drops;
	}
}
