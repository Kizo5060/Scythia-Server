package com.ruseps.world.content.dailytasks;

import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Player;

public class DailyTaskSlot {

    DailyTaskSlot() {
        task = null;
        progressOnTask = 0;
        completed = false;
    }

    public DailyTask getTask() {
		return task;
	}

	public void setTask(DailyTask task) {
		this.task = task;
	}

	public int getProgressOnTask() {
		return progressOnTask;
	}

	public void setProgressOnTask(int progressOnTask) {
		this.progressOnTask = progressOnTask;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isClaimedReward() {
		return claimedReward;
	}

	public void setClaimedReward(boolean claimedReward) {
		this.claimedReward = claimedReward;
	}

	private DailyTask task;
    private int progressOnTask;
    private boolean completed;
    private boolean claimedReward;


    public void assignTask(DailyTask task) {
        this.task = task;
        this.progressOnTask = 0;
        this.completed = false;
    }

    public void addProgress(int progress) {
        if (completed)
            return;
        progressOnTask += progress;
    }

    public void checkForCompletion(Player p) {
        if (completed)
            return;
        if (progressOnTask >= task.getAmountNeeded()) {
            p.sendMessage("<col=0264ea>You have completed the Daily Task: " + task.getName().replace("\\n", "") + ".");
            p.sendMessage("<col=0264ea>You can claim the rewards for this task via ::daily!");

            p.setDailyTasksCompleted(p.getDailyTasksCompleted() + 1);

           p.getBattlePass().addExperience(100 * task.ordinal());
          //  Cases.grantCasket(p, 3);

            for (Item item : task.getRewards()) {
                p.addItemToAny(item.getId(), item.getAmount());
            }
            completed = true;
            claimedReward = true;
        }
    }

}
