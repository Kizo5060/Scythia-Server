package com.ruseps.world.content.groupironman.impl;

import com.ruseps.model.input.Input;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class InvitePlayerInputListener extends Input {

    @Override
    public void handleSyntax(Player player, String username) {
        Player target = World.getPlayerByName(username);
        if (target == null) {
            player.sendMessage(username + " is offline");
            return;
        }

        player.getGroupIronmanGroup().invite(target);
    }
}
