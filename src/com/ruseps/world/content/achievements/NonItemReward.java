package com.ruseps.world.content.achievements;

import com.ruseps.world.entity.impl.player.Player;

public interface NonItemReward {
    void giveReward(Player player);

    String rewardDescription();
}

