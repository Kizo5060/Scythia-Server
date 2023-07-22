package com.ruseps.world.content.DiscordBot;

import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Player;

public class DropAnnouncer {
    int npcId;
    Player player;
    int itemId;
    Item item;


    public DropAnnouncer(int npcId, Player player, int itemId, Item item){
        this.npcId = npcId;
        this.player = player;
        this.itemId = itemId;
        this.item = item;
    }


    public int getNpcId() {
        return npcId;
    }

    public Item getItem() {
        return item;
    }

    public int getItemId() {
        return itemId;
    }

    public Player getPlayer() {
        return player;
    }
}
