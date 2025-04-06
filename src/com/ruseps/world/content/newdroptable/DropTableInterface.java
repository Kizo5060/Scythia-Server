package com.ruseps.world.content.newdroptable;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.DropUtils;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DropTableInterface {

    private static DropTableInterface instance = null;

    public static DropTableInterface getInstance() {
        if (instance == null) {
            instance = new DropTableInterface();
        }
        return instance;
    }

    public void open(@NotNull Player player) {
        player.getPacketSender().sendInterface(22220);
        initNpcList();
        sendNpcs(player);

    }

    private final List<NpcDefinition> initialNpcs = new ArrayList<>();
    private final List<NpcDefinition> npcs = new ArrayList<>();

    public boolean handleButton(Player player, int buttonId) {
        if (!(buttonId >= 22420 && buttonId <= 22569)) {
            return false;
        }
        int index = -22420 + buttonId;
        //player.sendMessage("Index: " + index);
        if (npcs.size() > index) {
            showData(player, index);
        }


        return true;
    }

    public void loadNpcs() {
        Arrays.stream(NpcDefinition.getDefinitions())
                .filter(Objects::nonNull)
                .filter(def -> NPCDrops.forId(def.getId()) != null)
                .filter(def -> def.getHitpoints() >= 10000)
                .sorted(Comparator.comparingLong(NpcDefinition::getHitpoints).reversed())
                .forEach(initialNpcs::add);
    }

    public void search(Player player, String name) {
        initNpcList();
        npcs.removeIf(def -> !def.getName().toLowerCase().contains(name.toLowerCase()));
        sendNpcs(player);
    }

    public void select(Player player, NpcDefinition def) {
        initNpcList();
        sendNpcs(player);
        boolean hasDrops = showData(player, def);
        if (hasDrops) {
            player.getPacketSender().sendInterface(22220);
        } else {
            player.sendMessage("This npc has no drops");
        }
    }

    private final int ITEM_CONTAINER_ID = 22985;
    private final int NAME_ID = 22884;
    private final int AMOUNT_ID = 22682;
    private final int CHANCE_ID = 22783;
    private final int SPRITE_COMPONENT_ID = 22581;

    private void showData(@NotNull Player player, int index) {
        NpcDefinition npc = npcs.get(index);
        player.getPacketSender().sendString(22227,"Viewing: " + npc.getName());
        int npcId = npc.getId();
        NPCDrops.NpcDropItem[] drops = NPCDrops.forId(npcId).getDropList();
        Arrays.sort(drops, Comparator.comparing(NPCDrops.NpcDropItem::getChance));
        int slot = 0;
        player.getPacketSender().resetItemsOnInterface(ITEM_CONTAINER_ID, 150);
        resetStrings(player);
        Map<Integer, String> stringMap = new HashMap<>();
        player.getPacketSender().sendScrollMax(22580, drops.length < 7 ? 276 : drops.length * 39);
        List<Item> items = new ArrayList<>();
        for (NPCDrops.NpcDropItem drop : drops) {
            Item item = drop.getItem();
            items.add(new Item(item.getId(), item.getAmount()));
            stringMap.put(NAME_ID + slot, item.getDefinition().getName());
            stringMap.put(AMOUNT_ID + slot, item.getAmount() < 1000 ? (item.getAmount() + "x") : Misc
                    .formatNumber(item.getAmount()));
            int playerDr = DropUtils.drBonus(player);

            int chance =  drop.getChance().getRandom();
            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player) / 100.0) + 1 : 1))
                    + (playerDr > 0 ? 1 : 0);
            if(player.isApplyDr())
            	stringMap.put(CHANCE_ID + slot, "1/" + adjustedDr);
            else
            	stringMap.put(CHANCE_ID + slot, "1/" + chance);
            int spriteId = getRaritySprite(drop.getChance().getRandom());
            if (spriteId != -1) {
                player.getPacketSender().sendSpriteChange(SPRITE_COMPONENT_ID + slot, spriteId);
            }
            slot++;
        }

        player.getPacketSender()
                .sendItemsOnInterfaceNew(ITEM_CONTAINER_ID, items);

        stringMap.forEach((id, str) -> player.getPacketSender().sendString(id, str));

    }

    private boolean showData(@NotNull Player player, NpcDefinition npc) {
        int npcId = npc.getId();
        NPCDrops npcDrops = NPCDrops.forId(npcId);
        if(npcDrops == null) {
            return false;
        }
        NPCDrops.NpcDropItem[] drops = npcDrops.getDropList();
        if (drops == null || drops.length == 0) {
            return false;
        }
        player.getPacketSender().sendString(22227,"Viewing: " + npc.getName());
        System.out.println(drops.length);
        Arrays.sort(drops, Comparator.comparing(NPCDrops.NpcDropItem::getChance));
        int slot = 0;
        player.getPacketSender().resetItemsOnInterface(ITEM_CONTAINER_ID, 150);
        resetStrings(player);
        Map<Integer, String> stringMap = new HashMap<>();
        player.getPacketSender().sendScrollMax(22580, drops.length < 7 ? 276 : drops.length * 39);
        List<Item> items = new ArrayList<>();
        for (NPCDrops.NpcDropItem drop : drops) {
            Item item = drop.getItem();
            items.add(new Item(item.getId(), item.getAmount()));
            stringMap.put(NAME_ID + slot, item.getDefinition().getName());
            stringMap.put(AMOUNT_ID + slot, item.getAmount() < 1000 ? (item.getAmount() + "x") : Misc
                    .formatNumber(item.getAmount()));
            int playerDr = DropUtils.drBonus(player);
            int chance =  drop.getChance().getRandom();
            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player) / 100.0) + 1 : 1))
                    + (playerDr > 0 ? 1 : 0);
            if(player.isApplyDr())
            	stringMap.put(CHANCE_ID + slot, "1/" + adjustedDr);
            else
            	stringMap.put(CHANCE_ID + slot, "1/" + chance);
            int spriteId = getRaritySprite(drop.getChance().getRandom());
            if (spriteId != -1) {
                player.getPacketSender().sendSpriteChange(SPRITE_COMPONENT_ID + slot, spriteId);
            }
            slot++;
        }

        player.getPacketSender()
                .sendItemsOnInterfaceNew(ITEM_CONTAINER_ID, items);

        stringMap.forEach((id, str) -> player.getPacketSender().sendString(id, str));


        return true;

    }

    private void resetStrings(Player player) {

        for(int i = NAME_ID; i < NAME_ID + 100; i++) {
            player.getPacketSender().sendString(i, "");
        }

        for(int i = AMOUNT_ID; i < AMOUNT_ID + 100; i++) {
            player.getPacketSender().sendString(i, "");
        }

        for(int i = CHANCE_ID; i < CHANCE_ID + 100; i++) {
            player.getPacketSender().sendString(i, "");
        }
    }


    private void initNpcList() {
        npcs.clear();
        npcs.addAll(initialNpcs);
    }

    private int getRaritySprite(int chance) { // sprite id based on the chance
        switch (chance) {
            case 0:
            case 2:
                return 1315;
            case 5:
            case 15:
                return 1316;
            case 25:
            case 40:
                return 1317;
            case 75:
            case 125:
                return 1318;
            case 150:
                return 1319;
            case 250:
                return 1320;
            case 400:
                return 1321;
            case 1000:
                return 1322;
            case 2000:
                return 1323;
            case 3500:
                return 1324;
            default:
                return 1315;
        }
    }

    private int textStart = 22420;

    private void sendNpcs(Player player) {
        textStart = 22420;
        for(int i = textStart; i < textStart + 100; i++) {
            player.getPacketSender().sendString(i, "");
        }
        Map<Integer, String> stringMap = new HashMap<>();

        npcs.forEach(npc -> {
            stringMap.put(textStart++, npc.getName());
        });

        stringMap.forEach((id, str) -> player.getPacketSender().sendString(id, str));
    }

}
