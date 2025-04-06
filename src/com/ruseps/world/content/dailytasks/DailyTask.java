package com.ruseps.world.content.dailytasks;

import java.util.ArrayList;
import java.util.List;

import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.world.content.dailytasks.*;

public enum DailyTask {
	//NAME(id,disc,rewards,amount to progress,difficulty, teleport pos(OPTIONAL))
	
	//**EASY**//
    DEFEAT_1K_NPCS(0,"Defeat 1,000 NPCs", new Item[] {new Item(915, 5)}, 1000, DailyTaskDifficulty.EASY), // not sure how to do this one
    DEFEAT_2K_NPCS(1,"Defeat 2,000 NPCs", new Item[] {new Item(915, 5)}, 2000, DailyTaskDifficulty.EASY),
    DEFEAT_5K_NPCS(2,"Defeat 5,000 NPCs", new Item[] {new Item(21055, 1)}, 5000, DailyTaskDifficulty.EASY), // not sure how to do this one
    COMPLETE_10_POKEMON_RAIDS(3,"Defeat 10 Pokemon Raids", new Item[] {new Item(915, 5)}, 10, DailyTaskDifficulty.EASY),
    COMPLETE_15_POKEMON_RAIDS(4,"Defeat 15 Pokemon Raids", new Item[] {new Item(915, 7)}, 15, DailyTaskDifficulty.EASY),
    COMPLETE_5_HSLAYER_TASKS(5,"Complete 5 Hard Slayer Tasks", new Item[] {new Item(14471, 5)}, 5, DailyTaskDifficulty.EASY),
    COMPLETE_10_HSLAYER_TASKS(6,"Complete 10 Hard Slayer Tasks", new Item[] {new Item(14471, 10)}, 10, DailyTaskDifficulty.EASY),//idk if you switched around the coding to make it smoother, but not sure how to do this one lmao
    COMPLETE_5_MSLAYER_TASKS(7,"Complete 5 Medium Slayer Tasks", new Item[] {new Item(14471, 5)}, 5, DailyTaskDifficulty.EASY),
    COMPLETE_10_MSLAYER_TASKS(8,"Complete 10 Medium Slayer Tasks", new Item[] {new Item(14471, 10)}, 10, DailyTaskDifficulty.EASY),
    COMPLETE_5_EASYSLAYER_TASKS(9,"Complete 5 Easy Slayer Tasks", new Item[] {new Item(14471, 5)}, 5, DailyTaskDifficulty.EASY),
    COMPLETE_10_EASYSLAYER_TASKS(10,"Complete 10 Easy Slayer Tasks", new Item[] {new Item(14471, 10)}, 10, DailyTaskDifficulty.EASY),
    COMPLETE_5_MK_RAIDS(41,"Complete 5 Mortal Kombat Raids", new Item[] {new Item(2752, 5)}, 5, DailyTaskDifficulty.EASY),
    COMPLETE_5_DD_RAIDS(42,"Complete 5 Dark Dimension Raids", new Item[] {new Item(13234, 5)}, 5, DailyTaskDifficulty.EASY),
    //ADD MORE
    //ADD MORE
    //ADD MORE
    //ADD MORE
    //ADD MORE
    //ADD MORE
    //ADD MORE
    
    //**MED**//
    COMPLETE_2_GLOBALS(40,"Complete 2 Global Bosses", new Item[] {new Item(6853, 1)}, 2, DailyTaskDifficulty.MEDIUM, new Position(1740, 5236)),
    COMPLETE_20_ANIME_RAIDS(30,"Complete 20 Anime Raids", new Item[] {new Item(2752, 5)}, 20, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),
    COMPLETE_25_ANIME_RAIDS(31,"Complete 20 Anime Raids", new Item[] {new Item(2752, 5)}, 20, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),//yeah the coding is diff from when i did it so not sure how to register them like i did before when i first did achievemetns.
    COMPLETE_25_HSLAYER_TASKS(32,"Complete 25 Hard Slayer Tasks", new Item[] {new Item(14471, 25)}, 25, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),
    COMPLETE_30_HSLAYER_TASKS(33,"Complete 30 Hard Slayer Tasks", new Item[] {new Item(14471, 30)}, 30, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),
    COMPLETE_20_ESLAYER_TASKS(34,"Complete 20 Elite Slayer Tasks", new Item[] {new Item(14471, 25)}, 20, DailyTaskDifficulty.MEDIUM, new Position(1740, 5236)),
    COMPLETE_20_SILVER_RAIDS(12,"Complete 20 Silver Raids", new Item[] {new Item(18872, 10)}, 20, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),
    COMPLETE_25_SILVER_RAIDS(13,"Complete 20 Silver Raids", new Item[] {new Item(18872, 10)}, 25, DailyTaskDifficulty.MEDIUM, new Position(2553, 3717)),
    COMPLETE_10_MK_RAIDS(43,"Complete 10 Mortal Kombat Raids", new Item[] {new Item(2752, 10)}, 10, DailyTaskDifficulty.MEDIUM),
    COMPLETE_10_DD_RAIDS(44,"Complete 10 Dark Dimension Raids", new Item[] {new Item(13234, 10)}, 10, DailyTaskDifficulty.MEDIUM),
    
    //ADD MORE
    //ADD MORE
    //ADD MORE
    
    //**hard**//
    COMPLETE_50_ESLAYER_TASKS(14,"Complete 50 Elite Slayer Tasks", new Item[] {new Item(21055, 3)}, 50, DailyTaskDifficulty.HARD, new Position(1740, 5236)),
    COMPLETE_60_ESLAYER_TASKS(15,"Complete 60 Elite Slayer Tasks", new Item[] {new Item(21055, 3)}, 60, DailyTaskDifficulty.HARD, new Position(1740, 5236)),
    COMPLETE_5_BSLAYER_TASKS(35,"Complete 5 Boss Slayer Tasks", new Item[] {new Item(21055, 3)}, 5, DailyTaskDifficulty.HARD, new Position(1740, 5236)),
    COMPLETE_5_GLOBALS(16,"Complete 5 Global Bosses", new Item[] {new Item(6853, 1)}, 5, DailyTaskDifficulty.HARD, new Position(1740, 5236)),
    COMPLETE_10_GLOBALS(17,"Complete 10 Global Bosses", new Item[] {new Item(6853, 1)}, 10, DailyTaskDifficulty.HARD, new Position(1740, 5236)),// not sure how to do this one
    COMPLETE_50_GOLD_RAIDS(18,"Complete 50 Gold Raids", new Item[] {new Item(4015, 10)}, 50, DailyTaskDifficulty.HARD, new Position(2553, 3717)),
    COMPLETE_55_GOLD_RAIDS(19,"Complete 55 Gold Raids", new Item[] {new Item(4015, 10)}, 55, DailyTaskDifficulty.HARD, new Position(2553, 3717)),
    COMPLETE_15_DD_RAIDS(39,"Complete 15 Dark Dimension Raids", new Item[] {new Item(13234, 10)}, 15, DailyTaskDifficulty.HARD),
    
    //**ELITE**//
    COMPLETE_20_BSLAYER_TASKS(36,"Complete 20 Boss Slayer Tasks", new Item[] {new Item(18689, 1)},	 20, DailyTaskDifficulty.ELITE, new Position(1740, 5236)),
    KILL_20_GLOBALS(20,"Kill 20 Global Bosses", new Item[] {new Item(10934, 1)}, 20, DailyTaskDifficulty.ELITE),
    KILL_25_GLOBALS(21,"Kill 25 Global Bosses", new Item[] {new Item(10934, 1)}, 25, DailyTaskDifficulty.ELITE),// not sure how to do this one
    KILL_50_PLAT_RAIDS(22,"Complete 50 Plat Raids", new Item[] {new Item(2747, 20)}, 50, DailyTaskDifficulty.ELITE),
    KILL_50_DIAMOND_RAIDS(23,"Complete 50 Diamond Raids", new Item[] {new Item(2747, 20)}, 50, DailyTaskDifficulty.ELITE),
    KILL_10K_NPC(24,"Defeat 10,000 NPCs", new Item[] {new Item(2747, 20)}, 10000, DailyTaskDifficulty.ELITE),
    COMPLETE_30_DD_RAIDS(37,"Complete 30 Dark Dimension Raids", new Item[] {new Item(13234, 20)}, 30, DailyTaskDifficulty.ELITE),
    COMPLETE_40_DD_RAIDS(38,"Complete 40 Dark Dimension Raids", new Item[] {new Item(13234, 20)}, 40, DailyTaskDifficulty.ELITE),
    
    //**MASTER**//
    COMPLETE_100_MK_RAIDS(25,"Complete 100 Mortal Kombat Raids", new Item[] {new Item(21056, 2)}, 100, DailyTaskDifficulty.MASTER),
    COMPLETE_150_MK_RAIDS(26,"Complete 150 Mortal Kombat Raids", new Item[] {new Item(21056, 2)}, 150, DailyTaskDifficulty.MASTER),
    COMPLETE_150_RUBY_RAIDS(27,"Complete 150 Ruby Raids", new Item[] {new Item(18689, 5)}, 150, DailyTaskDifficulty.MASTER),
    COMPLETE_150_DSTONE_RAIDS(28,"Complete 150 Dragonstone Raids", new Item[] {new Item(18689, 5)}, 150, DailyTaskDifficulty.MASTER),
    DEFEAT_25K_NPCS(29,"Defeat 25,000 NPCs", new Item[] {new Item(18689, 1)}, 25000, DailyTaskDifficulty.MASTER),
;


DailyTask(int taskIdentifier, String name, Item[] rewards, int amountNeeded, DailyTaskDifficulty difficulty) {
    this.taskIdentifier = taskIdentifier;
    this.rewards = rewards;
    this.name = name;
    this.amountNeeded = amountNeeded;
    this.difficulty = difficulty;
    this.position = null;
}

DailyTask(int taskIdentifier, String name, Item[] rewards, int amountNeeded, DailyTaskDifficulty difficulty, Position position) {
    this.taskIdentifier = taskIdentifier;
    this.rewards = rewards;
    this.name = name;
    this.amountNeeded = amountNeeded;
    this.difficulty = difficulty;
    this.position = position;
}

public static DailyTask getRandomTaskByDifficulty(DailyTaskDifficulty difficulty) {
    List<DailyTask> taskList = new ArrayList<>();
    for (DailyTask task : values()) {
        if (task.difficulty == difficulty)
            taskList.add(task);
    }
    return taskList.size() > 0 ? Misc.randomElement(taskList) : null;
}

public int getTaskIdentifier() {
	return taskIdentifier;
}

public int getAmountNeeded() {
	return amountNeeded;
}

public DailyTaskDifficulty getDifficulty() {
	return difficulty;
}

private final int taskIdentifier;
private final Item[] rewards;
private final String name;
private final int amountNeeded;
private final DailyTaskDifficulty difficulty;
private final Position position;

public Item[] getRewards() {
    return rewards;
}

public String getName() {
    return name;
}

public Position getPosition() {

    return position;
}
}
