package com.ruseps.world.content.bis;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.world.entity.impl.player.Player;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class BestInSlotInterface {

    private final Player player;

    public BestInSlotInterface(Player player) {
        this.player = player;
    }

    private final int STARTING_POINT = 27245;

    private int classIndex = 0;
    private int categoryIndex = 0;
    private int viewedIndex = 0;

    private final int[] categoryItemIds = {2774, 2788, 2778, 2785, 2776, 2786, 2782, 2790, 2780, 2783};

    private static Map<ItemDefinition.EquipmentType, List<ItemDefinition>> items;

    public static void loadAllItems() {
        items = Arrays.stream(ItemDefinition
                        .getDefinitions())
                .filter(Objects::nonNull)
                .collect(groupingBy(ItemDefinition::getEquipmentType));
    }

    private List<ItemDefinition> loadedDefinitions;
    private ItemDefinition selectedDefinition;

    private final int[] ignoredIds = {21898, 21907, 21903, 21904, 21908, 21883, 21913, 16554};

    public void open() {

        sendInterfaceData();
        player.getPacketSender().sendInterface(STARTING_POINT);
        loadItems();
    }

    public String getSpeedText() {
        WeaponInterfaces.WeaponInterface weaponInterface = WeaponInterfaces.getById(selectedDefinition.getId());
        int speed = weaponInterface != null ? weaponInterface.getSpeed() : -1;
        switch (speed) {
            case 0:
                return "Fastest";
            case 1:
                return "2nd Fastest";
            case 2:
                return "3rd Fastest";
            case 3:
                return "4th Fastest";
            case 4:
                return "5th Fastest";
            case 5:
                return "6th Fastest";
            case 6:
                return "7th Fastest";
            default:
                return "NONE";
        }
    }


    private void sendInterfaceData() {
        player.getPacketSender().sendInterfaceIntArray(STARTING_POINT + 12, categoryItemIds);
    }

    private void loadItems() {
        ItemDefinition.EquipmentType type = getEquipmentType();
        List<ItemDefinition> data = items.get(type)
                .stream()
                .filter(this::isValid)
                .filter(def -> !def.isNoted())
                .sorted(Comparator.<ItemDefinition>comparingInt(def -> def.getPrimaryBonusSum(classIndex, categoryIndex))
                        .reversed()).limit(40).collect(toList());
        int[] ids = data.stream()
                .mapToInt(ItemDefinition::getId)
                .toArray();
        loadedDefinitions = data;
        List<String> strings = new ArrayList<>();
        IntStream.range(0, data.size()).forEach(i -> {
            strings.add(data.get(i).getName());
        });
        int textIndex = STARTING_POINT + 164;
        for(int i = textIndex; i < textIndex + 150; i++) {
            player.getPacketSender().sendString(i, "");
        }
        player.getPacketSender().sendInterfaceIntArray(STARTING_POINT + 314, ids);
        for(int i = 0; i < strings.size(); i++) {
            player.getPacketSender().sendString(textIndex + i, strings.get(i));
        }
        viewedIndex = 0;
        player.getPacketSender().sendToggle(3200, 0);
        handleItemSelection();

    }

    private boolean isValid(ItemDefinition definition) {
        if (Arrays.stream(ignoredIds).anyMatch(i -> i == definition.getId()) || definition.getName().toLowerCase().contains("thre triangles") || definition.getName().toLowerCase().contains("todo") || definition.getName().toLowerCase().contains("lime rex") || definition.getName()
                .toLowerCase()
                .contains("(rename this)") || definition.getName()
                .toLowerCase()
                .contains("suic nr1")) {
            return false;
        }
        if(definition.allZero()) {
            return false;
        }
        boolean valid;
        switch (classIndex) {
            case 0: valid = definition.getOtherBonuses().get(14) != 0; break;
            case 1: valid = definition.getAttackBonuses().get(4) != 0; break;
            case 2: valid = definition.getAttackBonuses().get(3) != 0; break;
            default: throw new IllegalStateException("Unexpected value: " + classIndex);
        };
        return valid;
    }

    private void handleItemSelection() {
        if (loadedDefinitions.size() > viewedIndex) {
            selectedDefinition = loadedDefinitions.get(viewedIndex);
            sendStats();
        }
    }

    private final String[] BONUS_STRINGS = {"Attack bonus", "Stab: +0", "Slash: +0", "Crush: +0", "Magic: +0", "Range: +0", "Defence bonus", "Stab: +0", "Slash: +0", "Crush: +0", "Magic: +0", "Range: +0", "Other bonus", "Melee strength: +0", "Ranged strength: +0", "Magic damage: +0.0%", "Prayer: +0"};

    private void sendStats() {
        int start = STARTING_POINT + 315;
        int attackOffset = 27561;
        int defenceOffset = 27567;
        int otherOffset = 27573;
        int index;
        String[] mainNames = {"Stab: ", "Slash: ", "Crush: ", "Magic: ", "Range: "};
        String[] otherNames = {"Melee strength: ", "Ranged strength: ", "Magic damage: ", "Prayer: "};

        Map<Integer, String> stringMap = new HashMap<>();
        for(int i = 0; i < BONUS_STRINGS.length; i++) {
            player.getPacketSender().sendString(start + i, BONUS_STRINGS[i]);
        }
        for (Map.Entry<Integer, Integer> entry : selectedDefinition.getAttackBonuses()
                .entrySet()) {
            index = entry.getKey();
            stringMap.put(attackOffset + index, mainNames[index] + entry.getValue());
            //System.out.println("Sending attack at " + (attackOffset + index));
        }

        for (Map.Entry<Integer, Integer> entry : selectedDefinition.getDefenceBonuses()
                .entrySet()) {
            index = entry.getKey() - 5;
            stringMap.put(defenceOffset + index, mainNames[index] + entry.getValue());
            //System.out.println("Sending def at " + (defenceOffset + index));
        }

        for (Map.Entry<Integer, Integer> entry : selectedDefinition.getOtherBonuses()
                .entrySet()) {
            index = entry.getKey() - 14;
            stringMap.put(otherOffset + index, otherNames[index] + entry.getValue());
            //System.out.println("Sending other at " + (otherOffset + index));
        }

        stringMap.put(otherOffset + 4, "@yel@Attack Speed: " + getSpeedText());

        stringMap.forEach((id, str) -> player.getPacketSender().sendString(id, str));
    }

    private ItemDefinition.EquipmentType getEquipmentType() {
        ItemDefinition.EquipmentType type;
        switch (categoryIndex) {
            case 0: type = ItemDefinition.EquipmentType.FULL_HELMET; break;
            case 1: type = ItemDefinition.EquipmentType.PLATEBODY; break;
            case 2: type = ItemDefinition.EquipmentType.LEGS; break;
            case 3: type = ItemDefinition.EquipmentType.BOOTS; break;
            case 4: type = ItemDefinition.EquipmentType.GLOVES; break;
            case 5: type = ItemDefinition.EquipmentType.CAPE; break;
            case 6: type = ItemDefinition.EquipmentType.SHIELD; break;
            case 7: type = ItemDefinition.EquipmentType.WEAPON; break;
            case 8: type = ItemDefinition.EquipmentType.AMULET; break;
            case 9: type = ItemDefinition.EquipmentType.RING; break;
            default: throw new IllegalStateException("Unexpected value: " + categoryIndex);
        }
        return type;
    }

    public boolean handleButtonClick(int buttonId) {
        if (buttonId < 27247 || buttonId > 27584) {
            return false;
        }

        if (buttonId >= 27582) {
            int oldClassIndex = classIndex;
            classIndex = buttonId - 27582;
            if (classIndex != oldClassIndex) {
                loadItems();
            }
            return true;
        }

        if (buttonId <= 27256) {
            int oldCategoryIndex = categoryIndex;
            categoryIndex = buttonId - 27247;
            if (categoryIndex != oldCategoryIndex) {
                loadItems();
            }
            return true;
        }

        if (buttonId >= 27259 && buttonId <= 27408) {
            int oldViewedIndex = viewedIndex;
            viewedIndex = buttonId - 27259;
            if (viewedIndex != oldViewedIndex) {
                handleItemSelection();
            }
        }

        return true;
    }


}
