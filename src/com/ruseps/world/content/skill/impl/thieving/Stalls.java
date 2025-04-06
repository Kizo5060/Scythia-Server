package com.ruseps.world.content.skill.impl.thieving;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import com.ruseps.model.Animation;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.content.transportation.TeleportHandler;

public class Stalls {
    private static Map<Player, Integer> stealCount = new HashMap<>();
    private static Map<Player, StopWatch> stealCooldown = new HashMap<>();
    private static final int MAX_STEALS = 20;
    private static final long STEAL_COOLDOWN = 15 * 1000; // 30 seconds in milliseconds

    public static void stealFromStall(Player player, int lvlreq, int xp, int reward, String message) {
        if (!stealCount.containsKey(player)) {
            stealCount.put(player, 0);
        }

        int count = stealCount.get(player);
        StopWatch stopwatch = stealCooldown.getOrDefault(player, new StopWatch());

        if (count >= MAX_STEALS && !stopwatch.isStarted()) {
            stopwatch.start();
            player.getPacketSender().sendMessage("<shad=0>@or1@You have reached the maximum number of steals.");
        }

        if (count >= MAX_STEALS && stopwatch.getTime() < STEAL_COOLDOWN) {
            long remainingTime = (STEAL_COOLDOWN - stopwatch.getTime()) / 1000; // Remaining time in seconds
            player.getPacketSender().sendMessage("<shad=0>@or1@You must wait " + remainingTime + " <shad=0>@yel@seconds before stealing again.");
            return;
        }

        if (count >= MAX_STEALS && stopwatch.getTime() >= STEAL_COOLDOWN) {
            stopwatch.reset();
            stealCount.put(player, 0);
            count = 0; // Reset the count to 0 after the cooldown
        }

        if (count >= MAX_STEALS) {
            player.getPacketSender().sendMessage("<shad=0>@or1@You have reached the maximum number of steals.");
            return;
        }

        if (player.getInventory().getFreeSlots() < 1) {
            player.getPacketSender().sendMessage("You need some more inventory space to do this.");
            return;
        }
        if (player.getCombatBuilder().isBeingAttacked()) {
            player.getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
            return;
        }
        if (!player.getClickDelay().elapsed(2000))
            return;
        if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlreq) {
            player.getPacketSender().sendMessage("You need a Thieving level of at least " + lvlreq + " to steal from this stall.");
            return;
        }
        if (player.getLocation() == Location.DONATOR_ZONE) {
            if (RandomUtility.RANDOM.nextInt(35) == 5) {
                TeleportHandler.teleportPlayer(player, new Position(2338, 9800), player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("You were caught stealing and teleported away from the stall!");
                return;
            }
        }

        player.performAnimation(new Animation(881));
        player.getPacketSender().sendMessage(message);
        player.getPacketSender().sendInterfaceRemoval();
        player.getSkillManager().addExperience(Skill.THIEVING, xp);
        player.getClickDelay().reset();

        if (Misc.getRandom(14000) == 3) {
            player.getInventory().add(13016, 1);
            World.sendMessage("@blu@<img=10>[Skilling Pets] " + player.getUsername() + " has received a Pet Box!");
            player.getPacketSender().sendMessage("@red@Good luck on the pet!");
        } else if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.SUPPORT) {
            player.getInventory().add(19994, 1);
        } else if (player.getRights() == PlayerRights.GOLD_MEMBER || player.getRights() == PlayerRights.MODERATOR) {
            player.getInventory().add(19994, 3);
        } else if (player.getRights() == PlayerRights.PLATINUM_MEMBER || player.getRights() == PlayerRights.DIAMOND_MEMBER
        	|| player.getRights() == PlayerRights.ADMINISTRATOR) {
            player.getInventory().add(19994, 7).add(19992, 1);
        } else if (player.getRights() == PlayerRights.RUBY_MEMBER || player.getRights() == PlayerRights.DRAGONSTONE_MEMBER 
        	|| player.getRights() == PlayerRights.OWNER){
            player.getInventory().add(19994, 15).add(19992, 5);
        }

        if (player.getLocation() == Location.DONATOR_ZONE) {
            Item item = new Item(reward);
            int value = item.getDefinition().getValue();
            player.getInventory().add(19992, value);
        } else {
            player.getInventory().add(reward, 1);
        }

        stealCount.put(player, count + 1);
        stealCooldown.put(player, stopwatch);
        player.getSkillManager().stopSkilling();
       
    }
}
