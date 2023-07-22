package com.ruseps.world.content.customtasks;

import com.ruseps.model.Skill;
import com.ruseps.util.StringUtils;
import com.ruseps.world.content.customtasks.tasktypes.MinigameTask;
import com.ruseps.world.content.customtasks.tasktypes.NPCTask;
import com.ruseps.world.content.customtasks.tasktypes.OtherTask;
import com.ruseps.world.content.customtasks.tasktypes.SkillingTask;
import com.ruseps.world.entity.impl.player.Player;

import java.util.Arrays;

public class CustomTasks {

    private final Player player;

    public CustomTasks(Player player) {
        this.player = player;
    }

    private final int INTERFACE_ID = 20285;

    private int taskIndex = 0;
    private int tabIndex = 0;

    public void open() {
        player.getPacketSender().sendInterface(INTERFACE_ID);
        updateInterface();
    }

    public void updateInterface() {
        for (int i = 0; i < 100; i++) {
            player.getPacketSender().sendString(INTERFACE_ID + 151 + i, "");
        }
        if (tabIndex == 0) {
            NPCTask[] npcTasks = NPCTask.VALUES;
            for (int i = 0; i < npcTasks.length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 151 + i, npcTasks[i].getName());
            }
        } else if (tabIndex == 1) {
            SkillingTask[] skillingTasks = SkillingTask.VALUES;
            for (int i = 0; i < skillingTasks.length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 151 + i, skillingTasks[i].getName());
            }
        } else if (tabIndex == 2) {
            MinigameTask[] minigameTasks = MinigameTask.VALUES;
            for (int i = 0; i < minigameTasks.length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 151 + i, minigameTasks[i].getName());
            }
        } else if (tabIndex == 3) {
            OtherTask[] otherTasks = OtherTask.VALUES;
            for (int i = 0; i < otherTasks.length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 151 + i, otherTasks[i].getName());
            }
        }
        taskIndex = 0;
        sendTaskData();
    }

    public boolean handleTaskClick(int id) {
        if (id < 20436 || id > 20463) {
            return false;
        }

        taskIndex = id - 20436;
        sendTaskData();
        return true;
    }

    public boolean handleTabClick(int id) {
        if (id < 20295 || id > 20298) {
            return false;
        }

        tabIndex = id - 20295;
        updateInterface();
        return true;
    }


    private void sendTaskData() {
        for (int i = 0; i < 3; i++) {
            player.getPacketSender().sendString(INTERFACE_ID + 252 + i, "");
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 19 + i, "");
        }
        if (tabIndex == 0) {
            NPCTask npcTask = NPCTask.VALUES[taskIndex];
            TaskReward reward = npcTask.getReward();
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 18, npcTask.getName());
            for (int i = 0; i < npcTask.getDescription().length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 19 + i, StringUtils.capitalizeFirst(npcTask.getDescription()[i]));
            }
            if (reward.getPerks() != null) {
                for (int i = 0; i < reward.getPerks().length; i++) {
                    player.getPacketSender()
                            .sendString(INTERFACE_ID + 252 + i, reward.getPerks()[i].getDescription());
                }
            }
            player.getPacketSender()
                    .sendItemsOnInterfaceArrayNew(INTERFACE_ID + 262, reward.getItems());

            player.getPacketSender()
                    .updateProgressBar(INTERFACE_ID + 22, getPercent(player.getNpcKillCount(npcTask.getNpcId()), npcTask
                            .getNpcAmount()));
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 23, player.getNpcKillCount(npcTask.getNpcId()) + "/" + npcTask
                            .getNpcAmount());
        } else if (tabIndex == 1) {
            SkillingTask skillingTask = SkillingTask.VALUES[taskIndex];
            TaskReward reward = skillingTask.getReward();
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 18, skillingTask.getName());
            for (int i = 0; i < skillingTask.getDescription().length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 19 + i, StringUtils.capitalizeFirst(skillingTask.getDescription()[i]));
            }
            if (reward.getPerks() != null) {
                for (int i = 0; i < reward.getPerks().length; i++) {
                    player.getPacketSender()
                            .sendString(INTERFACE_ID + 252 + i, reward.getPerks()[i].getDescription());
                }
            }
            player.getPacketSender()
                    .sendItemsOnInterfaceArrayNew(INTERFACE_ID + 262, reward.getItems());
            int currentAmount = player.getSkillData(skillingTask.getSkill()) == null ? 0 : player.getSkillData(skillingTask
                    .getSkill()).getOrDefault(skillingTask.getId(), 0);
            player.sendMessage("ID: " + skillingTask.getId());
            player.getPacketSender()
                    .updateProgressBar(INTERFACE_ID + 22, getPercent(currentAmount, skillingTask.getAmount()));
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 23, currentAmount + "/" + skillingTask.getAmount());
        } else if (tabIndex == 2) {
            MinigameTask minigameTask = MinigameTask.VALUES[taskIndex];
            TaskReward reward = minigameTask.getReward();
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 18, minigameTask.getName());
            for (int i = 0; i < minigameTask.getDescription().length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 19 + i, StringUtils.capitalizeFirst(minigameTask.getDescription()[i]));
            }
            if (reward.getPerks() != null) {
                for (int i = 0; i < reward.getPerks().length; i++) {
                    player.getPacketSender()
                            .sendString(INTERFACE_ID + 252 + i, reward.getPerks()[i].getDescription());
                }
            }
            player.getPacketSender()
                    .sendItemsOnInterfaceArrayNew(INTERFACE_ID + 262, reward.getItems());
            String key = minigameTask.getIdentifier();
            int amount = minigameTask.getAmount() > 1 ? player.getMiscAmount(key) : (player.getMiscState(key) ? 1 : 0);
            player.getPacketSender()
                    .updateProgressBar(INTERFACE_ID + 22, getPercent(amount, minigameTask.getAmount()));
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 23, amount + "/" + minigameTask.getAmount());
        } else if (tabIndex == 3) {
            OtherTask otherTask = OtherTask.VALUES[taskIndex];
            TaskReward reward = otherTask.getReward();
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 18, otherTask.getName());
            for (int i = 0; i < otherTask.getDescription().length; i++) {
                player.getPacketSender()
                        .sendString(INTERFACE_ID + 19 + i, StringUtils.capitalizeFirst(otherTask.getDescription()[i]));
            }
            if (reward.getPerks() != null) {
                for (int i = 0; i < reward.getPerks().length; i++) {
                    player.getPacketSender()
                            .sendString(INTERFACE_ID + 252 + i, reward.getPerks()[i].getDescription());
                }
            }
            player.getPacketSender()
                    .sendItemsOnInterfaceArrayNew(INTERFACE_ID + 262, reward.getItems());
            String key = otherTask.getIdentifier();
            int amount = otherTask.getAmount() > 1 ? player.getMiscAmount(key) : (player.getMiscState(key) ? 1 : 0);
            player.getPacketSender()
                    .updateProgressBar(INTERFACE_ID + 22, getPercent(amount, otherTask.getAmount()));
            player.getPacketSender()
                    .sendString(INTERFACE_ID + 23, amount + "/" + otherTask.getAmount());
        }
    }

    private int getPercent(int currentNumber, int maxNumber) {
        return Math.min((currentNumber * 100) / maxNumber, 100);
    }

    public void handleNpcKill(int npcId) {
        // player.sendMessage("handled for " + npcId);
        NPCTask[] candidates = Arrays.stream(NPCTask.VALUES)
                .filter(task -> !player.getNpcTaskCompletions()[task.getIndex()])
                .filter(task -> task.getNpcId() == npcId)
                .filter(task -> {
                    int index = task.getIndex();
                    boolean valid = index == 0 || player.getNpcTaskCompletions()[index - 1];
                    if(!valid) {
                        player.getNpcKillCount().put(task.getNpcId(), 0);
                    }
                    return valid;
                })
                .toArray(NPCTask[]::new);

        for (NPCTask task : candidates) {
            boolean completed = player.getNpcKillCount(task.getNpcId()) >= task.getNpcAmount();
            if (completed) {
                int index = task.getIndex();
                player.setNpcTaskCompleted(index);
                player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                        .toLowerCase()) + " has been completed");
                TaskReward reward = task.getReward();
                player.getInventory().addItemSet(reward.getItems());
                Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
                player.recomputeAttributes();
            }
        }
    }

    /**
     * Those 2 depend on what tasks ur adding(eg if u have any woodcutting tasks, then this must be called once in the woodcutting class)
     */

    public void handleSkillingAction(Skill skill, int id) {
        SkillingTask[] candidates = Arrays.stream(SkillingTask.VALUES)
                .filter(task -> !player.getSkillingTaskCompletions()[task.getIndex()])
                .filter(task -> task.getId() == id)
                .toArray(SkillingTask[]::new);

        System.out.println(Arrays.toString(candidates));
        System.out.println("id = " + id);

        for (SkillingTask task : candidates) {
            boolean completed = player.getSkillData(skill)
                    .getOrDefault(task.getId(), 0) >= task.getAmount();
            if (completed) {
                int index = task.getIndex();
                player.setSkillingTaskCompleted(index);
                player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                        .toLowerCase()) + " has been completed");
                TaskReward reward = task.getReward();
                player.getInventory().addItemSet(reward.getItems());
                Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
                player.recomputeAttributes();
            }
        }
    }

    public void handleMinigameAction(String key, int amount) {
        player.addMiscAmount(key, amount);

        MinigameTask[] candidates = Arrays.stream(MinigameTask.VALUES)
                .filter(task -> !player.getMinigameTaskCompletions()[task.getIndex()])
                .filter(task -> task.getIdentifier().equalsIgnoreCase(key))
                .toArray(MinigameTask[]::new);
        for (MinigameTask task : candidates) {
            boolean completed = player.getMiscAmount(task.getIdentifier()) >= task.getAmount();

            if (completed) {
                int index = task.getIndex();
                player.setMinigameTaskCompleted(index);
                player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                        .toLowerCase()) + " has been completed");
                TaskReward reward = task.getReward();
                player.getInventory().addItemSet(reward.getItems());
                Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
                player.recomputeAttributes();
            }
        }

    }

    public void handleOtherTask(String key, int amount) {
        player.addMiscAmount(key, amount);

        OtherTask[] candidates = Arrays.stream(OtherTask.VALUES)
                .filter(task -> !player.getOtherTaskCompletions()[task.getIndex()])
                .filter(task -> task.getIdentifier().equalsIgnoreCase(key))
                .toArray(OtherTask[]::new);
        for (OtherTask task : candidates) {
            boolean completed = player.getMiscAmount(task.getIdentifier()) >= task.getAmount();

            if (completed) {
                int index = task.getIndex();
                player.setOtherTaskCompleted(index);
                player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                        .toLowerCase()) + " has been completed");
                TaskReward reward = task.getReward();
                player.getInventory().addItemSet(reward.getItems());
                Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
                player.recomputeAttributes();
            }
        }
    }

    public void completeOtherTask(String key) {
        if (player.getMiscState(key)) {
            return;
        }
        player.setMiscState(key, true);
        OtherTask task = OtherTask.getByIdentifier(key);
        if (task != null) {
            int index = task.getIndex();
            player.setOtherTaskCompleted(index);
            player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                    .toLowerCase()) + " has been completed");
            TaskReward reward = task.getReward();
            player.getInventory().addItemSet(reward.getItems());
            Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
            player.recomputeAttributes();
        }
    }


    public void handleOtherTask(String key) {
        handleOtherTask(key, 1);
    }

    public void handleMinigameAction(String key) {
        handleMinigameAction(key, 1);
    }

    public void completeMinigame(String key) {
        if (player.getMiscState(key)) {
            return;
        }
        player.setMiscState(key, true);
        MinigameTask task = MinigameTask.getByIdentifier(key);
        if (task != null) {
            int index = task.getIndex();
            player.setMinigameTaskCompleted(index);
            player.sendMessage("Task " + StringUtils.usToSpace(task.toString()
                    .toLowerCase()) + " has been completed");
            TaskReward reward = task.getReward();
            player.getInventory().addItemSet(reward.getItems());
            Arrays.stream(reward.getPerks()).forEach(player.getPerks()::add);
            player.recomputeAttributes();
        }
    }
}
