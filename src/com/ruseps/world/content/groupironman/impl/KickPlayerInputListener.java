package com.ruseps.world.content.groupironman.impl;

import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class KickPlayerInputListener extends Input {

    @Override
    public void handleSyntax(Player player, String username) {

        player.getGroupIronmanGroup().kick(username);
    }
}

