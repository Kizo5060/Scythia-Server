package com.ruseps.world.content.starter_progression;

public class NPCRequirement {
	private final int id;
	private final int amount;

	public NPCRequirement(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}
}
