package com.ruseps.world.content.referral;

import com.ruseps.model.Item;

public enum RefferalOptions {
	YOUTUBERS(new String[] {"InheritedRS", "Jipy", "Wr3ckedyou", "NexArch"}, new Item[] {new Item(19990, 15),new Item(19992, 50),new Item(19994, 100), new Item(6855, 1)}, RefferalType.YOUTUBERS),
	WEBSITES(new String[] {"Google search", "TopG", "Runelocus", "Discord", "EverythingRS",""}, new Item[] {new Item(19990, 15),new Item(19992, 50),new Item(19994, 100), new Item(6855, 1)}, RefferalType.WEBSITES),
	OTHER(new String[] {"Word of mouth", "","","","","","","","",""}, new Item[] {new Item(19990, 15),new Item(19992, 50),new Item(19994, 100), new Item(6855, 1)}, RefferalType.OTHER);
	
	private String[] options;
	
	private RefferalType type;
	
	private Item[] rewards;
	
	RefferalOptions(String[] options, Item[] rewards, RefferalType type) {
		this.options = options;
		this.rewards = rewards;
		this.type = type;
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public RefferalType getType() {
		return type;
	}
	
	public Item[] getRewards() {
		return rewards;
	}

}
