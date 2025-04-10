package com.ruseps.model;

import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.skill.impl.slayer.SlayerMaster;
import com.ruseps.world.content.skill.impl.slayer.SlayerTasks;
import com.ruseps.world.entity.impl.player.Player;

public enum GameMode 
{ 
	NORMAL, HARDCORE_IRONMAN, IRONMAN, GROUP_IRONMAN,VETERAN;

	public static void set(Player player, GameMode newMode, boolean death)
	{
		if (!death && !player.getClickDelay().elapsed(1000))
			return;
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		
		if (newMode != player.getGameMode() || death)
		{
			if (!player.newPlayer()) 
			{
		
				player.getSlayer().resetSlayerTask();
				player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0)
						.setSlayerMaster(SlayerMaster.VANNAKA);
		
				player.getMinigameAttributes().getRecipeForDisasterAttributes().reset();
				player.getMinigameAttributes().getNomadAttributes().reset();
				player.getKillsTracker().clear();
				player.getDropLog().clear();
				player.getPointsHandler().reset();
				PlayerPanel.refreshPanel(player);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		}
		player.setGameMode(newMode);
		player.getPacketSender().sendIronmanMode(newMode.ordinal());
		if (!death)
		{
			player.getPacketSender().sendMessage("")
					.sendMessage(
							"You've set your gamemode to " + newMode.name().toLowerCase().replaceAll("_", " ") + ".")
					.sendMessage("If you wish to change it, please talk to the town crier in Edgeville.");
		} else {
			player.getPacketSender().sendMessage("Your account progress has been reset.");
		}
		
		if(player.newPlayer()) {
			player.setPlayerLocked(true);
		} else {
			player.setPlayerLocked(false);
		}
	}
}
