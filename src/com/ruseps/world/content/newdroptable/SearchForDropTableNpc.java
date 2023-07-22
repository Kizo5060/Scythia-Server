package com.ruseps.world.content.newdroptable;

import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class SearchForDropTableNpc extends Input {
    public void handleSyntax(Player player, String msg) {
        DropTableInterface.getInstance().search(player, msg);
    }
}
