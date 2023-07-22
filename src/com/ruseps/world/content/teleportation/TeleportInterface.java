package com.ruseps.world.content.teleportation;

import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.world.World;
import com.ruseps.world.content.raids.GodsRaid;
import com.ruseps.world.content.raids.MarvelsRaid;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

/**
 * @author Suic
 * @since 8th July 2019
 */

public class TeleportInterface {

	private final static int CATEGORY_NAME_ID = 50508;

	public enum Bosses {

		CHARMELEON(50601, "Charmeleon", "This monster drops", "some amazing beginner", "range weapons and armours", "",
				"@red@T1 RANGE", 6358, new int[] { 3030, 2889, 8 }, 0),
		BELERION(50602, "Belerion", "This monster drops", "the dark depature set", "", "", "@red@T2 RANGE", 4392,
				new int[] { 2704, 9756, 0 }, 0),
		BELERIONAOE(50603, "Belerion AOE", "This monster drops", "the dark depature set", "", "", "@red@T2 RANGE", 4392,
				new int[] { 3050, 2889, 8 }, 0),
		ABBADONAOE(50604, "Abbadon AOE", "This boss drops ", "Blood Depature", "and other range weapons", "", "@red@T3 RANGE",
				6303, new int[] { 2516, 5173, 0 }, 0),
		ABBADON(50605, "Abbadon", "This boss drops ", "Blood Depature", "and other range weapons", "", "@red@T3 RANGE",
				6303, new int[] { 3070, 2889, 8 }, 0),
		MEWTWO(50606, "MewTwo", "This boss drops ", "the Animal set and the", "Dark Lord's Bow", "", "@red@T3 RANGE",
				6357, new int[] { 2799, 3321, 0 }, 0),
		MEWTWOAOE(50607, "MewTwoAOE", "This boss drops ", "the Animal set and the", "Dark Lord's Bow", "", "@red@T3 RANGE",
				6357, new int[] { 3030, 2901, 8 }, 0),
		DRAGONIX(50608, "Dragonix", "This boss drops ", "the Dragonrage set,", "Dragonrage Blowpipe and",
				"the Lava Scythe", "@red@T4 RANGE", 6305, new int[] { 2516, 4638, 0 }, 0),
		DRAGONIXAOE(50609, "DragonixAOE", "This boss drops ", "the Dragonrage set,", "Dragonrage Blowpipe and",
				"the Lava Scythe", "@red@T4 RANGE", 6305, new int[] { 3050, 2901, 8 }, 0),
		VEGETAAOE(50610, "VegetaAOE", "This boss drops ", "the Vegeta set.", "Very good for range", "@blu@Multi-Boss",
				"@red@T5 RANGE", 20, new int[] { 3070, 2901, 8 }, 0),
		VEGETA(50611, "Vegeta", "This boss drops ", "the Vegeta set.", "Very good for range", "@blu@Multi-Boss",
				"@red@T5 RANGE", 20, new int[] { 2670, 3984, 1 }, 0),
		BIGORK(50612, "Big Ork Boss", "This boss drops ", "the Infernal Vegeta set", "which is an amazing set of range",
				"@blu@Multi-Boss", "@red@T8 RANGE", 6274, new int[] { 2386, 3468, 8 }, 0),
		OPDRAGONIX(50613, "OP Dragonix Boss", "This boss drops ", "the OP Dragonrage set",
				"which is an amazing set of range", "@blu@Multi-Boss", "@red@T8 RANGE", 6315,
				new int[] { 3050, 2913, 8 }, 0),
		LINK(50614, "Link Boss", "This boss drops ", "the Drygore Predator items", "which can't be missed",
				"@pur@Multi-Boss", "@pur@T9 RANGE", 6322, new int[] { 3070, 2913, 8 }, 0),
		DRSTRANGE(50615, "DrStrange", "This boss drops ", "the Grevador AOE Bane", "which can't be missed",
				"@pur@Multi-Boss", "@pur@T10 RANGE", 2505, new int[] { 3030, 2925, 8 }, 0);
		
		Bosses(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Bosses(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, final int npcReq) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.npcReq = npcReq;
		}
		

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int npcReq;

		public int getNpcReq() {
			return npcReq;
		}
	}

	public enum Monsters {
		// command is ::teleportinterface
		Lucario(50601, "Lucario", "This is a great", "npc too start grinding at!", "", "", "@red@T1 MELEE", 6360,
				new int[] { 2306, 4590, 0 }),
		LucarioAOE(50602, "Lucario AOE", "This is a great", "npc too start grinding at!", "", "", "@red@T1 MELEE", 6360,
				new int[] { 3030, 2889, 16 }),
		
		SQUIRTLE(50603, "Squirtle", "This monster drops", "the dark predetor set", "", "", "@red@T2 MELEE", 6361,
				new int[] { 3561, 9948, 0 }),
		SQUIRTLEAOE(50604, "Squirtle AOE", "This monster drops", "the dark predetor set", "", "", "@red@T2 MELEE", 6361,
				new int[] { 3050, 2889, 16 }),
		
		REAVERS(50605, "Blood Reaver", "This monster drops", "the deadly assasin set", "", "", "@red@T3 MELEE", 13458,
				new int[] { 3169, 2982, 0 }),
		REAVERSAOE(50606, "Blood Reaver AOE", "This monster drops", "the deadly assasin set", "", "", "@red@T3 MELEE", 13458,
				new int[] { 3050, 2889, 16 }),
		
		
		ALLAN(50607, "Allan", "This monster drops", "the nature torva set", "", "", "@red@T4 MELEE", 1459,
				new int[] { 2796, 2776, 0 }),
		ALLANAOE(50608, "Allan AOE", "This monster drops", "the nature torva set", "", "", "@red@T4 MELEE", 1459,
				new int[] { 3030, 2901, 16 }),
		
		VLADIMIR(50609, "Vladimir", "This boss drops ", "the Shadow Brutal Whip", "and the", "Corrupt Ring of Wealth",
				"@red@T5 MELEE", 9357, new int[] { 1930, 4641, 0 }),
		VLADIMIRAOE(50610, "Vladimir AOE", "This boss drops ", "the Shadow Brutal Whip", "and the", "Corrupt Ring of Wealth",
				"@red@T5 MELEE", 9357, new int[] { 3050, 2901, 16 }),
		
		SHADOWLORD(50611, "Shadowlord", "This boss drops ", "the ShadowLord set", "", "@blu@Multi-Boss",
				"@red@T5 MELEE", 2518, new int[] { 2899, 3618, 0 }),
		SHADOWLORDAOE(50612, "Shadowlord AOE", "This boss drops ", "the ShadowLord set", "", "@blu@Multi-Boss",
				"@red@T5 MELEE", 2518, new int[] { 3070, 2901, 16 }),
		
		DRAKKON(50613, "Drakkon", "This boss drops ", "the Drakkon set", "", "", "@red@T6 MELEE", 201,
				new int[] { 3108, 5537, 0 }),
		DRAKKONAOE(50614, "Drakkon AOE", "This boss drops ", "the Drakkon set", "", "", "@red@T6 MELEE", 201,
				new int[] { 3030, 2913, 16 }),
		
		GOKU(50615, "Goku", "This boss drops ", "the goku set.", "Very good for melee", "@blu@Multi-Boss",
				"@red@T7 MELEE", 7, new int[] { 3050, 2913, 16 }),
		
		INFARTICO(50616, "Infartico", "This boss drops ", "the blood glaive set", "which is a very strong weapon",
				"@blu@Multi-Boss", "@red@T8 MELEE", 1010, new int[] { 2779, 2745, 0 }),
		INFARTICOAOE(50617, "Infartico AOE", "This boss drops ", "the blood glaive set", "which is a very strong weapon",
				"@blu@Multi-Boss", "@red@T8 MELEE", 1010, new int[] { 3070, 2913, 16 }),
		
		PURPLEWYRM(50618, "Purple Wyrm", "This boss drops ", "the Purple Wyrm items", "Keys",
				"@pur@Multi-Boss", "@pur@T10 MELEE", 6335, new int[] { 3030, 2925, 16 });

		Monsters(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
	}

	public enum Dungeons {
		AURELIA(50601, "Aurelia", "This monster drops", "the oblivion set", "", "", "@red@T1 MAGIC", 6102,
				new int[] { 1747, 5323, 0 }),
		AURELIAAOE(50602, "Aurelia AOE", "This monster drops", "the oblivion set", "", "", "@red@T1 MAGIC", 6102,
				new int[] { 3030, 2889, 24 }),
		BLACKPANTHER(50603, "Black Panther", "This boss drops ", "the Shadow oblivion set",
				"which is an amazing set of mage", "@blu@Multi-Boss", "@red@T8 MAGE", 2500,
				new int[] { 2387, 3481, 8 }),
		BLACKPANTHERAOE(50604, "Black Panther AOE", "This boss drops ", "the Shadow oblivion set",
				"which is an amazing set of mage", "@blu@Multi-Boss", "@red@T8 MAGE", 2500,
				new int[] { 3050, 2889, 24 }),
		THANOS(50605, "Thanos Boss", "This boss drops ", "the Blood Oblivion set", "which is an amazing set of mage",
				"@blu@Multi-Boss", "@red@T9 MAGE", 2515, new int[] { 2402, 3481, 8 }),
		THANOSAOE(50606, "Thanos AOE", "This boss drops ", "the Blood Oblivion set", "which is an amazing set of mage",
				"@blu@Multi-Boss", "@red@T9 MAGE", 2515, new int[] { 3070, 2889, 24 }),
		TODODILE(50607, "Todoile", "This boss drops ", "Ancient Ceremonal", "which is an amazing set of mage",
				"@blu@Multi-Boss", "", 6377, new int[] { 3030, 2901, 24 }),
		LARACROFT(50608, "Lara-croft", "This boss drops ", "AOE Weapons", "",
				"@blu@Multi-Boss", "", 6324, new int[] { 3050, 2901, 24 })
		;

		Dungeons(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;

	}

	public enum Skilling {
		LIVY(50601, "Livy", "This monster drops", "the trio set", "", "", "@red@T5 HYBRID", 52,
				new int[] { 2908, 9803, 0 }),
		LIVYAOE(50602, "Livy AOE", "This monster drops", "the trio set", "", "", "@red@T5 HYBRID", 52,
				new int[] { 3030, 2889, 32 }),
		HEKATE(50603, "Hekate Boss", "This boss drops ", "the Hekate set", "which is an amazing set of hybrid",
				"@blu@Multi-Boss", "@red@T8 HYBRID", 66, new int[] { 2382, 3884, 0 }),
		HEKATEAOE(50604, "Hekate AOE", "This boss drops ", "the Hekate set", "which is an amazing set of hybrid",
				"@blu@Multi-Boss", "@red@T8 HYBRID", 66, new int[] { 3050, 2889, 32 }),
		DARTH_VADER(50605, "Darth Vader", "This boss drops ", "", "",
				"@blu@Multi-Boss", "@red@T8 HYBRID", 67, new int[] { 2802, 4718, 0 }),
		DARTHVADERAOE(50606, "Darth Vader AOE", "", "", "",
				"@blu@Multi-Boss", "@red@T8 HYBRID", 67, new int[] { 3070, 2889, 32 })
		
		;

		Skilling(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
	}

	public enum Minigames {
		MARVEL(50601, "Marvel Raid", "This minigame can", "reward you with very", "high tier gear", "",
				"@red@Pets not allows!", 2511, new int[] { 1823, 5154, 2 }),
		PC(50602, "Pest Control", "This minigame can", "reward you with", "void pieces which increase",
				"your overal damage", "@red@!", 3789, new int[] { 2662, 2651, 0 }),
		OLM(50603, "Olm Minigame", "This minigame can", "reward you with very", "high tier, exclusive gear", "",
				"@red@WARNING: Very Grindy!", 8670, new int[] { 2582, 4609, 0 }),
		GAMBLING(50604, "Gambling Zone", "This is where you may", "gamble with other players", "", "",
				"@red@WARNING: You may loose!", 4249, new int[] { 2441, 3090, 0 }),
		ZOMBIE(50605, "Zombie Minigame", "This minigame can", "reward you with great", "starting gear for bossing", "",
				"@red@!", 3101, new int[] { 3503, 3564, 0 }),
		AFK_TREE(50606, "AFK Tree", "AFK at this tree too", " get rewarded with cash.", "Great side money maker!", "",
				"@red@Higher donator status=more cash!", 3622, new int[] { 3159, 3511, 0 }),
		VADERRAID(50607, "Vader Raid", "This minigame can", "reward you with very", "high tier gear", "",
				"@red@Pets not allows!", 68, new int[] { 2835, 4580, 0 }),
		MULTIBOSSRAID(50608, "Multi Boss Raid", "This minigame can", "reward you with very", "high tier gear", "",
				"@red@Pets not allows!", 6322, new int[] { 2718, 9805, 0 }),
		FANTASYTREE(50609, "GrevadorPs Tree", "AFK at this tree too", " get rewarded with fantasy tickets", "for the new shop",
				"", "@red@Higher donator status=more cash!", 3622, new int[] { 3159, 3511, 8 }),
		CUSTOMRAID(50610, "Custom Raid", "This minigame can", " reward you with very", "high tier gear", "",
				"@red@Single Player!", 3622, new int[] { 3024, 5232, 0 });

		// 2835
		Minigames(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
	}

	public enum Cities {
		BANDOS_AVATAR(50601, "Bandos Avatar", "This boss drops the", "Trio set and regular", "donator status", "", "",
				4540, new int[] { 2867, 9946, 0 }),
		BANDOS_AVATARAOE(50602, "Bandos Avatar AOE", "This boss drops the", "Trio set and regular", "donator status", "", "",
				4540, new int[] {3030, 2889, 40 }),
		GROUDON(50603, "Infernal Groudon", "This boss drops ", "the Infernal set", "and various infernal guns", "", "",
				1234, new int[] { 1240, 1227, 0 }),
		GROUDONAOE(50604, "Infernal Groudon AOE", "This boss drops ", "the Infernal set", "and various infernal guns", "", "",
				1234, new int[] { 3050, 2889, 40 }),
		BAPHOMET(50605, "Baphomet", "This boss drops ", "the Baphomet Torva set", "", "", "", 2236,
				new int[] { 2461, 10156, 0 }),
		BAPHOMETAOE(50606, "Baphomet AOE", "This boss drops ", "the Baphomet Torva set", "", "", "", 2236,
				new int[] { 3070, 2889, 40}),
		LEFOSH(50607, "Le'fosh", "This boss drops ", "a lot of boxes", "", "", "@red@BOX ZONE", 6309,
				new int[] { 2464, 4782, 0 }),
		LEFOSHAOE(50608, "Le'fosh AOE", "This boss drops ", "a lot of boxes", "", "", "@red@BOX ZONE", 6309,
				new int[] { 3030, 2901, 40 }),
		YETI(50609, "Yeti", "This boss drops ", "various strong Capes", "and Necklaces", "", "@red@CAPES/NECKLACES",
				130, new int[] { 2835, 3816, 0 }),
		YETIAOE(50610, "Yeti AOE", "This boss drops ", "various strong Capes", "and Necklaces", "", "@red@CAPES/NECKLACES",
				130, new int[] { 3050, 2901, 40 }),
		CERYS(50611, "Cerys", "This boss drops ", "a lot of boxes", "", "", "@red@BOX ZONE", 433,
				new int[] { 2914, 4446, 0 }),
		CERYSAOE(50612, "Cerys AOE", "This boss drops ", "a lot of boxes", "", "", "@red@BOX ZONE", 433,
				new int[] { 3070, 2901, 40 }),
		JACKKRAKEN(50613, "Jack 'o Kraken Boss", "This boss drops ", "OP ASF Keys which can be", "be used on the chest",
				"@blu@Multi-Boss", "@red@COSMETIC", 2506, new int[] {3030, 2913, 40 }),
		RICK(50614, "Rick", "This starter boss drops", "some great gear that'll help", "you move onto bossing in",
				"GrevadorPs", "@red@Starter Boss", 6351, new int[] { 2786, 4839, 0 }),
		RICKAOE(50615, "Rick AOE", "This starter boss drops", "some great gear that'll help", "you move onto bossing in",
				"GrevadorPs", "@red@Starter Boss", 6351, new int[] { 3050, 2913, 40 }),
		ALIEN(50616, "ALIEN", "This monsters drops ", "a lot of SOE'S", "", "", "@red@SOE ZONE", 4419,
				new int[] { 2914, 4446, 8 }),
		ALIENAOE(50617, "ALIEN AOE", "This monsters drops ", "a lot of SOE'S", "", "", "@red@SOE ZONE", 4419,
				new int[] { 3070, 2913, 40 }),
		BETABOX(50618, "Beta-mBox", "Mhmm Mbox's? ", "", "", "", "@red@MBox ZONE", 6423, new int[] { 2264, 3313, 0 }),
		MINIHULK(50619, "Mini Hulk", "Mini green?", "", "", "", "HULK?!", 8534, new int[] { 3035, 4497, 0 }),
		THANOS(50620, "Thanos", "All Mighty", "", "", "", "THANOS!", 9765, new int[] { 3106, 5529, 0 }),
		HULK(50621, "Hulk", "Full Beast", "", "", "", "HULK!", 8493, new int[] { 3029, 2913, 0 }),
		ROBOT(50622, "Robot'", "Beep Beep", "", "", "", "ROBOT!", 5588, new int[] { 1836, 5088, 2}),
		MONEYBAG(50623, "Money Zone'", "CASH CASH", "", "", "", "lovely gold!", 22, new int[] { 2421, 4686, 0})
		
		;

		Cities(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
	}

	public static void handleTeleports(Player player) {
		if (currentClickIndex == -2) {
			if (player.lastTeleportPosition == null) {
				TeleportHandler.teleportPlayer(player, new Position(1839, 5087, 2),
						player.getSpellbook().getTeleportType());
			} else {
				TeleportHandler.teleportPlayer(player, player.lastTeleportPosition,
						player.getSpellbook().getTeleportType());
			}
			return;
		}
		switch (currentTab) {
		case 0:
			Bosses bossData = Bosses.values()[currentClickIndex];
			handleBossTeleport(player, bossData);
			break;
		case 1:
			Monsters monsterData = Monsters.values()[currentClickIndex];
			handleMonsterTeleport(player, monsterData);
			break;
		case 2:
			Dungeons dungeonsData = Dungeons.values()[currentClickIndex];
			handleDungeonsTeleport(player, dungeonsData);
			break;
		case 3:
			Skilling skillingData = Skilling.values()[currentClickIndex];
			handleSkillingTeleport(player, skillingData);
			break;
		case 4:
			Minigames minigameData = Minigames.values()[currentClickIndex];
			handleMinigameTeleport(player, minigameData);
			break;
		case 5:
			Cities cityData = Cities.values()[currentClickIndex];
			handleCityTeleport(player, cityData);
			break;

		}

	}

	public static void handleBossTeleport(Player player, Bosses bossData) {

		TeleportHandler.teleportPlayer(player,
				new Position(bossData.teleportCords[0], bossData.teleportCords[1], bossData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(bossData.teleportCords[0], bossData.teleportCords[1], bossData.teleportCords[2]);
	}

	public static void handleMonsterTeleport(Player player, Monsters monsterData) {

		TeleportHandler.teleportPlayer(player,
				new Position(monsterData.teleportCords[0], monsterData.teleportCords[1], monsterData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(monsterData.teleportCords[0], monsterData.teleportCords[1], monsterData.teleportCords[2]);
	}

	public static void handleDungeonsTeleport(Player player, Dungeons wildyData) {

		TeleportHandler.teleportPlayer(player,
				new Position(wildyData.teleportCords[0], wildyData.teleportCords[1], wildyData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(wildyData.teleportCords[0], wildyData.teleportCords[1], wildyData.teleportCords[2]);
	}

	public static void handleSkillingTeleport(Player player, Skilling skillData) {

		TeleportHandler.teleportPlayer(player,
				new Position(skillData.teleportCords[0], skillData.teleportCords[1], skillData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(skillData.teleportCords[0], skillData.teleportCords[1], skillData.teleportCords[2]);
	}

	public static void handleMinigameTeleport(Player player, Minigames minigameData) {

		if (minigameData.teleportCords[0] == 3138) {
			World.minigameHandler.getMinigame(1).addPlayer(player);
		}

		if (minigameData.teleportCords[0] == 1823) {
			if (player.getSummoned() != -1) {
				player.sendMessage("You cannot bring pets into the Super Sayian raid!");
				return;
			} else {
				if (!GodsRaid.getStarted()) {
					World.minigameHandler.getMinigame(0).addPlayer(player);
				} else {
					player.sendMessage("@red@Super Sayian Raid is already started! wait till next round.");
					return;
				}
				if (minigameData.teleportCords[0] == 2263) {
					if (player.getSummoned() != 0) {
						player.sendMessage("You can bring pets into the marvel raid!");
						return;
					} else {
						if (!MarvelsRaid.getStarted()) {
							World.minigameHandler.getMinigame(0).addPlayer(player);
						} else {
							player.sendMessage("@red@Marvel Raid is already started! wait till next round.");
							return;
						}
					}
				}
			}
		}
		TeleportHandler.teleportPlayer(player, new Position(minigameData.teleportCords[0],
				minigameData.teleportCords[1], minigameData.teleportCords[2]), player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(minigameData.teleportCords[0], minigameData.teleportCords[1], minigameData.teleportCords[2]);

	}

	public static void handleCityTeleport(Player player, Cities cityData) {

		TeleportHandler.teleportPlayer(player,
				new Position(cityData.teleportCords[0], cityData.teleportCords[1], cityData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
		
		player.lastTeleportPosition = new Position(cityData.teleportCords[0], cityData.teleportCords[1], cityData.teleportCords[2]);
	}

	public static void clearData(Player player) {
		for (int i = 50601; i < 50700; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	public static int currentTab = 0; // 0 = Boss, 1 = Monsters, 2 = Wildy, 3 = Skilling, 4 = Minigame, 5 = Cities

	public static boolean handleButton(Player player, int buttonID) {

		if (!(buttonID >= -14935 && buttonID <= -14836)) {
			return false;
		}
		int index = -1;

		if (buttonID >= -14935) {
			index = 14935 + buttonID;
		}
		if (currentTab == 0) {
			if (index >= 0 && index < Bosses.values().length) {
				System.out.println("Handled boss data [As index was 0]");
				Bosses bossData = Bosses.values()[index];
				currentClickIndex = index;
				sendBossData(player, bossData);
				sendDrops(player, bossData.npcId);
			}
		}
		if (currentTab == 1) {
			if (index >= 0 && index < Monsters.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Monsters monsterData = Monsters.values()[index];
				currentClickIndex = index;
				sendMonsterData(player, monsterData);
				sendDrops(player, monsterData.npcId);
			}
		}
		if (currentTab == 2) {
			if (index >= 0 && index < Dungeons.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Dungeons dungeonsData = Dungeons.values()[index];
				currentClickIndex = index;
				senddungeonsData(player, dungeonsData);
				sendDrops(player, dungeonsData.npcId);
			}
		}
		if (currentTab == 3) {
			if (index >= 0 && index < Skilling.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Skilling skillingData = Skilling.values()[index];
				currentClickIndex = index;
				sendSkillingData(player, skillingData);
				sendDrops(player, skillingData.npcId);
			}
		}
		if (currentTab == 4) {
			if (index >= 0 && index < Minigames.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Minigames minigamesData = Minigames.values()[index];
				currentClickIndex = index;
				sendMinigameData(player, minigamesData);
				sendDrops(player, minigamesData.npcId);
			}
		}
		if (currentTab == 5) {
			if (index >= 0 && index < Cities.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Cities cityData = Cities.values()[index];
				currentClickIndex = index;
				sendCityData(player, cityData);
				sendDrops(player, cityData.npcId);
			}
		}

		return true;

	}

	public static int currentClickIndex = -14935;

	public static void sendBossData(Player player, Bosses data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void sendMonsterData(Player player, Monsters data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void senddungeonsData(Player player, Dungeons data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void sendSkillingData(Player player, Skilling data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void sendMinigameData(Player player, Minigames data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void sendCityData(Player player, Cities data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcHeadOnInterface(50514, data.npcId);
	}

	public static void sendBossTab(Player player) {
		player.getPacketSender().sendInterface(50500);
		currentClickIndex = -2;
		currentTab = 0;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Boss Names");
		for (Bosses data : Bosses.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}

	}

	public static void sendMonsterTab(Player player) {
		currentTab = 1;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Monster Names");
		for (Monsters data : Monsters.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendDungeonsTab(Player player) {
		currentTab = 2;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Wildy Names");
		for (Dungeons data : Dungeons.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendSkillingTab(Player player) {
		currentTab = 3;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Skilling Names");
		for (Skilling data : Skilling.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendMinigamesTab(Player player) {
		currentTab = 4;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Skilling Names");
		for (Minigames data : Minigames.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendCitiesTab(Player player) {
		currentTab = 5;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Skilling Names");
		for (Cities data : Cities.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendDrops(Player player, int npcId) {
		// ogod
		player.getPacketSender().resetItemsOnInterface(51251, 100); // wtf u dont have this method?
		try {
			NPCDrops drops = NPCDrops.forId(npcId);
			for (int i = 0; i < drops.getDropList().length; i++) {
				player.getPacketSender().sendItemOnInterface(51251, drops.getDropList()[i].getId(), i,
						drops.getDropList()[i].getItem().getAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
