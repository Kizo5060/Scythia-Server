package com.ruseps.world.content.customtasks.tasktypes;

import com.ruseps.model.Item;
import com.ruseps.world.content.customtasks.TaskReward;

public enum NPCTask {

    KILL_100_SNOWMAN("Kill 100 Beginner Snowman", 0, 5049, 100, new TaskReward(new Item(6199, 2)), "Kill 100 Beginner Snowman", "They can be found at ::train"),
    KILL_250_SNOWMAN("Kill 250 Transformer Snowman", 1, 4990, 250, new TaskReward(new Item(6199, 5), new Item(995, 10000000)), "Kill 150 Tranformed Snowman", "They can be found in the Monsters Teleport " );
    NPCTask(String name, int index, int npcId, int npcAmount, TaskReward reward, String... description) {
        this.name = name;
        this.index = index;
        this.npcId = npcId;
        this.npcAmount = npcAmount;
        this.reward = reward;
        this.description = description;
    }

    private final String name;
    private final int index;
    private final int npcId;
    private final int npcAmount;
    private final TaskReward reward;
    private final String[] description;

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getNpcAmount() {
        return npcAmount;
    }

    public TaskReward getReward() {
        return reward;
    }

    public String[] getDescription() {
        return description;
    }

    public static final NPCTask[] VALUES = values();
}
