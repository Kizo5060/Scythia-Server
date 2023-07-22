package com.ruseps.net.packet;


import com.ruseps.world.entity.impl.player.Player;

public class WearCosmeticItemPacketListener implements PacketListener
{
    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readUnsignedShortA();

        player.getCosmeticGear().equip(id);
    }
}
