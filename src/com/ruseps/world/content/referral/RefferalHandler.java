package com.ruseps.world.content.referral;

import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Player;

public class RefferalHandler {
	
	public void openRefferal(Player player) {
		clearInterface(player);
		player.getRefferalHandler().setRefferalType(RefferalType.YOUTUBERS);
		player.getRefferalHandler().sendOptions(player);
		player.getRefferalHandler().sendRewards(player);
		player.getPA().sendInterface(57750);
	}
	
	public void sendOptions(Player player) {
		int start = 57768;
		for(RefferalOptions options : RefferalOptions.values()) {
			if(options.getType() == player.getRefferalHandler().getRefferalType()) {
				for(String option : options.getOptions()) {
					player.getPacketSender().sendString(start, option);
					start += 2;
				}
			}
		}
	}
	
	public void sendRewards(Player player) {
		for(RefferalOptions options : RefferalOptions.values()) {
			if(options.getType() == player.getRefferalHandler().getRefferalType()) {
				player.getPA().sendItemContainer2(options.getRewards(), 57766);
			}
		}
	}
	

	public void confirmOption(Player player) {
		for(RefferalOptions options : RefferalOptions.values()) {
			if(options.getType() == player.getRefferalHandler().getRefferalType()) {
				for(Item item : options.getRewards()) {
					player.getInventory().addItem(item);
				}
			}
		}

		player.referaledby = player.getRefferalHandler().getOptionSelected();
		player.getPA().sendMessage("thank you");
		
		player.getPacketSender().sendInterface(3559);
		player.getAppearance().setCanChangeAppearance(true);
	}
	
	private void clearInterface(Player player) {
		int start = 0;
		for(int i = 0; i < 10; i++) {
			player.getPacketSender().sendString(57768 + start, "");
			start += 2;
		}
	}
	
	private RefferalType refType;

	public RefferalType getRefferalType() {
		return refType;
	}
	
	public void setRefferalType(RefferalType refType) {
		this.refType = refType;
	}
	
	private String optionSelected;
	
	public String getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(String optionSelected) {
		this.optionSelected = optionSelected;
	}
}
