package com.ruseps.world.content.collectionLogs;

import java.util.ArrayList;
import java.util.List;

import com.ruseps.model.Item;
import com.ruseps.model.container.impl.Shop;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.DropChance;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.npc.impl.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_eight.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_five_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_four_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_six_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_seven_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_three_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_two_loot.*;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.*;

import com.ruseps.world.content.new_raids_system.raids_loot.*;
import com.ruseps.world.content.new_raids_system.instances.dark_demention_loot.*;
import com.ruseps.world.content.new_raids_system.instances.dragonstone_raids.*;
import com.ruseps.world.content.mboxes.*;

public enum CollectionLogs {
	DARIUS(CollectionLogType.BOSSES, 4263, new Item(915, 5)),
	HEATBLAST(CollectionLogType.BOSSES, 4266, new Item(20658, 1)),
	GOLDENKNIGHTS(CollectionLogType.BOSSES, 4272, new Item(621, 5)),
	DARKKNIGHTS(CollectionLogType.BOSSES, 3009, new Item(13016, 2)),
	CANONBOLT(CollectionLogType.BOSSES, 3010, new Item(18057, 1)),
	BADBITCH(CollectionLogType.BOSSES, 4274, new Item(8788, 100)),
	REDASSASIN(CollectionLogType.BOSSES, 3011, new Item(19992, 50)),
	EVILASSCLOWN(CollectionLogType.BOSSES, 3014, new Item(19990, 25)),
	YVATEL(CollectionLogType.BOSSES, 190, new Item(6855, 1)),
	DOOMSDAY(CollectionLogType.BOSSES, 185, new Item(21055, 1)),
	MOUNTAINDRWELLER(CollectionLogType.BOSSES, 4387, new Item(621, 5)),
	MISSMAGIC(CollectionLogType.BOSSES, 1506, new Item(19990, 25)),
	OBELISK(CollectionLogType.BOSSES, 1510, new Item(6855, 1)),
	BLOODQUEEN(CollectionLogType.BOSSES, 1508, new Item(6856, 1)),
	FOURARMS(CollectionLogType.BOSSES, 1509, new Item(13081, 1)),

	// MULTI BOSSES
	GALACTICTITAN(CollectionLogType.MULTIBOSSES, 1016, new Item(12502, 1)),
	OCTANE(true, CollectionLogType.MULTIBOSSES, 1417, OctaneLoot.allLoot, new Item(10934, 1)),
	LT_LIMES(CollectionLogType.MULTIBOSSES, 1017, new Item(21055, 2)),
	SILFERTHEDRAGON(CollectionLogType.MULTIBOSSES, 1018, new Item(6853, 1)),
	FALLENLORD(CollectionLogType.MULTIBOSSES, 1039, new Item(10935, 1)),
	KNIGHT_OF_TORMENT(CollectionLogType.MULTIBOSSES, 1038, new Item(21056, 2)),
	LITCH(CollectionLogType.MULTIBOSSES, 1494, new Item(21055, 3)),
	SPUDERMAN(CollectionLogType.MULTIBOSSES, 3012, new Item(6853, 2)),
	DR_STRANGE(CollectionLogType.MULTIBOSSES, 3007, new Item(21056, 2)),
	GODS_RULER(CollectionLogType.MULTIBOSSES, 1511, new Item(18689, 5)),
	GOLDEN_FREEZA(CollectionLogType.MULTIBOSSES, 5148, new Item(6854, 2)),

	// RAIDS
	POKEMON_RAID(CollectionLogType.RAIDS, "Pokemon Raids", RaidsOneChest.Loots, new Item(5585, 20)),
	ANIME_RAID(CollectionLogType.RAIDS, "Anime Raids", RaidsTwoChest.Loots, new Item(21013, 20)),
	MK_RAID(CollectionLogType.RAIDS, "MK Raids", RaidsThreeChest.Loots, new Item(10934, 1), new Item(2752, 30)),
	DD_RAID(CollectionLogType.RAIDS, "Dark Dimension Raids", DarkDementionLoot.Loots, new Item(13234, 20), new Item(10935, 1)),
	SILVER_RAID(CollectionLogType.RAIDS, "Silver Raids", RaidsFourChest.Loots, new Item(18872, 3), new Item(6856, 5)),
	GOLD_RAID(CollectionLogType.RAIDS, "Gold Raids", RaidsFiveChest.Loots, new Item(2750, 20), new Item(10934, 1)),
	PLAT_RAID(CollectionLogType.RAIDS, "Platinum Raids", RaidsSixChest.Loots, new Item(18941, 1), new Item(4015, 20), new Item(21055, 1)),
	DIAMOND_RAID(CollectionLogType.RAIDS, "Diamond Raids", RaidsSevenChest.Loots, new Item(18980, 1), new Item(10935, 1), new Item(6854, 1)),
	RUBY_RAID(CollectionLogType.RAIDS, "Ruby Raids", RaidsEightLoot.Loots, new Item(667, 1), new Item(10943, 1), new Item(85, 25)),
	DS_RAID(CollectionLogType.RAIDS, "Dragonstone Raids", DragonstoneRaidsLoot.Loots, new Item(275, 15), new Item(10943, 1)),
	
	// Globals
	DONATOR_BOSS(true, CollectionLogType.GLOBAL, 704, DonationBossLoot.allLoot, new Item(6852, 3)),
	BLESSED(true, CollectionLogType.GLOBAL, BlessedSpartanLoot.NPC_ID, BlessedSpartanLoot.allLoot, new Item(18060, 1)),
	DEMON(true, CollectionLogType.GLOBAL, DemonLoot.NPC_ID, DemonLoot.allLoot, new Item(10935, 1)),
	DRAGON(true, CollectionLogType.GLOBAL, DragonLoot.BLUE_NPC_ID, DragonLoot.allLoot, new Item(18980, 1)),
	INHERITED(true, CollectionLogType.GLOBAL, InheritedLoot.NPC_ID, InheritedLoot.allLoot, new Item(10935, 1)),
	REAPER(true, CollectionLogType.GLOBAL, ReaperLoot.NPC_ID, ReaperLoot.allLoot, new Item(1013, 1)),
	SMOOZIE(true, CollectionLogType.GLOBAL, SmooziesLoot.NPC_ID, DonationBossLoot.allLoot, new Item(10943, 1)),
	VADER(true, CollectionLogType.GLOBAL, VaderLoot.NPC_ID, VaderLoot.allLoot, new Item(6830, 1)),
	TELOS(true, CollectionLogType.GLOBAL, TelosLoot.BLUE_NPC_ID, TelosLoot.allLoot, new Item(10934, 1)),

	// Boxes
	M_BOX(false, CollectionLogType.BOXES, 915, MysteryBox.loot, new Item(600, 2)),
	PET_BOX(false, CollectionLogType.BOXES, 13016, PetBox.loot, new Item(21055, 4)),
	SECRET_BOX(false, CollectionLogType.BOXES, 21055, ScythiaBox.loot, new Item(21056, 2)),
	BRONZE_BOX(false, CollectionLogType.BOXES, 6855, BronzeBox.loot, new Item(6856, 3)),
	SILVER_BOX(false, CollectionLogType.BOXES, 6856, SilverBox.loot, new Item(6853, 3)),
	GOLD_BOX(false, CollectionLogType.BOXES, 6853, GoldBox.loot, new Item(6854, 2)),
	DIAMOND_BOX(false, CollectionLogType.BOXES, 6854, DiamondBox.loot, new Item(6852, 1)),
	RUBY_BOX(false, CollectionLogType.BOXES, 6852, RubyBox.loot, new Item(17916, 1)),
	OWNER_BOX(false, CollectionLogType.BOXES, 21056, OwnerBox.loot, new Item(18689, 10)),
	OWNER_KEY_CHEST(false,CollectionLogType.BOXES, 18689, new int[][] {
			{18338, 915, 18057, 10942, 18058, 18689, 18958, 8655, 8654, 10902,
				10900, 10901, 2746, 21043, 12601, 3499, 3502, 6855, 6856, 12502},
			{ 13016, 6199, 10934, 19111, 18060, 13035, 13034, 8676, 8668, 3523, 19077, 2732,
					10867, 10869, 19908, 19909, 6853, 12279, 12278 },
				{21056, 21055, 19002, 19003, 18061, 18960, 2996, 18063, 10935, 1413, 2786, 2805,
						2804, 2800, 19039, 3525, 10537, 2746, 13034 } },
			new Item(4067, 1)),
	SLAYER_KEY_CHEST(false,CollectionLogType.BOXES, 14471,
			new int[][] { { 19087, 19088, 19089, 19090, 19091, 19092, 6830, 2709, 15492, 19092, 915,
				935, 1053  },
					{14471, 15246, 9850, 7960, 6832, 2856, 2857, 17662, 17666, 2769, 2753, 3491,
					18057 },
					{6828, 2858, 2869, 2859, 13024, 13025, 3526, 3525, 13327, 1002, 13029, 8467, 8465, 18058} },
			new Item(21056, 6)),
	BARROWS(false,CollectionLogType.BOXES, 15378, new int[][] {
		{ 12001, 4740, 19992, 19994, 8788, 4708, 4710, 4712, 4714, 4716, 4718, 4720},
		{ 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753}, 
		{ 4755, 4757, 4759, 11846, 11848, 11850, 11852, 11854, 11856} },
		new Item(10934, 1)),
	;

	public CollectionLogType getType() {
		return type;
	}

	public void setType(CollectionLogType type) {
		this.type = type;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public boolean isAnnounce() {
		return announce;
	}

	public void setAnnounce(boolean announce) {
		this.announce = announce;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getUniqueItems() {
		return uniqueItems;
	}

	public void setUniqueItems(List<Integer> uniqueItems) {
		this.uniqueItems = uniqueItems;
	}

	public Item getReward() {
		return reward;
	}

	public void setReward(Item reward) {
		this.reward = reward;
	}

	CollectionLogs(CollectionLogType type, int npcID, Item reward) {
		this(type, npcID, true, reward);
	}

	CollectionLogs(boolean isNpc, CollectionLogType type, int npcID, int[][] loot, Item reward) {
		this(isNpc, type, npcID, loot, true, reward);
	}

	public CollectionLogs getCollectionLogs() {
		return this;
	}

	CollectionLogs(boolean isNpc, CollectionLogType type, int ID, int[][] loot, boolean announce, Item reward) {
		this.reward = reward;
		this.type = type;
		this.name = isNpc ? NpcDefinition.forId(ID).getName() : ItemDefinition.forId(ID).getName();
		this.npcId = isNpc ? ID : -1;
		this.itemId = isNpc ? itemId : ID;
		this.uniqueItems = new ArrayList<>();
		this.announce = announce;
		for (int[] lootRarity : loot) {
			for (int drop : lootRarity)
				if (!uniqueItems.contains(drop)) {
					uniqueItems.add(drop);
				}
		}
	}

	CollectionLogs(CollectionLogType type, int npcID, boolean announce, Item reward) {
		this.reward = reward;
		this.type = type;
		this.name = NpcDefinition.forId(npcID).getName();
		this.npcId = npcID;
		this.uniqueItems = new ArrayList<>();
		this.announce = announce;
		;
		NPCDrops npcDrops = NPCDrops.forId(npcId);
		if (npcDrops == null) {
			System.out.print("Error: cant find drops coll logs for " + this.name + " \n");
			return;
		}
		NPCDrops.NpcDropItem[] drops = npcDrops.getDropList();
		if (drops == null || drops.length == 0) {
			System.out.print("Error: cant find drops coll logs1 " + this.name + " \n");
			return;
		}
		for (NPCDrops.NpcDropItem npcDropItem : drops) {
			if (npcDropItem.getChance() != DropChance.ALWAYS) {
				if (!uniqueItems.contains(npcDropItem.getId())) {
					uniqueItems.add(npcDropItem.getId());
				}
			}
		}
	}

	CollectionLogs(CollectionLogType type, String name, int[][] loot, Item... reward) {
		this(type, name, loot, true, reward);
	}

	CollectionLogs(CollectionLogType type, String name, int[] loot, boolean announce, Item... reward) {
		this.reward = reward[0];
		this.type = type;
		this.name = name;
		this.npcId = -1;
		this.uniqueItems = new ArrayList<>();
		this.announce = announce;
		for (int item : loot) {
			if (!uniqueItems.contains(item)) {
				uniqueItems.add(item);
			}
		}
	}

	CollectionLogs(CollectionLogType type, String name, int[][] loot, boolean announce, Item... reward) {
		this.reward = reward[0];
		this.type = type;
		this.name = name;
		this.npcId = -1;
		this.uniqueItems = new ArrayList<>();
		this.announce = announce;
		for (int[] loots : loot) {
			for (int item : loots) {
				if (!uniqueItems.contains(item)) {
					uniqueItems.add(item);
				}
			}
		}
	}

	public int getKillCount(Player player) {
		if (type.ordinal() <= CollectionLogType.RAIDS.ordinal()) {
			return KillsTracker.getTotalKillsForNpc(npcId, player);
		}
		if (type == CollectionLogType.BOXES || type == CollectionLogType.GLOBAL) {
			for (CollectionLogManager.LogProgress progress : player.getCollectionLogManager().getLogProgress()) {
				if (progress.getName().equalsIgnoreCase(name())) {
					return progress.getCompleted();
				}
			}
		}
		return 0;
	}

	public static CollectionLogs forNpcId(int npcId) {
		for (CollectionLogs logs : CollectionLogs.values()) {
			if (logs.npcId == npcId)
				return logs;
		}
		return null;
	}

	public static CollectionLogs forItemId(int itemId) {
		for (CollectionLogs logs : CollectionLogs.values()) {
			if (logs.itemId == itemId)
				return logs;
		}
		return null;
	}

	public static CollectionLogs[] values1() {
		return CollectionLogs.values();
	}

	public static CollectionLogs valuesOf1(String name) {
		return CollectionLogs.valueOf(name);
	}

	public static CollectionLogs forName(String name) {
		for (CollectionLogs logs : CollectionLogs.values()) {
			if (logs.name().equalsIgnoreCase(name))
				return logs;
		}
		return null;
	}

	private CollectionLogType type;
	private int npcId;
	private int itemId;
	private boolean announce;
	private String name;
	private List<Integer> uniqueItems;
	private Item reward;

	public enum CollectionLogType {

		// MONSTERS, ZONES, BOSSES, GLOBAL, BOXES,
		BOSSES, MULTIBOSSES, RAIDS, GLOBAL, BOXES,;

		CollectionLogType() {
		}

		public CollectionLogType[] values1() {
			return CollectionLogType.values();
		}
	}

}
