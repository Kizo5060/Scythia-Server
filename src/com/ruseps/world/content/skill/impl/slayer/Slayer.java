package com.ruseps.world.content.skill.impl.slayer;

import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Shop.ShopManager;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.GameLoader;
import com.ruseps.world.content.Emotes.*;

public class Slayer {

	private Player player;

	public Slayer(Player p) {
		this.player = p;
	}

	private SlayerTasks slayerTask = SlayerTasks.NO_TASK, lastTask = SlayerTasks.NO_TASK;
	private SlayerMaster slayerMaster = SlayerMaster.VANNAKA;
	private int amountToSlay, taskStreak;
	private String duoPartner, duoInvitation;

	public void assignTask() {
		boolean hasTask = getSlayerTask() != SlayerTasks.NO_TASK && player.getSlayer().getLastTask() != getSlayerTask();
		boolean duoSlayer = duoPartner != null;
		if (duoSlayer && !player.getSlayer().assignDuoSlayerTask())
			return;
		if (hasTask) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		int[] taskData = SlayerTasks.getNewTaskData(slayerMaster);
		int slayerTaskId = taskData[0], slayerTaskAmount = taskData[1];
		SlayerTasks taskToSet = SlayerTasks.forId(slayerTaskId);
		if (taskToSet == player.getSlayer().getLastTask() || NpcDefinition.forId(taskToSet.getNpcId())
				.getSlayerLevel() > player.getSkillManager().getMaxLevel(Skill.SLAYER)) {
			assignTask();
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		this.amountToSlay = slayerTaskAmount;
		this.slayerTask = taskToSet;
		DialogueManager.start(player, SlayerDialogues.receivedTask(player, getSlayerMaster(), getSlayerTask()));
		PlayerPanel.refreshPanel(player);
		if (duoSlayer) {
			Player duo = World.getPlayerByName(duoPartner);
			duo.getSlayer().setSlayerTask(taskToSet);
			duo.getSlayer().setAmountToSlay(slayerTaskAmount);
			duo.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(duo, SlayerDialogues.receivedTask(duo, slayerMaster, taskToSet));
			PlayerPanel.refreshPanel(duo);
		}
	}

	public void resetSlayerTask() {
		SlayerTasks task = getSlayerTask();
		if (task == SlayerTasks.NO_TASK)
			return;
		this.slayerTask = SlayerTasks.NO_TASK;
		this.amountToSlay = 0;
		this.taskStreak = 0;
		player.getPointsHandler().setSlayerPoints(player.getPointsHandler().getSlayerPoints() - 5, false);
		PlayerPanel.refreshPanel(player);
		Player duo = duoPartner == null ? null : World.getPlayerByName(duoPartner);
		if (duo != null) {
			duo.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0);
			duo.getPacketSender()
					.sendMessage("Your partner exchanged 5 Slayer points to reset your team's Slayer task.");
			PlayerPanel.refreshPanel(duo);
			player.getPacketSender().sendMessage("You've successfully reset your team's Slayer task.");
		} else {
			player.getPacketSender().sendMessage("Your Slayer task has been reset.");
		}
	}

	public void killedNpc(NPC npc) {
		if (slayerTask != SlayerTasks.NO_TASK) {
			if (slayerTask.getNpcId() == npc.getId()) {
				handleSlayerTaskDeath(true);
				if (duoPartner != null) {
					Player duo = World.getPlayerByName(duoPartner);
					if (duo != null) {
						if (checkDuoSlayer(player, false)) {
							duo.getSlayer().handleSlayerTaskDeath(
									Locations.goodDistance(player.getPosition(), duo.getPosition(), 20));
						} else {
							resetDuo(player, duo);
						}
					}
				}
			}
		}
	}

	public void handleSlayerTaskDeath(boolean giveXp) {
		int xp = slayerTask.getXP() + Misc.getRandom(slayerTask.getXP() / 5);

		if (amountToSlay > 1) {
			amountToSlay--;
		} else {
			player.getPacketSender().sendMessage("")
					.sendMessage("You've completed your Slayer task! Return to a Slayer master for another one.");
			taskStreak++;
			if (slayerTask.getTaskMaster() == SlayerMaster.VANNAKA)
				player.getDailyTaskManager().submitProgressToIdentifier(9, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(10, 1);
			player.getAchievementTracker()
					.progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_5_SLAYER_TASKS, 1);
			if (slayerTask.getTaskMaster() == SlayerMaster.DURADEL) {
				player.getDailyTaskManager().submitProgressToIdentifier(7, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(8, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_20_DURADEL_SLAYER_TASKS, 1);
			} else if (slayerTask.getTaskMaster() == SlayerMaster.KURADEL) {
				player.getDailyTaskManager().submitProgressToIdentifier(5, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(6, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(32, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(33, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_5_HARD_SLAYER_TASKS, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_30_HARD_SLAYER_TASKS, 1);
			} else if (slayerTask.getTaskMaster() == SlayerMaster.SUMONA) {
				player.getDailyTaskManager().submitProgressToIdentifier(14, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(15, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(34, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_SOME_ELITE_SLAYER_TASKS, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_100_ELITE_SLAYER_TASKS, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_350_ELITE_SLAYER_TASKS, 1);
			} else if (slayerTask.getTaskMaster() == SlayerMaster.CHAELDAR) {
				player.getDailyTaskManager().submitProgressToIdentifier(35, 1);
				player.getDailyTaskManager().submitProgressToIdentifier(36, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_200_BOSS_SLAYER_TASKS, 1);
				player.getAchievementTracker().progress(
						com.ruseps.world.content.achievements.AchievementData.COMPLETE_400_BOSS_SLAYER_TASKS, 1);

			}
			lastTask = slayerTask;
			slayerTask = SlayerTasks.NO_TASK;
			amountToSlay = 0;
			givePoints(slayerMaster);
		}

		if (giveXp) {
			player.getSkillManager().addExperience(Skill.SLAYER, doubleSlayerXP ? xp * 2 : xp);
		}

		PlayerPanel.refreshPanel(player);
	}

	@SuppressWarnings("incomplete-switch")
	public void givePoints(SlayerMaster master) {
		int pointsReceived = 4;
		switch (master) {
		case DURADEL:
			pointsReceived = 10 + Misc.random(3);
			player.getBattlePass().addExperience(125 + Misc.getRandom(100));
			break;
		case KURADEL:
			pointsReceived = 15 + Misc.random(4);
			player.getBattlePass().addExperience(250 + Misc.getRandom(125));
			break;
		case SUMONA:
			pointsReceived = 25 + Misc.random(5);
			player.getBattlePass().addExperience(400 + Misc.getRandom(300));
			break;
		case CHAELDAR:
			pointsReceived = 100 + Misc.random(5);
			player.getBattlePass().addExperience(600 + Misc.getRandom(450));//
			break;
		}
		if (Skillcape_Data.SLAYER.isWearingCape(player)) {
			pointsReceived += pointsReceived + 5;
		}

		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == (2867)) {
			pointsReceived += 5; // Add 5 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 5 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == (13028)) {
			pointsReceived += 10; // Add 10 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 10 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == (13027)) {
			pointsReceived += 10; // Add 10 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 10 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == (13024)) {
			pointsReceived += 10; // Add 10 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 10 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == (13025)) {
			pointsReceived += 10; // Add 10 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 10 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == (13026)) {
			pointsReceived += 10; // Add 10 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 10 additional Slayer points.");
		}
		if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == (19034)) {
			pointsReceived += 35; // Add 35 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 35 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == (19033)) {
			pointsReceived += 35; // Add 35 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 35 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == (19031)) {
			pointsReceived += 35; // Add 35 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 35 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == (19032)) {
			pointsReceived += 35; // Add 35 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 35 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == (19030)) {
			pointsReceived += 35; // Add 35 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 35 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == (10598)) {
			pointsReceived += 40; // Add 40 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 40 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == (13029)) {
			pointsReceived += 20; // Add 20 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 20 additional Slayer points.");
		}

		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == (2728)) {
			pointsReceived += 75; // Add 75 additional Slayer points if the item is equipped
			player.getPacketSender().sendMessage("You received 75 additional Slayer points.");

		}

		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() == (6731)) {
			pointsReceived += 50; // slayer grinder pet

		}

		int per5 = pointsReceived * 4;
		int per10 = pointsReceived * 6;

		if (GameLoader.getDay() == GameLoader.THURSDAY || GameLoader.getDay() == GameLoader.TUESDAY
				|| GameLoader.getDay() == GameLoader.SUNDAY) {
			pointsReceived *= 2;
		}
		if (player.getRights() == PlayerRights.RUBY_MEMBER) {
			pointsReceived *= 1.75;
		} else if (player.getRights() == PlayerRights.DRAGONSTONE_MEMBER) {
			pointsReceived *= 2;
		} else if (player.getRights() == PlayerRights.DIAMOND_MEMBER) {
			pointsReceived *= 1.50;
		} else if (player.getRights() == PlayerRights.PLATINUM_MEMBER) {
			pointsReceived *= 1.25;
		}

		if (player.getSlayer().getTaskStreak() == 5) {
			player.getPointsHandler().setSlayerPoints(per5, true);
			player.getPacketSender().sendMessage("You received " + per5 + " Slayer points.");
		} else if (player.getSlayer().getTaskStreak() == 10) {
			player.getPointsHandler().setSlayerPoints(per10, true);
			player.getPacketSender()
					.sendMessage("You received " + per10 + " Slayer points and your Task Streak has been reset.");
			player.getSlayer().setTaskStreak(0);
		} else if (player.getSlayer().getTaskStreak() >= 0 && player.getSlayer().getTaskStreak() < 5
				|| player.getSlayer().getTaskStreak() >= 6 && player.getSlayer().getTaskStreak() < 10) {
			player.getPointsHandler().setSlayerPoints(pointsReceived, true);
			player.getPacketSender().sendMessage("You received " + pointsReceived + " Slayer points.");
		}
		player.getPointsHandler().refreshPanel();
	}

	public boolean assignDuoSlayerTask() {
		player.getPacketSender().sendInterfaceRemoval();
		if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("You already have a Slayer task.");
			return false;
		}
		Player partner = World.getPlayerByName(duoPartner);
		if (partner == null) {
			player.getPacketSender().sendMessage("");
			player.getPacketSender().sendMessage("You can only get a new Slayer task when your duo partner is online.");
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
			resetDuo(player, null);
			return false;
		}
		if (partner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
			player.getPacketSender().sendMessage("You and your partner need to have the same Slayer master.");
			return false;
		}
		if (partner.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Your partner must close all their open interfaces.");
			return false;
		}
		return true;
	}

	public static boolean checkDuoSlayer(Player p, boolean login) {
		if (p.getSlayer().getDuoPartner() == null) {
			return false;
		}
		Player partner = World.getPlayerByName(p.getSlayer().getDuoPartner());
		if (partner == null) {
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(p.getUsername())) {
			resetDuo(p, null);
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != p.getSlayer().getSlayerMaster()) {
			resetDuo(p, partner);
			return false;
		}
		if (login) {
			p.getSlayer().setSlayerTask(partner.getSlayer().getSlayerTask());
			p.getSlayer().setAmountToSlay(partner.getSlayer().getAmountToSlay());
		}
		return true;
	}

	public static void resetDuo(Player player, Player partner) {
		if (partner != null) {
			if (partner.getSlayer().getDuoPartner() != null
					&& partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
				partner.getSlayer().setDuoPartner(null);
				partner.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
				PlayerPanel.refreshPanel(partner);
			}
		}
		player.getSlayer().setDuoPartner(null);
		player.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
		PlayerPanel.refreshPanel(player);
	}

	public void handleInvitation(boolean accept) {
		if (duoInvitation != null) {
			Player inviteOwner = World.getPlayerByName(duoInvitation);
			if (inviteOwner != null) {
				if (accept) {
					if (duoPartner != null) {
						player.getPacketSender().sendMessage("You already have a Slayer duo partner.");
						inviteOwner.getPacketSender()
								.sendMessage("" + player.getUsername() + " already has a Slayer duo partner.");
						return;
					}
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has joined your duo Slayer team.")
							.sendMessage("Seek respective Slayer master for a task.");
					inviteOwner.getSlayer().setDuoPartner(player.getUsername());
					PlayerPanel.refreshPanel(inviteOwner);
					player.getPacketSender()
							.sendMessage("You have joined " + inviteOwner.getUsername() + "'s duo Slayer team.");
					player.getSlayer().setDuoPartner(inviteOwner.getUsername());
					PlayerPanel.refreshPanel(player);
				} else {
					player.getPacketSender().sendMessage("You've declined the invitation.");
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has declined your invitation.");
				}
			} else
				player.getPacketSender().sendMessage("Failed to handle the invitation.");
		}
	}

	public void handleSlayerRingTP(int itemId) {
		if (!player.getClickDelay().elapsed(4500))
			return;
		if (player.getMovementQueue().isLockMovement())
			return;
		SlayerTasks task = getSlayerTask();
		if (task == SlayerTasks.NO_TASK)
			return;
		Position slayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition().getY(),
				task.getTaskPosition().getZ());
		if (!TeleportHandler.checkReqs(player, slayerTaskPos))
			return;
		TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook().getTeleportType());
		Item slayerRing = new Item(itemId);
		player.getInventory().delete(slayerRing);
		if (slayerRing.getId() < 13288)
			player.getInventory().add(slayerRing.getId() + 1, 1);
		else
			player.getPacketSender().sendMessage("Your Ring of Slaying crumbles to dust.");
	}

	public void handleSlayerCommand() {
		if (!player.getClickDelay().elapsed(4500))
			return;
		if (player.getMovementQueue().isLockMovement())
			return;
		SlayerTasks task = getSlayerTask();
		if (task == SlayerTasks.NO_TASK)
			return;
		Position slayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition().getY(),
				task.getTaskPosition().getZ());
		if (!TeleportHandler.checkReqs(player, slayerTaskPos))
			return;
		TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook().getTeleportType());
	}

	public int getAmountToSlay() {
		return this.amountToSlay;
	}

	public Slayer setAmountToSlay(int amountToSlay) {
		this.amountToSlay = amountToSlay;
		return this;
	}

	public int getTaskStreak() {
		return this.taskStreak;
	}

	public Slayer setTaskStreak(int taskStreak) {
		this.taskStreak = taskStreak;
		return this;
	}

	public SlayerTasks getLastTask() {
		return this.lastTask;
	}

	public void setLastTask(SlayerTasks lastTask) {
		this.lastTask = lastTask;
	}

	public boolean doubleSlayerXP = false;

	public Slayer setDuoPartner(String duoPartner) {
		this.duoPartner = duoPartner;
		return this;
	}

	public String getDuoPartner() {
		return duoPartner;
	}

	public SlayerTasks getSlayerTask() {
		return slayerTask;
	}

	public Slayer setSlayerTask(SlayerTasks slayerTask) {
		this.slayerTask = slayerTask;
		return this;
	}

	public SlayerMaster getSlayerMaster() {
		return slayerMaster;
	}

	public void setSlayerMaster(SlayerMaster master) {
		this.slayerMaster = master;
	}

	public void setDuoInvitation(String player) {
		this.duoInvitation = player;
	}

	public static boolean handleRewardsInterface(Player player, int button) {
		if (player.getInterfaceId() == 36000) {
			switch (button) {
			case -29534:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case -29522:
				if (player.getPointsHandler().getSlayerPoints() < 10) {
					player.getPacketSender().sendMessage("You do not have 10 Slayer points.");
					return true;
				}
				player.getPointsHandler().refreshPanel();
				player.getPointsHandler().setSlayerPoints(-10, true);
				player.getSkillManager().addExperience(Skill.SLAYER, 10000);
				player.getPacketSender().sendMessage("You've bought 10000 Slayer XP for 10 Slayer points.");
				break;
			case -29519:
				if (player.getPointsHandler().getSlayerPoints() < 300) {
					player.getPacketSender().sendMessage("You do not have 300 Slayer points.");
					return true;
				}
				if (player.getSlayer().doubleSlayerXP) {
					player.getPacketSender().sendMessage("You already have this buff.");
					return true;
				}
				player.getPointsHandler().setSlayerPoints(-300, true);
				player.getSlayer().doubleSlayerXP = true;
				player.getPointsHandler().refreshPanel();
				player.getPacketSender().sendMessage("You will now permanently receive double Slayer experience.");
				break;
			case -29531:
				ShopManager.getShops().get(47).open(player);
				break;
			}
			player.getPacketSender().sendString(36030,
					"Current Points:   " + player.getPointsHandler().getSlayerPoints());
			return true;
		}
		return false;
	}
}
