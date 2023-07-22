package com.ruseps.world.content.new_raids_system;

import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.entity.impl.player.Player;

public class RaidsPartyConnector {
	
	private Player player;
    private RaidsParty raidsPartyConnector;
    
    public RaidsPartyConnector(Player player) {
        this.player = player;
        this.raidsPartyConnector = new RaidsParty(player);
        System.err.println("setting raids party connector..");
    }
    public RaidsParty getRaidsPartyConnector() {
        return raidsPartyConnector;
    }
} 