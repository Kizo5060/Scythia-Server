package com.ruseps.world.content.customtasks.tasktypes;

import com.ruseps.model.Item;
import com.ruseps.model.Skill;
import com.ruseps.world.content.customtasks.TaskReward;

public enum SkillingTask {

    CUT_1000_MAGIC_LOGS("Cut 1000 Magic Logs", 0, Skill.WOODCUTTING, 1513, 1000, new TaskReward(new Item(21882, 25)), new String[]{}),
    MINE_500_RUNITE_ORES("Mine 500 Runite Ores", 1, Skill.MINING, 451, 500, new TaskReward(new Item(21882, 100)), new String[]{});

    private final String name;
    private final int index;
    private final Skill skill;
    private final int id;
    private final int amount;
    private final TaskReward reward;
    private final String[] description;

    public static SkillingTask[] VALUES = values();

    SkillingTask(String name, int index, Skill skill, int id, int amount, TaskReward reward, String[] description) {
        this.name = name;
        this.index = index;
        this.skill = skill;
        this.id = id;
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

    public Skill getSkill() {
        return skill;
    }

    public int getId() {
        return id;
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

    public static SkillingTask[] getVALUES() {
        return VALUES;
    }
}
