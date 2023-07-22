package com.ruseps.world.content.new_raids_system;

import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.raids_system.Raids;
import com.ruseps.world.entity.impl.player.Player;


public class RaidsConnector {

    private Player player;

    private Raids raidsConnector;
    
    public RaidsConnector(Player player) {
        this.player = player;
        this.raidsConnector = new Raids(player);
    }

    public Raids getRaidsConnector() {
        return raidsConnector;
    }
    
    
}
