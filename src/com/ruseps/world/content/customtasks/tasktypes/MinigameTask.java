package com.ruseps.world.content.customtasks.tasktypes;

import com.ruseps.model.Item;
import com.ruseps.world.content.customtasks.TaskReward;

import java.util.Arrays;

public enum MinigameTask {

	POKEMON_RAIDS("Pokemon Raids", 0, "pokemon_raids", 500, new TaskReward(new Item(915, 5)), "Complete 500 Pokemon Raids"),
	ANIME_RAIDS("Anime Raids", 1, "anime_raids", 1000, new TaskReward(new Item(915, 5)), "Complete 1000 Anime Raids"), 
	MORTAL_KOMBAT_RAIDS("Mortal Kombat Raids", 2, "kombat_raids", 1500, new TaskReward(new Item(915, 5)), "Complete 1500 Mortal Kombat Raids"),
	;

    private final String name;
    private final int index;
    private final String identifier;
    private final int amount;
    private final TaskReward reward;
    private final String[] description;

    MinigameTask(String name, int index, String identifier, int amount, TaskReward reward, String... description) {
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

    public static MinigameTask getByIdentifier(String identifier) {
        return Arrays.stream(VALUES)
                .filter(task -> task.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

    public static MinigameTask[] VALUES = values();

}
