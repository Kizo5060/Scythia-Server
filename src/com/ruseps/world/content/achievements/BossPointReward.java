package com.ruseps.world.content.achievements;

import com.ruseps.world.entity.impl.player.Player;

public class BossPointReward implements NonItemReward {

    private final int amount;

    public BossPointReward(int amount) {
        this.amount = amount;
    }

    @Override
    public void giveReward(Player player) {
        player.setBossPoints(player.getBossPoints() + amount);
    }

    @Override
    public String rewardDescription() {
        return "-" + amount + " MOB Points.";
    }
}
