package com.ruseps.world.entity.impl.npc.summoning;

import com.ruseps.model.Position;
import com.ruseps.world.entity.impl.player.Player;

public class TestPet extends Pet {

    public static final int ID = 174;

    public TestPet(int id, Position position) {
        super(id, position);
    }

    @Override
    void onTick() {
    }

    /**
     * Checks if the player has the effect on.
     *
     * @param player
     * @return if the effect is active.
     */
    public static boolean hasEffect(Player player) {
        if (player.getSummoning().getFamiliar() == null || player.getSummoning().getFamiliar().getSummonNpc() == null
                || !(player.getSummoning().getFamiliar().getSummonNpc() instanceof TestPet)) {
            return false;
        }

        TestPet olmlet = (TestPet) player.getSummoning().getFamiliar().getSummonNpc();

        return olmlet.isEffectActive();
    }
}
