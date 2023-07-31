package com.ruseps.world.content.overlaytimer;

import com.ruseps.world.entity.impl.player.Player;

public class OverlayTimer {

    public static void updateOverlayTimersOnLogIn(Player player) {
        long secondsLeft = 0;
        long secondsLeft1 = 0;
        secondsLeft = (player.getCleansingTime() - System.currentTimeMillis()) / 1000;
        secondsLeft1 = (player.getPraiseTime() - System.currentTimeMillis()) / 1000;
        sendOverlayTimer(player, 1267, (int) secondsLeft);
        sendOverlayTimer(player, 1220, (int) secondsLeft1);
    }

    public static void sendOverlayTimer(Player player, int overlaySpriteId, int secondsDurationLeft) {
        if (secondsDurationLeft > 0) {
            player.getPA().sendMessage(":packet:startoverlaytimer " + overlaySpriteId + " " + secondsDurationLeft);
        }
    }

    public static void stopAllOverlayTimers(Player player) {
        player.getPA().sendMessage(":packet:stopalloverlaytimers");
    }

    public static void stopOverlayTimer(Player player, int overlaySpriteId) {
        player.getPA().sendMessage(":packet:stopoverlaytimer " + overlaySpriteId);
    }

}
