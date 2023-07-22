package com.ruseps.world.content.dropchecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.DropChance;
import com.ruseps.model.definitions.NPCDrops.NpcDropItem;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.entity.impl.npc.impl.*;
import com.ruseps.world.entity.impl.npc.impl.SmooziesLoot;
import com.ruseps.world.entity.impl.player.Player;

public class NPCDropTableChecker {
    private static final NPCDropTableChecker SINGLETON = new NPCDropTableChecker();
    private final List<Integer> dropTableNpcIds = new ArrayList<Integer>();
    private final List<String> dropTableNpcNames = new ArrayList<String>();

    public NPCDropTableChecker() {
        initialize();
    }

    private void initialize() {
        for (int npcId : NPCDrops.getDrops().keySet()) {
            if (npcId < 0) {
                continue;
            }
            boolean addToList = true;
            String name = getNPCName(npcId);
            for (int i : getDropTableNpcIds()) {
                if (getNPCName(i).equalsIgnoreCase(name) || name.equals("null")) {
                    addToList = false;
                    break;
                }
            }
            if (addToList) {
                getDropTableNpcIds().add(npcId);
            }
        }
        /*SantaLoot.loadDrops();*/
        BlessedSpartanLoot.loadDrops();
        DragonLoot.loadDrops();
        TelosLoot.loadDrops();
        VaderLoot.loadDrops();
        AuroraLoot.loadDrops();
        DemonLoot.loadDrops();
        WreckedLoot.loadDrops();
        OctaneLoot.loadDrops();
        SmooziesLoot.loadDrops();


        getDropTableNpcIds().add(8540);
        getDropTableNpcIds().add(BlessedSpartanLoot.NPC_ID);
        getDropTableNpcIds().add(DragonLoot.BLUE_NPC_ID);
        getDropTableNpcIds().add(TelosLoot.GREEN_NPC_ID);
        getDropTableNpcIds().add(VaderLoot.NPC_ID);
        getDropTableNpcIds().add(AuroraLoot.NPC_ID);
        getDropTableNpcIds().add(DemonLoot.NPC_ID);
        getDropTableNpcIds().add(WreckedLoot.NPC_ID);
        getDropTableNpcIds().add(OctaneLoot.NPC_ID);
        getDropTableNpcIds().add(SmooziesLoot.NPC_ID);
        Collections.sort(getDropTableNpcIds(), (Integer s1, Integer s2) -> getNPCName(s1).compareTo(getNPCName(s2)));
        for (int npcId : getDropTableNpcIds()) {
            dropTableNpcNames.add(getNPCName(npcId));
        }
    }

    public void refreshDropTableChilds(final Player player) {
        int childId = 37651;
        for (String npcName : dropTableNpcNames) {
            player.getPacketSender().sendFrame126(npcName, childId++);
        }
        for (int child = childId; child < 37821; child++) {
            player.getPacketSender().sendFrame126("", child);
        }
    }

    public boolean handleButtonClick(final Player player, final int actionId) {
        if (actionId >= -27885 && actionId <= -27716) {
            int index = actionId + 27885;
            if (index >= getDropTableNpcIds().size() || getDropTableNpcIds().get(index) == null) {
                return true;
            }
            showNPCDropTable(player, getDropTableNpcIds().get(index));
            player.getPacketSender().sendFrame126("@or1@You are now viewing the @whi@"
                    + dropTableNpcNames.get(index) + "@or1@ drop table.", 37605);
            player.npcDropTableDelay = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public static int arrayMax(int[] arr) {
        int max = Integer.MIN_VALUE;

        for (int cur : arr)
            max = Math.max(max, cur);
        return max;
    }

    public static void derp(final Player player) {
        player.sendMessage("vefrvrev");
    }

    public void showNPCDropTable(final Player player, final int npcId) {
        player.getPacketSender().resetItemsOnInterface(37915, 60);
        player.getPacketSender().resetItemsOnInterface(37916, 60);
        player.getPacketSender().resetItemsOnInterface(37917, 60);
        player.getPacketSender().resetItemsOnInterface(37918, 60);
        player.getPacketSender().resetItemsOnInterface(37919, 60);
        player.getPacketSender().resetItemsOnInterface(37920, 60);
        player.getPacketSender().resetItemsOnInterface(37921, 60);
        player.getPacketSender().resetItemsOnInterface(37922, 60);
        Map<DropChance, List<Item>> dropTables = new HashMap<>();
        System.out.println("Drops for " + npcId);
        NPCDrops drops = NPCDrops.forId(npcId);
        System.out.println("Drops: " + drops);
        NpcDropItem[] items = drops.getDropList();

        for (NpcDropItem item : items) {
            DropChance chance = item.getChance();
            List<Item> table = dropTables.get(chance);

            if (drops == null) {
                player.sendMessage("This NPC has no drops.");
                return;
            }
            if (table == null) {
                dropTables.put(chance, table = new ArrayList<Item>());
            }
            boolean found = false;

            for (Item drop : table) {
                if (drop != null && drop.getId() == item.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                int amount = arrayMax(item.getCount());

                if (amount >= 1 && amount <= Integer.MAX_VALUE) {
                    table.add(new Item(item.getId(), amount));
                }
            }
        }

        for (Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {
            DropChance chance = entry.getKey();
            List<Item> dropTable = entry.getValue();

            if (dropTable.size() == 0) {
                continue;
            }

            if (chance == DropChance.ALWAYS) {
                player.getPacketSender().sendItemsOnInterface(37915, 60,
                        dropTable, false);
            } else if (chance == DropChance.COMMON) {
                player.getPacketSender().sendItemsOnInterface(37916, 60,
                        dropTable, false);
            } else if (chance == DropChance.UNCOMMON) {
                player.getPacketSender().sendItemsOnInterface(37917, 60,
                        dropTable, false);
            } else if (chance == DropChance.RARE) {
                player.getPacketSender().sendItemsOnInterface(37918, 60,
                        dropTable, false);
            } else if (chance == DropChance.LEGENDARY
                    || chance == DropChance.LEGENDARY) {
                player.getPacketSender().sendItemsOnInterface(37919, 60,
                        dropTable, false);
            } else if (chance == DropChance.LEGENDARY_2) {
                player.getPacketSender().sendItemsOnInterface(37920, 60,
                        dropTable, false);
            } else if (chance == DropChance.LEGENDARY_3) {
                player.getPacketSender().sendItemsOnInterface(37921, 60,
                        dropTable, false);
            } else if (chance == DropChance.LEGENDARY_4) {
                player.getPacketSender().sendItemsOnInterface(37922, 60,
                        dropTable, false);
            }
        }
        player.getPacketSender().sendInterface(37600);
    }

    private String getNPCName(int npcId) {
        NpcDefinition def = NpcDefinition.forId(npcId);
        if (def == null) {
            return "null";
        }
        return def.getName();
    }

    public static NPCDropTableChecker getSingleton() {
        return SINGLETON;
    }

    public List<Integer> getDropTableNpcIds() {
        return dropTableNpcIds;
    }
}