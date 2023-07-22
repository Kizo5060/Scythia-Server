package com.ruseps.world.content.groupironman;

import com.ruseps.model.Item;
import com.ruseps.util.NameUtils;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.groupironman.impl.InvitationDialogue;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerLoading;
import com.ruseps.world.entity.impl.player.PlayerSaving;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class GroupIronmanGroup {

    private Player owner;

    public Player getOwner() {
        return owner;
    }

    private String ownerName;


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    private List<String> members = new CopyOnWriteArrayList<>();

    public List<String> getMembers() {
        return members;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        if (owner != null) {
            ownerName = owner.getUsername();
        }
    }

    public GroupIronmanGroup(Player owner) {
        this.owner = owner;
        members.add(owner.getUsername());
        ownerName = owner.getUsername();
        save();
    }

    public GroupIronmanGroup(Player owner, List<String> members) {
        this.owner = owner;
        this.members = new CopyOnWriteArrayList<>(members);
        if (owner != null) {
            ownerName = owner.getUsername();
        }
        save();
    }

    private static final Path path = Paths.get("./data/groupironman/");

    private static final Map<String, GroupIronmanGroup> groups = new LinkedHashMap<>();

    public static Map<String, GroupIronmanGroup> getGroups() {
        return groups;
    }

    private CircularFifoQueue<String> logs = new CircularFifoQueue<>(100);

    public void setLogs(CircularFifoQueue<String> logs) {
        this.logs = logs;
    }

    private final List<String> pendingInvites = new ArrayList<>();

    public List<String> getPendingInvites() {
        return pendingInvites;
    }

    private Map<Integer, Integer> bank = new LinkedHashMap<>();

    public Map<Integer, Integer> getBank() {
        return bank;
    }

    public void setBank(Map<Integer, Integer> bank) {
        this.bank = bank;
    }

    private int points;
    private int level;
    private long experience;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;

    }

    public void addPoints(int amount) {
        points += amount;
        if (groups.containsKey(ownerName)) {
            groups.get(ownerName).setPoints(points);
        }
    }

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementLevel(int count) {
        level += count;
        if (groups.containsKey(ownerName)) {
            groups.get(ownerName).setLevel(level);
        }
    }

    private void calculateLevel() {
        level = Math.max((int) Math.floor(log5(experience / 1000D)), 0);
    }

    private double log5(double x) {
        return Math.log(x) / Math.log(5D);
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
        calculateLevel();
    }

    public void addExperience(long count) {
        experience += count;
        if (groups.containsKey(ownerName)) {
            groups.get(ownerName).setExperience(experience);
        }
    }

    public void addItem(Player player, int id, int amount) {
        if (!player.getInventory().contains(id, amount)) {
            return;
        }

        player.getInventory().delete(id, amount);
        if (bank.containsKey(id)) {
            long newAmount = (long) bank.get(id) + (long) amount;
            System.out.println("New amount = " + newAmount);
            if (newAmount > Integer.MAX_VALUE) {
                return;
            }
            bank.put(id, (int) newAmount);
        } else {
            bank.put(id, amount);
        }
        System.out.println(bank);
        System.out.println("after adding");
        System.out.println(bank);
        // bank.merge(id, amount, Integer::sum);
        player.getInventory().refreshItems();
        updateBank();
    }

    public void delete()
    {
        members.forEach(this::kick);
        groups.remove(ownerName);

        try
        {
            Files.delete(Paths.get(path + "/" + ownerName + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String memberName : members)
        {
            Player member = World.getPlayerByName(memberName);

            member.setGroupIronmanGroup(null);
            member.setGroupOwnerName("");
            member.sendMessage("@red@Your group was deleted!");
            member.getGroupIronman().updateLeaderboard();
        }

        owner.setGroupOwnerName("");
        owner.sendMessage("@red@Group deleted");
        owner.setGroupIronmanGroup(null);
        owner.getGroupIronman().updateLeaderboard();
    }

    public void removeItem(Player player, int id, int amount) {
        if (bank.containsKey(id) && bank.get(id) >= amount) {
            int amountInBank = bank.get(id);
            int invAmount = player.getInventory().getAmount(id);
            long total = (long) amountInBank + (long) invAmount;
            if (total > Integer.MAX_VALUE) {
                return;
            }
            if (bank.get(id) >= amountInBank) {
                int remainder = amountInBank - amount;
                //    System.out.println("remainder = " + remainder);
                if (remainder == 0) {
                    bank.remove(id);
                } else {
                    bank.put(id, bank.get(id) - amount);
                }
                player.getInventory().add(id, amount);
                player.getInventory().refreshItems();
            }
            updateBank();
        }
    }

    public void removeItem(Player player, int id) {
        if (bank.containsKey(id)) {
            int amountInBank = bank.get(id);
            int invAmount = player.getInventory().getAmount(id);
            long total = (long) amountInBank + (long) invAmount;
            if (total > Integer.MAX_VALUE) {
                return;
            }
            if (bank.get(id) >= amountInBank) {
                bank.remove(id);
                player.getInventory().add(id, amountInBank);
                player.getInventory().refreshItems();
            }
            updateBank();
        }
    }

    private static final int BANK_STARTING_POINT = 19307;

    public void openBank(Player player) {
        player.getPacketSender().sendInterface(BANK_STARTING_POINT);
        updateBank();
    }

    private void updateBank() {
        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }

            int index = 0;
            Item[] items = new Item[bank.size()];
            System.out.println(bank.entrySet());
            for (Map.Entry<Integer, Integer> item : bank.entrySet()) {
                items[index++] = new Item(item.getKey(), item.getValue());
            }
            if (member.getInterfaceId() == BANK_STARTING_POINT) {
                member.getPacketSender()
                        .sendInterfaceItemArray(BANK_STARTING_POINT + 6, items);
                member.getPacketSender().sendInterface(BANK_STARTING_POINT);
                member.getPacketSender().sendInterfaceSet(BANK_STARTING_POINT, 3321);
                member.getPacketSender().sendItemContainer(member.getInventory(), 3322);
            }
        }
        groups.get(ownerName).setBank(bank);
        save();

    }

    public static void loadGroups() {
        try {
            Files.walk(path).filter(Files::isRegularFile).forEach(file -> {
                try {
                    List<String> lines = Files.readAllLines(file);
                    List<String> members = new ArrayList<>();
                    Map<Integer, Integer> bank = new LinkedHashMap<>();
                    CircularFifoQueue<String> logs = new CircularFifoQueue<>();
                    int points = 0;
                    int level = 0;
                    long experience = 0;
                    String groupName = "NONE";
                    int state = -1;
                    for (String line : lines) {

                        if (line.startsWith("POINTS: ")) {
                            points = Integer.parseInt(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("LEVEL: ")) {
                            level = Integer.parseInt(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("EXPERIENCE: ")) {
                            experience = Long.parseLong(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("GROUP NAME: ")) {
                            groupName = line.split(": ")[1];
                            continue;
                        }

                        if (line.startsWith("MEMBERS {")) {
                            state = 1;
                            continue;
                        } else if (line.startsWith("BANK {")) {
                            state = 2;
                            continue;
                        } else if (line.startsWith("LOGS {")) {
                            state = 3;
                            continue;
                        }

                        if (line.endsWith("}")) {
                            state = -1;
                            continue;
                        }

                        switch (state) {
                            case 1:
                                members.add(line);
                                break;
                            case 2:
                                String[] data = line.split(", ");
                                bank.put(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
                                break;
                            case 3:
                                logs.add(line);
                                break;
                        }
                    }

                    String owner = members.get(0); // oh, test now.
                    GroupIronmanGroup group = new GroupIronmanGroup(null, members);
                    group.setBank(bank);
                    group.setLogs(logs);
                    group.setPoints(points);
                    group.setLevel(Math.max(level, 0));
                    group.setExperience(experience);
                    group.setGroupName(groupName);
                    groups.put(owner, group);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultiThreadHandler.submit(() -> {
            groups.values().forEach(GroupIronmanGroup::save);
            System.out.println("SAVED GROUPS");
        }, 900);
    }

    public void addLog(String message) {
        logs.add(message);

        owner.getGroupIronman().updateInterface();
        save();
    }

    public CircularFifoQueue<String> getLogs() {
        return logs;
    }

    public void clearInvitations() {
        pendingInvites.clear();
        groups.get(ownerName).pendingInvites.clear();
        owner.getGroupIronman().updateInterface(this);
        owner.sendMessage("Successfully cleared all pending invitations");
        save();
    }

    public void invite(Player other) {
        if (!other.isGim()) {
            owner.sendMessage(other.getUsername() + " isn't a group ironman");
            return;
        }
        if (members.size() == 4) {
            owner.sendMessage("Your group already has the maximum amount of members(4)");
            return;
        }
        if (members.contains(other.getUsername())) {
            owner.sendMessage(other.getUsername() + " is already on the group");
            return;
        }

        if (other.getGroupIronmanGroup() != null) {
            owner.sendMessage(other.getUsername() + " is already in a group");
            return;
        }

        owner.sendMessage("Invitation sent to " + other.getUsername());

        other.setGroupIronmanGroupInvitation(this);
        pendingInvites.add(other.getUsername());
        other.sendMessage(owner.getUsername() + " invited you to their group ironman group");
        other.sendMessage("Type ::accept to accept the invitation");
        DialogueManager.start(other, new InvitationDialogue(getOwner(), other));
        getOwner().getPacketSender().sendMessage("An invitation has been sent to " + other.getUsername() + ".");

        owner.getGroupIronman().updateInterface(this);

    }

    public void handleInvitation(Player other, boolean accepted) {
        other.setGroupIronmanGroupInvitation(null);
        if (accepted) {
            other.setGroupIronmanGroup(this);
            other.setGroupOwnerName(owner.getUsername());
        }

        members.add(other.getUsername());
        pendingInvites.remove(other.getUsername());

        other.sendMessage("Group invitation accepted");
        addLog(other.getUsername() + " has joined the group");
        owner.getGroupIronman().updateInterface(this);
        other.save();
        save();

    }

    public void kick(String username) {
        if (!members.contains(username)) {
            owner.sendMessage(username + " is not on your group");
            return;
        }
        members.remove(username);

        Player kickedPlayer = World.getPlayerByName(username);
        if (kickedPlayer != null) {
            kickedPlayer.setGroupOwnerName("");
            kickedPlayer.setGroupIronmanGroup(null);
            kickedPlayer.sendMessage("You have been kicked from your ironman group");
        } else {
            Player player = new Player(null);

            player.setUsername(username);
            player.setLongUsername(NameUtils.stringToLong(username));

            PlayerLoading.getResult(player, true);

            player.setGroupOwnerName("");

            PlayerSaving.save(player);
        }



        owner.getGroupIronman().updateInterface(this);

        save();
    }


    public void save() {
        MultiThreadHandler.submit(() -> {
            try {
                if (ownerName == null) {
                    return;
                }
                Path path = Paths.get(GroupIronmanGroup.path + "/" + ownerName + ".txt");
                List<String> result = new ArrayList<>();
                result.add("MEMBERS {");
                result.addAll(members);
                result.add("}");
                result.add("BANK {");
                bank.forEach((id, amount) -> {
                    result.add(id + ", " + amount);
                });
                result.add("}");
                result.add("LOGS {");
                result.addAll(logs);
                result.add("}");
                result.add("POINTS: " + points);
                result.add("LEVEL: " + level);
                result.add("EXPERIENCE: " + experience);
                result.add("GROUP NAME: " + groupName);
                Files.write(path, result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
