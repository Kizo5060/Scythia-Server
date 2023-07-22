package com.ruseps.world.content;


import com.ruseps.model.Flag;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.world.entity.impl.player.Player;

public class CosmeticGear {

    private final Player player;

    public CosmeticGear(Player player) {
        this.player = player;
    }

    public void equip(int id) {
        if (!player.getInventory().contains(id)) {
            return;
        }
        int slot = ItemDefinition.forId(id).getEquipmentSlot();
        player.getInventory().delete(id);
        if (player.getCosmeticEquipment()[slot] > 0) {
            player.getInventory().add(player.getCosmeticEquipment()[slot], 1);
        }
        player.setCosmeticEquipment(slot, id);
        update();
    }

    public void unequip(int id) {
        int slot = ItemDefinition.forId(id).getEquipmentSlot();

        if (player.getCosmeticEquipment()[slot] == id) {
            player.getInventory().add(player.getCosmeticEquipment()[slot], 1);
            player.getCosmeticEquipment()[slot] = -1;
        }
        update();
    }

    public void update() {
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        if (player.isViewingCosmeticTab()) {
            for (int i = 0; i < player.getCosmeticEquipment().length; i++) {
                player.getPacketSender()
                        .sendItemOnInterface(1688, player.getCosmeticEquipment()[i], i, 1);
            }
        }
    }
}
