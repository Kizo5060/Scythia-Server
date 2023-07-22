package com.ruseps.model.definitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ruseps.GameSettings;
import com.ruseps.model.GameMode;
import com.ruseps.model.Graphic;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.util.JsonLoader;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.content.DropLog;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.DropLog.DropLogEntry;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.minigames.impl.WarriorsGuild;
import com.ruseps.world.content.skill.impl.prayer.BonesData;
import com.ruseps.world.content.skill.impl.summoning.CharmingImp;
import com.ruseps.world.content.skill.impl.summoning.Familiar;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.VaderLoot;
import com.ruseps.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class NPCDrops {

    /**
     * The map containing all the npc drops.
     */
    private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

    public static JsonLoader parseDrops() {
        // System.out.println("CALLED");
        ItemDropAnnouncer.init();
        ValetinesItemDropAnnouncer.init();

        return new JsonLoader() {

            @Override
            public void load(JsonObject reader, Gson builder) {
                int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
                NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);
                NPCDrops d = new NPCDrops();
                d.npcIds = npcIds;
                d.drops = drops;
                for (int id : npcIds) {
                    dropControllers.put(id, d);
                }

            }

            @Override
            public String filePath() {
                return "./data/def/json/drops.json";
            }
        };
    }

    /**
     * The id's of the NPC's that "owns" this class.
     */
    private int[] npcIds;

    /**
     * All the drops that belongs to this class.
     */
    private NpcDropItem[] drops;

    /**
     * Gets the NPC drop controller by an id.
     *
     * @return The NPC drops associated with this id.
     */
    public static NPCDrops forId(int id) {
        return dropControllers.get(id);
    }


    public static Map<Integer, NPCDrops> getDrops() {
        return dropControllers;
    }

    /**
     * Gets the drop list
     *
     * @return the list
     */
    public NpcDropItem[] getDropList() {
        return drops;
    }

    /**
     * Gets the npcIds
     *
     * @return the npcIds
     */
    public int[] getNpcIds() {
        return npcIds;
    }

    /**
     * Represents a npc drop item
     */
    public static class NpcDropItem {

        /**
         * The id.
         */
        private final int id;

        /**
         * Array holding all the amounts of this item.
         */
        private final int[] count;

        /**
         * The chance of getting this item.
         */
        private final int chance;

        /**
         * New npc drop item
         *
         * @param id     the item
         * @param count  the count
         * @param chance the chance
         */
        public NpcDropItem(int id, int[] count, int chance) {
            this.id = id;
            this.count = count;
            this.chance = chance;
        }

        public int getId() {
            return id;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public int[] getCount() {
            return count;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public DropChance getChance() {
            switch (chance) {
                case 1:
                    return DropChance.ALMOST_ALWAYS; // 50% <-> 1/2
                case 2:
                    return DropChance.VERY_COMMON; // 20% <-> 1/5
                case 3:
                    return DropChance.COMMON; // 5% <-> 1/20
                case 4:
                    return DropChance.UNCOMMON; // 2% <-> 1/50
                case 5:
                    return DropChance.RARE; // 0.5% <-> 1/200
                case 6:
                    return DropChance.LEGENDARY; // 0.2% <-> 1/500
                case 7:
                    return DropChance.LEGENDARY_2;
                case 8:
                    return DropChance.LEGENDARY_3;
                case 9:
                    return DropChance.LEGENDARY_4;
                case 10:
                    return DropChance.LEGENDARY_5;
                default:
                    return DropChance.ALWAYS; // 100% <-> 1/1
            }

        }

        public WellChance getWellChance() {
            switch (chance) {
                case 1:
                    return WellChance.ALMOST_ALWAYS; // 50% <-> 1/2
                case 2:
                    return WellChance.VERY_COMMON; // 20% <-> 1/5
                case 3:
                    return WellChance.COMMON; // 5% <-> 1/20
                case 4:
                    return WellChance.UNCOMMON; // 2% <-> 1/50
                case 5:
                    return WellChance.RARE; // 0.5% <-> 1/200
                case 6:
                    return WellChance.LEGENDARY; // 0.2% <-> 1/500
                case 7:
                    return WellChance.LEGENDARY_2;
                case 8:
                    return WellChance.LEGENDARY_3;
                case 9:
                    return WellChance.LEGENDARY_4;
                case 10:
                    return WellChance.LEGENDARY_5;
                default:
                    return WellChance.ALWAYS; // 100% <-> 1/1

            }
        }

        /**
         * Gets the item
         *
         * @return the item
         */
        public Item getItem() {
            int amount = 0;
            for (int i = 0; i < count.length; i++)
                amount += count[i];
            if (amount > count[0])
                amount = count[0] + RandomUtility.getRandom(count[1]);
            return new Item(id, amount);
        }
    }

    public enum DropChance {
        ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(50), COMMON(100), UNCOMMON(150), NOTTHATRARE(75), RARE(200),
        LEGENDARY(1000), LEGENDARY_2(2500), LEGENDARY_3(3500), LEGENDARY_4(4500), LEGENDARY_5(5500);

        DropChance(int randomModifier) {
            this.random = randomModifier;
        }

        private int random;

        public int getRandom() {
            return this.random;
        }
    }

    public enum WellChance {
        ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(3), COMMON(8), UNCOMMON(20), NOTTHATRARE(50), RARE(200), LEGENDARY(750),
        LEGENDARY_2(1000), LEGENDARY_3(2000), LEGENDARY_4(340), LEGENDARY_5(450);

        WellChance(int randomModifier) {
            this.random = randomModifier;
        }

        private int random;

        public int getRandom() {
            return this.random;
        }
    }

    /**
     * Drops items for a player after killing an npc. A player can max receive one
     * item per drop chance.
     *
     * @param p   Player to receive drop.
     * @param npc NPC to receive drop FROM.
     */
    public static void dropItems(Player p, NPC npc) {
        if (npc.getLocation() == Location.WARRIORS_GUILD)
            WarriorsGuild.handleDrop(p, npc);
        NPCDrops drops = NPCDrops.forId(npc.getId());
        if (drops == null)
            return;
        final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
        final Position npcPos = npc.getPosition().copy();
        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {

            casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
        }
   
        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {

        }
		
        if (npc.getDefaultConstitution() > 10000) {
            System.out.println("DROPPING");
            dropScratchcard(p, p.getPosition());
        }

        rollDropTable(false, p, drops.getDropList().clone(), npc, npcPos, goGlobal);
    }

    public static Collection<Integer> getNpcList(String name) {
        List<Integer> items = new ArrayList<>();
        for (int index = 0; index < ItemDefinition.getDefinitions().length; index++) {
            ItemDefinition def = ItemDefinition.forId(index);
            if (def == null || !def.getName().toLowerCase().equals(name)) {
                continue;
            }
            items.add(def.getId());
            break;
        }
        if (items.isEmpty()) {
            System.out.println("No such item.");
            return new ArrayList<>();
        }
        Collection<Integer> npcs = new ArrayList<>();
        for (NPCDrops npc_drop : dropControllers.values()) {
            for (NpcDropItem dropped_item : npc_drop.drops) {
                for (Integer cached_item : items) {
                    if (dropped_item.getId() == cached_item) {
                        for (int npc_id : npc_drop.getNpcIds()) {
                            if (npcs.contains(npc_id)) {
                                continue;
                            }
                            npcs.add(npc_id);
                        }
                        continue;
                    }
                }
            }
        }
        return npcs;
    }

    public static void rollDropTable(boolean isWell, Player player, NpcDropItem[] drops, NPC npc, Position npcPos,
            boolean goGlobal) {
        boolean hasRecievedDrop = false;
        int playerDr = DropUtils.drBonus(player);
        int random = 0;
        if (player.getSummoning().getFamiliar() != null) {
            random += player.getNpcDropRate();
        }
        System.out.println("Player dr: " + playerDr);


        System.out.println("Player dr after: " + playerDr);

        Arrays.sort(drops, (a, b) -> b.getChance().compareTo(a.getChance()));

        for (int i = 0; i < drops.length; i++) {
            int chance = isWell ? drops[i].getWellChance().getRandom() : drops[i].getChance()
                    .getRandom();
            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player) / 100.0) + 1 : 1))
                    + (playerDr > 0 ? 1 : 0);

            // System.out.println("Item: " + drops[i].getItem().getDefinition().getName() +
            // "x" + drops[i].getItem().getAmount() + " chance: " + chance + " adj chance: "
            // + adjustedDr);

            if (drops[i].getChance() == DropChance.ALWAYS || adjustedDr == 1) {
                Item drop = drops[i].getItem();
                if (player.isDoubleRateActive())
                    drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
                drop(player, drop, npc, npcPos, goGlobal);
                System.out.println("Drop: " + drop.getId() + ", " + drop.getAmount());
            } else if (RandomUtility.getRandom(adjustedDr) == 1 && !hasRecievedDrop) {
                Item drop = drops[i].getItem();
                if (player.isDoubleRateActive())
                    drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
                drop(player, drop, npc, npcPos, goGlobal);
                System.out.println("Drop1: " + drop.getId() + ", " + drop.getAmount());
                hasRecievedDrop = true;
            }

        }

    }

    public static int getDropAmntMult(Player player) {
        int multiplier = 1;


        return multiplier;
    }

    public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {
        int itemId = item.getId();
        int amount = item.getAmount();
        Player toGive = player;

        if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
            player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
            player.getSkillManager()
                    .addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP());
            return;
        }

        if (itemId == CharmingImp.GOLD_CHARM || itemId == CharmingImp.GREEN_CHARM || itemId == CharmingImp.CRIM_CHARM
                || itemId == CharmingImp.BLUE_CHARM) {
            if (player.getInventory()
                    .contains(6500) && CharmingImp.handleCharmDrop(player, itemId, amount)) {
                return;
            }
        }
        boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);
        //boolean isWearingCollectorPet = hasCollPetSummoned(player);
        int itemIdd = item.getId();
        int amountt = item.getAmount();

        if (isWearingCollector) {
            if (ItemDropAnnouncer.announce(itemId)) {
                String itemName = item.getDefinition().getName();
                String itemMessage = Misc.anOrA(itemName) + " " + itemName;
                String npcName = Misc.formatText(npc.getDefinition().getName());
                String kills = Misc.insertCommasToNumber(String.valueOf(KillsTracker.getTotalKillsForNpc(npc.getId(), player)));
                String message = "<img=31><col=960a02><shad=1>[LOOT]<img=31> <col=5c26e5>" + toGive.getUsername()
                        + " @red@received <col=5c26e5>" + itemMessage + "<col=960a02><shad=1> from <col=5c26e5>" + npcName + "!"
                        + " @bla@(@red@" + kills + " Kills@bla@)";
                if (player.kcMessage != false) {
                    World.sendMessage(message);
                }
            }

            player.addItemToAny(itemIdd, amountt);
            
            DropLog.submit(toGive, new DropLogEntry(itemIdd, amountt));
            return;
        }
		/*if (isWearingCollectorPet) 
		{
			if(player.getInventory().getFreeSlots() > 0) {
				player.getInventory().add(itemIdd, amountt);
			} else { player.getBank(0).add(itemIdd, amountt);}
				//player.getBank().add(itemIdd, amountt); } 
			player.sendMessage("@red@Your Collector Pet has picked up " + item.getAmount() + "x "
					+ item.getDefinition().getName() + " and added them to your "
					+ (player.getInventory().getFreeSlots() <= 0 ? "bank" : "inventory"));
			DropLog.submit(toGive, new DropLogEntry(itemIdd, amountt));
			return;
		}*/
        if (ValetinesItemDropAnnouncer.valentinesAnnounce(itemId)) {
            String itemName = item.getDefinition().getName();
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            String npcName = Misc.formatText(npc.getDefinition().getName());

            String kills = Misc.insertCommasToNumber(String.valueOf(KillsTracker.getTotalKillsForNpc(npc.getId(), player)));
            String message = "<img=31><col=960a02><shad=1>[VALENTINES LOOT]<img=31> <col=5c26e5>" + toGive.getUsername()
                    + " @red@received <col=5c26e5>" + itemMessage + "<col=960a02><shad=1> from <col=5c26e5>" + npcName + "!"
                    + " @bla@(@red@" + kills + " Kills@bla@)";
            if (player.kcMessage != false) {
                World.sendMessage(message);
            }
        }
        if (ItemDropAnnouncer.announce(itemId)) {
            String itemName = item.getDefinition().getName();
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            String npcName = Misc.formatText(npc.getDefinition().getName());

            switch (itemId) {
                case 14484:
                    itemMessage = "a pair of Dragon Claws";
                    break;
                case 20000:
                case 20001:
                case 20002:
                    itemMessage = itemName;
                    break;
            }
            switch (npc.getId()) {
                case 50:
                case 3200:
                case 8133:
                case 4540:
                case 1160:
                case 8549:
                    npcName = "The " + npcName + "";
                    break;
                case 51:
                case 54:
                case 5363:
                case 8349:
                case 1592:
                case 1591:
                case 1590:
                case 1615:
                case 9463:
                case 9465:
                case 9467:
                case 1382:
                case 13659:
                case 11235:
                    npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
                    break;
            }
            String kills = Misc.insertCommasToNumber(String.valueOf(KillsTracker.getTotalKillsForNpc(npc.getId(), player)));
            String message = "<img=31><col=960a02><shad=1>[LOOT]<img=31> <col=5c26e5>" + toGive.getUsername()
                    + " @red@received <col=5c26e5>" + itemMessage + "<col=960a02><shad=1> from <col=5c26e5>" + npcName + "!"
                    + " @bla@(@red@" + kills + " Kills@bla@)";
            if (player.kcMessage != false) {
                World.sendMessage(message);
            }
            PlayerLogs.log(toGive.getUsername(),
                    "" + toGive.getUsername() + " received "
                            + itemMessage
                            + " from "
                            + npcName
                            + "");
        }
        
        GroundItemManager.spawnGroundItem(toGive,
                new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
        player.getCollectionLog().handleDrop(player, npc.getId(), item);
        DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
    }

    public static void casketDrop(Player player, int combat, Position pos) {
        int chance = Misc.getRandom(50_000);
        boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);

        if (chance >= 49_999) {
            if (isWearingCollector) {
                player.addItemToAny(915, 1);
                return;
            }
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(915), pos, player.getUsername(), false, 150, true, 200));
        }
    }

    private static void dropScratchcard(Player player, Position pos) {
        int chance = RandomUtility.inclusiveRandom(0, 1000);

        if (chance <= 999) {
            return;
        }

        GroundItemManager.spawnGroundItem(player,
                new GroundItem(new Item(455, 1), pos, player.getUsername(), false, 150, true, 200));

        player.sendMessage("@red@Congrats, you have gotten a scratchcard, good luck!");
    }

    public static class ValetinesItemDropAnnouncer {

        private static List<Integer> VALENTINES_ITEM_LIST;

        private static final int[] VALENTINES_TO_ANNOUNCE = new int[]{
                2731, 2732
        };

        private static void init() {
            VALENTINES_ITEM_LIST = new ArrayList<Integer>();
            for (int i : VALENTINES_TO_ANNOUNCE) {
                VALENTINES_ITEM_LIST.add(i);
            }
        }

        public static boolean valentinesAnnounce(int item) {
            return VALENTINES_ITEM_LIST.contains(item);
        }
    }

    public static class ItemDropAnnouncer {

        private static List<Integer> ITEM_LIST;

        private static final int[] TO_ANNOUNCE = new int[]{20120, 20122, 20124, 17291, 7080,
                10502, 19920, 941, 944, 20130, 20128, 20132, 20134, 16137,
                20519, 20520, 20522, 20523, 21000, 21001, 21002, 21003, 21034, 1002, 21017, 21018, 21019, 21020, 21021, 21022,
                18852, 18854, 18856, 18858, 18860, 18864, 18868, 21029, 21030, 21031, 21032, 21033, 21023, 21024, 21025, 21026, 21027, 21028,
                20106, 20108, 20110, 20112, 20114, 20116, 21007, 21008, 21009, 21010, 21011, 21012, 21004, 21005, 21006,
                20150, 20154, 20156, 20158, 20160, 20162, 14018, 19098, 19100, 19099, 19093, 19096, 938, 19097, 915,
                18990, 18992, 18994, 18998, 19000, 19980,
                12279, 18448, 18920, 18926, 18950, 6659, 6661, 6663,
                18446, 18910, 18942, 13045, 19085,
                18944, 18912, 18444, 13047, 8421,
                5079, 5080, 5081, 13235, 7671, 6763, 10903, 10905, 10906, 19918, 11005,
                13239, 19921, 10503, 11000, 11654,
                10682, 10683, 10684, 10685, 11659, 11653,
                11147, 11148, 11149, 11614, 11650, 7081,
                11039, 11040, 11041, 11042, 11004, 20146, 15485,
                10991, 10992, 10993, 10994, 10995, 940, 10480,
                10822, 11208, 10824, 11206, 10826, 7082,
                798, 799, 894, 895, 896, //bad bitch
                8860, 8861, 8862, 8860, //Cannonbolt Pieces
                20100, 20102, 20104, 11896,
                20140, 20136, 20138, 20142, 20144, 10502,
                15247, 15248, 15249, 15253, 15254, 15255, 15256,
                11658, 5090, 5091, 5092, 20658, 20659, 20660,
                20650, 20651, 20652, 20653, 20654, 937,
                5085, 5086, 5087, 5088, 5089, 11651,
                7085, 11063, 12708, 5082, 5083, 5084,
                79, 81, 82, 83, 8517, 8518, 8519,
                798, 799, 894, 895, 896, 938,
                8860, 8861, 8862, 19919, 8871,
                8863, 8864, 8865, 11991,
                8660, 8656, 8658, 8659, 8657,
                11661, 11662, 11679, 11680, 11681, 11682, 20656,
                19067, 19068, 19069, 19070, 19071, 19072,
                19048, 19049, 19050, 19051, 19052, 19053, 19055, 19054,
                21065, 21066, 21067, 21068, 21069, 21071,
                21072, 21073, 21074, 21075, 21076, 21077, 21039,
                21078, 21079, 21080, 21081, 21082, 21083, 21084,
                18850, 18862, 18866, 18870, 18997, 19952, 19954,
                19084, 19085, 19082, 19077,
                18057, 2701, 2702, 2704, 2705, 2706, 2707, 2708, 10942,
                2677, 2678, 2806, 2807, 2808, 2809, 2810, 2811, 18943, 18947, 18948, 18941, 2703,
                2679, 3490, 3491, 3492, 3493, 3494, 18966, 2680, 2812, 2814, 2815, 2816, 2817, 2818,
                18949, 18951, 18952, 18953, 18954, 18955, 18956,
                19007, 19008, 19009, 19010, 19011, 19013,
                11039, 11040, 11041, 11042, 11147, 11148, 11149,
                10822, 11208, 10824, 11206, 10826, 18448, 18950, 18920, 15501, 6183,
                2739, 2740, 2741, 2742, 2743, 2744,
                2734, 2735, 2736, 2737, 2738, 2745, 2746,
                3511, 3512, 3415, 3514, 3516, 3513,
                3517, 3518, 3519, 3520, 3521, 3522, 3523, 3524
        };// All Rare
        // Boss
        // Drops};

        private static void init() {
            ITEM_LIST = new ArrayList<Integer>();
            for (int i : TO_ANNOUNCE) {
                ITEM_LIST.add(i);
            }
        }

        public static boolean announce(int item) {
            return ITEM_LIST.contains(item);
        }
    }

    public void setDrops(NpcDropItem[] dropItems) {
        this.drops = dropItems;
    }
}