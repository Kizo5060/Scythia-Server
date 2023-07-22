package com.ruseps.world.content.customtasks.tasktypes;

import com.ruseps.model.Item;
import com.ruseps.model.Skill;
import com.ruseps.world.content.customtasks.TaskReward;

import java.util.Arrays;

import static com.ruseps.world.content.new_raids_system.raids_loot.raids_one.RaidsOneChest.raidsKey;

public enum OtherTask {
;

    private final String name;
    private final int index;
    private final String identifier;
    private final int amount;
    private final TaskReward reward;
    private final String[] description;


    OtherTask(String name, int index, String identifier, int amount, TaskReward reward, String[] description) {
        this.name = name;
        this.index = index;
        this.identifier = identifier;
        this.amount = amount;
        this.reward = reward;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getAmount() {
        return amount;
    }

    public TaskReward getReward() {
        return reward;
    }

    public String[] getDescription() {
        return description;
    }

    public static OtherTask getByIdentifier(String identifier) {
        return Arrays.stream(VALUES)
                .filter(task -> task.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    public static OtherTask[] VALUES = values();

}