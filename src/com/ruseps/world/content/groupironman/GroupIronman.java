package com.ruseps.world.content.groupironman;

import com.ruseps.util.Misc;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GroupIronman {

    private final Player player;

    public GroupIronman(Player player) {
        this.player = player;
    } // open client

    private final int STARTING_POINT = 29603;
    private final int LEADERBOARD_STARTING_POINT = 28185;

    public void open() {
        player.getPacketSender().sendInterface(STARTING_POINT); // actually sec
        updateInterface();
    }
    // can u open client cuz i cant see the task bar
//sorry some kid jsut got on my server again after chargin back 654$ damn that sucks, i hope u win the chargebacki already won oh nice, i hate those ppl, happened to me multiple times
    public void openLeaderboard() {
        System.out.println("Opening leaderboard");
        player.getPacketSender().sendInterface(LEADERBOARD_STARTING_POINT);
        updateLeaderboard();
    }

    public void addExperience(long experience) {
        if (hasGroup()) {
            getGroup().addExperience(experience);
        }
    }

    public void addPoints(int amount) {
        if (hasGroup()) {
            getGroup().addPoints(amount);
        }
    }

    private GroupIronmanGroup getGroup() {
        return player.getGroupIronmanGroup();
    }

    private boolean hasGroup() {
        return getGroup() != null;
    }


    void updateLeaderboard() {
        GroupIronmanGroup playerGroup = player.getGroupIronmanGroup();
        if (playerGroup != null) {
            String ownerName = (playerGroup.getOwner()
                    .getUsername()
                    .equals(player.getUsername()) ? player.getUsername() : player.getGroupOwnerName());
            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 5, playerGroup.getGroupName());

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 6, "Leader: " + ownerName);
            //player.getPacketSender()
            //.sendString(LEADERBOARD_STARTING_POINT + 7, "TODO:";

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 8, "Points: " + playerGroup.getPoints());

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 9, "Experience: " + Misc.formatNumber(playerGroup.getExperience()));

        }
        List<GroupIronmanGroup> groups = new ArrayList<>(GroupIronmanGroup.getGroups().values());
        List<GroupIronmanGroup> sortedGroups = groups.stream()
                .sorted(Comparator.comparing(GroupIronmanGroup::getLevel)
                        .thenComparing(GroupIronmanGroup::getPoints)
                        .thenComparing(GroupIronmanGroup::getExperience)
                        .reversed())
                .collect(Collectors.toList());
        int size = Math.min(sortedGroups.size(), 30);
        for (int i = 0; i < size; i++) {
            player.getPacketSender().sendString(LEADERBOARD_STARTING_POINT + 61 + i, "");
            player.getPacketSender().sendString(LEADERBOARD_STARTING_POINT + 91 + i, "");
            player.getPacketSender().sendString(LEADERBOARD_STARTING_POINT + 121 + i, "");
            player.getPacketSender().sendString(LEADERBOARD_STARTING_POINT + 151 + i, "");
            player.getPacketSender().sendString(LEADERBOARD_STARTING_POINT + 181 + i, "");
        }
        for (int i = 0; i < size; i++) {
            GroupIronmanGroup group = sortedGroups.get(i);
            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 61 + i, group.getGroupName());

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 91 + i, String.valueOf(group.getMembers()
                            .size()));

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 121 + i, String.valueOf(group.getPoints()));


            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 151 + i, String.valueOf(Misc.formatNumber(group.getExperience())));

            player.getPacketSender()
                    .sendString(LEADERBOARD_STARTING_POINT + 181 + i, String.valueOf(group.getLevel()));
        }
    }


    public boolean canTrade(Player other) {
        GroupIronmanGroup group = player.getGroupIronmanGroup();
        if (group == null) {
            return false;
        }

        return group.getMembers().contains(other.getUsername());
    }

    public void updateInterface() {


        //members
        for (int i = 0; i < 4; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 25 + i, "");
        }

        for (int i = 0; i < 50; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 182 + i, "");
        }

        for (int i = 0; i < 6; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 108 + i, "");
        }

        if (player.getGroupIronmanGroup() == null) {
            return;
        }

        List<String> members = player.getGroupIronmanGroup().getMembers();

        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }
            member.getPacketSender().sendString(STARTING_POINT + 14, player.getGroupIronmanGroup().getGroupName());
            List<String> groupMembers = member.getGroupIronmanGroup().getMembers();
            int index = 0;
            for (String groupMember : groupMembers) {
                member.getPacketSender().sendString(STARTING_POINT + 25 + index, groupMember);
                index++;
            }
        }


        //logs
        CircularFifoQueue<String> logs = player.getGroupIronmanGroup().getLogs();

        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }

            for (int i = 0; i < logs.size(); i++) {
                member.getPacketSender().sendString(STARTING_POINT + 182 + i, logs.get(i));
            }
        }

        // pending invites

        List<String> pendingInvites = player.getGroupIronmanGroup().getPendingInvites();

        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }
            int index = 0;
            for (String log : pendingInvites) {
                member.getPacketSender().sendString(STARTING_POINT + 108 + index, log);
                index++;
            }
        }


    }

    public void updateInterface(GroupIronmanGroup group) {

        //members
        for (int i = 0; i < 4; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 25 + i, "");
        }

        for (int i = 0; i < 6; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 108 + i, "");
        }

        for (int i = 0; i < 50; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 182 + i, "");
        }

        if (player.getGroupIronmanGroup() == null) {
            return;
        }

        List<String> members = group.getMembers();

        System.out.println("Members there = " + members);
        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }
            List<String> groupMembers = member.getGroupIronmanGroup().getMembers();
            int index = 0;
            for (String groupMember : groupMembers) {
                member.getPacketSender().sendString(STARTING_POINT + 25 + index, groupMember);
                index++;
            }
        }


        //logs
        CircularFifoQueue<String> logs = player.getGroupIronmanGroup().getLogs();

        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }

            int index = 0;
            for (String log : logs) {
                member.getPacketSender().sendString(STARTING_POINT + 182 + index, log);
                index++;
            }
        }

        // pending invites

        List<String> pendingInvites = player.getGroupIronmanGroup().getPendingInvites();

        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }

            int index = 0;
            for (String log : pendingInvites) {
                member.getPacketSender().sendString(STARTING_POINT + 108 + index, log);
                index++;
            }
        }
    }

}