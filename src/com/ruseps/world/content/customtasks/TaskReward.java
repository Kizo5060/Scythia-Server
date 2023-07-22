package com.ruseps.world.content.customtasks;

import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Perk;

public class TaskReward {
    Item[] items;
    Perk[] perks;

    public TaskReward(Item... items) {
        this.items = items;
        this.perks = new Perk[0];
    }

    public TaskReward(Item[] items, Perk[] perks) {
        this.items = items;
        this.perks = perks;
    }

    public TaskReward(Item item) {
        this.items = new Item[] {item};
    }

    public Item[] getItems() {
        return items;
    }

    public Perk[] getPerks() {
        return perks;
    }
}
