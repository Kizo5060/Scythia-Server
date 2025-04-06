package com.ruseps.world.content;

import com.ruseps.model.GameMode;
import com.ruseps.model.PlayerRights;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("You did not recieve donator rank because you are an iron man!");
			return;
		}
		PlayerRights rights = null;
		if (player.getAmountDonated() >= 3500) {
		    rights = PlayerRights.DRAGONSTONE_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1>@mag@" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1>@mag@"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 1700) {
		    rights = PlayerRights.RUBY_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1><col=D10000>" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1><col=D10000>"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 850) {
		    rights = PlayerRights.DIAMOND_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1>@cya@" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1>@cya@"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 500) {
		    rights = PlayerRights.PLATINUM_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1><col=E5E4E2>" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1><col=E5E4E2>"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 300) {
		    rights = PlayerRights.GOLD_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1><col=FFD700>" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1><col=FFD700>"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 100) {
		    rights = PlayerRights.SILVER_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1><col=C0C0C0>" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1><col=C0C0C0>"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		} else if (player.getAmountDonated() >= 50) {
		    rights = PlayerRights.BRONZE_MEMBER;
		    if(rights != null && rights != player.getRights()) 
		    	World.sendMessage("<shad=1><col=CD7F32>" +player.getUsername() +"<shad=1>@whi@ Is Now A <shad=1><col=CD7F32>"+Misc.formatText(rights.toString().toLowerCase())+"!<shad=1>@whi@  Congratulations!");
		}
		if(rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("<shad=1>@whi@You've become a "+Misc.formatText(rights.toString().toLowerCase())+"! Congratulations!");
			
			
			
			//asas
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}

	public static void giveWarning(Player player) {
		DialogueManager.start(player, 391);
		player.setDialogueActionId(391);
	}
	

	
	public static void handleScrollClaim(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
			if(player.getScrollAmount() == 1) {
				player.getInventory().delete(10942, 1);
				player.incrementAmountDonated(10);
				player.getPacketSender().sendMessage("Your account has gained funds worth $10. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			
			if(player.getScrollAmount()== 100) {//10$ claim all
				int i = 0;
				for(;i < player.numOfScrolls; i++) {
					player.getInventory().delete(10942, 1);
					player.incrementAmountDonated(10);
					player.getPointsHandler().incrementDonationPoints(10);
				}
				player.getPacketSender().sendMessage("Your account has gained funds worth "+ player.getScrollNum()+"x$10. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 2) {
				player.getInventory().delete(10934, 1);
				player.incrementAmountDonated(20);
				player.getPacketSender().sendMessage("Your account has gained funds worth $20. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);	
			}
			
			if(player.getScrollAmount()== 101) { //20$ claim all
				int i = 0;
				for(;i < player.numOfScrolls; i++) {
					player.getInventory().delete(10934, 1);
					player.incrementAmountDonated(20);
					player.getPointsHandler().incrementDonationPoints(20);
				}
				player.getPacketSender().sendMessage("Your account has gained funds worth "+ player.getScrollNum()+"x$20. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			
			if(player.getScrollAmount() == 3) {
				player.getInventory().delete(10935, 1);
				player.incrementAmountDonated(50);
				player.getPacketSender().sendMessage("Your account has gained funds worth $50. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			
			if(player.getScrollAmount()== 102) {//50$ claim all
				int i = 0;
				for(;i < player.numOfScrolls; i++) {
					player.getInventory().delete(10935, 1);
					player.incrementAmountDonated(50);
					player.getPointsHandler().incrementDonationPoints(50);
				}
				player.getPacketSender().sendMessage("Your account has gained funds worth "+ player.getScrollNum()+"x$50. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 4) {
				player.getInventory().delete(10943, 1);
				player.incrementAmountDonated(100);
				player.getPacketSender().sendMessage("Your account has gained funds worth $100. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			
			if(player.getScrollAmount()== 103) { //100$ claim all
				int i = 0;
				for(;i < player.numOfScrolls; i++) {
					player.getInventory().delete(10943, 1);
					player.incrementAmountDonated(100);
					player.getPointsHandler().incrementDonationPoints(100);
				}
				player.getPacketSender().sendMessage("Your account has gained funds worth "+ player.getScrollNum()+"x$100. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 5) {
				player.getInventory().delete(21035, 1);
				player.incrementAmountDonated(250);
				player.getPacketSender().sendMessage("Your account has gained funds worth $250. Your total is now at $"+player.getAmountDonated()+".");
				World.sendMessage("<img=10>@blu@ " + player.getUsername() + " has just donated $250, Now they have Donated $" + player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 6) {
				player.getInventory().delete(21036, 1);
				player.incrementAmountDonated(500);
				player.getPacketSender().sendMessage("Your account has gained funds worth $500. Your total is now at $"+player.getAmountDonated()+".");
				World.sendMessage("<img=10>@blu@ " + player.getUsername() + " has just donated $500, Now they have Donated $" + player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 7) {
				player.getInventory().delete(21037, 1);
				player.incrementAmountDonated(1000);
				player.getPacketSender().sendMessage("Your account has gained funds worth $1000. Your total is now at $"+player.getAmountDonated()+".");
				World.sendMessage("<img=10>@blu@ " + player.getUsername() + " has just donated $1000, Now they have Donated $" + player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 8) {
				player.getInventory().delete(21038, 1);
				player.incrementAmountDonated(2000);
				player.getPacketSender().sendMessage("Your account has gained funds worth $2000. Your total is now at $"+player.getAmountDonated()+".");
				World.sendMessage("<img=10>@blu@ " + player.getUsername() + " has just donated $2000, Now they have Donated $" + player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 9) {
				player.getInventory().delete(621, 1);
				player.incrementAmountDonated(1);
				player.getPacketSender().sendMessage("Your account has gained funds worth $1. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
				}
			if(player.getScrollAmount()== 104) {//1$ claim all
				int i = 0;
				for(;i < player.numOfScrolls; i++) {
					player.getInventory().delete(621, 1);
					player.incrementAmountDonated(1);
					player.getPointsHandler().incrementDonationPoints(1);
				}
				player.getPacketSender().sendMessage("Your account has gained funds worth "+ player.getScrollNum()+"x$1. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
			}
			}
				
	
	public static Dialogue getTotalFunds(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public int npcId() {
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
