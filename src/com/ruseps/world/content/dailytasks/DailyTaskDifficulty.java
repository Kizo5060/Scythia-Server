package com.ruseps.world.content.dailytasks;

public enum DailyTaskDifficulty {
	EASY(5),
    MEDIUM(4),
    HARD(3),
    ELITE(2),
    MASTER(1);

    DailyTaskDifficulty(int tasksPerDay) {
        this.tasksPerDay = tasksPerDay;
    }

    private final int tasksPerDay;

	public int getTasksPerDay() {
		return tasksPerDay;
	}
}
