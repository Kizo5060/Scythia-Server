package com.ruseps.world.content.groupironman.impl;

import com.ruseps.model.input.Input;
import com.ruseps.world.content.groupironman.GroupIronmanGroup;
import com.ruseps.world.entity.impl.player.Player;

public class CreateGroupInputListener extends Input {

    @Override
    public void handleSyntax(Player player, String text) {
        if (text.length() > 12) {
            player.sendMessage("@red@You can only use up to 12 characters for your group name");
            return;
        }

        player.setGroupIronmanGroup(new GroupIronmanGroup(player));
        player.getGroupIronmanGroup().setGroupName(text);
        player.getGroupIronmanGroup().addLog("Group created by " + player.getUsername());
        GroupIronmanGroup.getGroups().put(player.getUsername(), player.getGroupIronmanGroup());

    }
}
