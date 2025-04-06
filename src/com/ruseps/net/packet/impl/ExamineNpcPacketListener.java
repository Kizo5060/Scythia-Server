package com.ruseps.net.packet.impl;

import com.ruseps.model.PlayerRights;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.content.dropchecker.NPCDropTableChecker;
import com.ruseps.world.content.newdroptable.DropTableInterface;
import com.ruseps.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int npc = packet.readShort();
        if (npc <= 0) {
            return;
        }
        //NPCDropTableChecker.getSingleton().showNPCDropTable(player, npc);
        NpcDefinition def = NpcDefinition.forId(npc);
        if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.DEVELOPER) {
        	player.sendMessage("NpcID: " + def.getId());
        	player.def = null;
        }
        if (def != null && def.getCombatLevel() > 0) {
        	player.def = def;
            DropTableInterface.getInstance().select(player, def);
            player.getMovementQueue().reset();
        }

    }

}
