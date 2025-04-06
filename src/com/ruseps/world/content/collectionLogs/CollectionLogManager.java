package com.ruseps.world.content.collectionLogs;

import java.util.ArrayList;
import java.util.List;

import com.ruseps.model.Item;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

import lombok.Getter;
import lombok.Setter;

import com.ruseps.world.World;

import com.ruseps.world.content.collectionLogs.CollectionLogs.*;

public class CollectionLogManager {

	private final Player player;
	private CollectionLogType type;
	private ArrayList<CollectionLogs> currentCollectionLogs;
	private ArrayList<CollectionLogs> logsViewing;
	private CollectionLogs logCurrentlyViewing;

	@Getter
	private ArrayList<LogProgress> logProgress;

	public ArrayList<LogProgress> getLogProgress() {
		return logProgress;
	}

	public CollectionLogManager(Player player) {
		this.player = player;
		type = CollectionLogType.BOSSES;
		currentCollectionLogs = new ArrayList<>();
		logsViewing = new ArrayList<>();
		logProgress = new ArrayList<>();
		addLogs();
	}

	public void openInterface() {
		for (int i = 34052; i <= 34081; i += 2) {

			player.getPacketSender().sendString(i, "");
		}
		openBossLog();
		player.getPacketSender().sendInterface(34000);
	}

	public void createNewCurrentLogs() {
		currentCollectionLogs.clear();
		for (CollectionLogs b : CollectionLogs.values()) {
			currentCollectionLogs.add(b);
		}

	}

	public void addLogProgress(LogProgress log) {
		for (LogProgress progress : logProgress) {
			if (progress.name.equalsIgnoreCase(log.getName())) {
				for (CollectionLogs b : CollectionLogs.values()) {
					if (b.name().equalsIgnoreCase(log.getName())) {
						for (Item item : log.getObtainedItems()) {
							if (b.getUniqueItems().contains(item.getId()))
								progress.getObtainedItems().add(item);
						}
					}
				}

				progress.setCompleted(log.getCompleted());
			}
		}
	}

	public void addLogs() {
		for (CollectionLogs b : CollectionLogs.values()) {
			logProgress.add(new LogProgress(b.name()));
		}
	}

	public ArrayList<Item> getObtainedItems(CollectionLogs log) {
		for (LogProgress progress : logProgress) {
			if (progress.name.equalsIgnoreCase(log.name())) {
				return progress.getObtainedItems();
			}
		}
		return new ArrayList<>();
	}

	public boolean isCompleted(CollectionLogs log) {
		for (LogProgress progress : logProgress) {
			if (progress.name.equalsIgnoreCase(log.name())) {
				if (progress.getObtainedItems().size() == log.getUniqueItems().size()) {
					return true;
				}
			}
		}
		return false;
	}

	public int totalCompleted() {
		int count = 0;
		for (LogProgress progress : logProgress) {
			if (progress.getObtainedItems().size() == CollectionLogs.valuesOf1(progress.getName()).getUniqueItems()
					.size()) {
				count++;
			}
		}
		return count;
	}

	public void openBossLog() {
		if (currentCollectionLogs.size() <= 0) {
			createNewCurrentLogs();
		}

		player.getPacketSender().sendConfig(2388, type.ordinal());

		if (logCurrentlyViewing == null) {
			logCurrentlyViewing = currentCollectionLogs.get(0);
		}
		logsViewing.clear();
		int index = 34052;
		for (CollectionLogs b : CollectionLogs.values()) {
			if (b.getType() == type) {
				player.getPacketSender().sendString(index, (isCompleted(b) ? "@gre@" : "") + b.getName());
				logsViewing.add(b);
				index += 2;
			}
		}
		int length = logsViewing.size() * 17;
		if (length <= 214)
			length = 214;
		player.getPacketSender().setScrollBar(34050, 0);

		player.getPacketSender().sendString(34006, "Total: @whi@(" + totalCompleted() + "/" + logProgress.size() + ")");
		player.getPacketSender().sendString(34007,
				logCurrentlyViewing.getName() != null ? logCurrentlyViewing.getName() : "None");

		for (CollectionLogs log : currentCollectionLogs) {
			if (log.getName().equals(logCurrentlyViewing.getName())) {
				double percent = 100 * getObtainedItems(log).size() / log.getUniqueItems().size();
				player.getPacketSender().updateProgressBar(34008, (int) percent);
				player.getPacketSender().sendString(34009,
						(type == CollectionLogType.BOXES ? "Opened: "
								: type == CollectionLogType.RAIDS ? "Completed: " : "Killed: ") + "@whi@"
								+ Misc.insertCommasToNumber(log.getKillCount(player)));
				player.getPacketSender().sendString(34020,
						"@whi@" + getObtainedItems(log).size() + "/" + log.getUniqueItems().size());

				Item reward = (log.getReward());
				player.getPacketSender().sendItemOnInterface(34022, reward.getId(), reward.getAmount());

			}
		}

		int itemsLength = 180;

		int itemsScroll = 36 + 3 + (logCurrentlyViewing.getUniqueItems().size() / 6 * 36);
		if (itemsScroll <= 175)
			itemsScroll = 175;
		player.getPacketSender().setScrollBar(34250, 0);

		for (int i = 0; i < itemsLength; i++) {
			if (logCurrentlyViewing.getUniqueItems().size() > i) {
				int amount = 69696969;
				int itemId = logCurrentlyViewing.getUniqueItems().get(i);
				int count = 0;

				for (Item item : getObtainedItems(logCurrentlyViewing)) {
					if (item.getId() == itemId) {
						count += item.getAmount();
						break;
					}
				}

				if (count > 0) {
					amount = count;
				}

				player.getPacketSender().sendItemOnInterface(34251 + i, itemId, amount);
			} else {
				player.getPacketSender().sendItemOnInterface(34251 + i, -1, 1);
			}
		}

	}

	public void finishCollectionLog(CollectionLogs collectionLogs) {
		claimReward(collectionLogs);
	}

	@Getter
	private List<String> claimedCollectionRewards = new ArrayList<>();

	public boolean claimReward(CollectionLogs collectionLogs) {
		if (!claimedCollectionRewards.contains(collectionLogs.name())) {
			if (isCompleted(collectionLogs)) {
				if (collectionLogs.isAnnounce()) {
					World.sendMessage("<shad=1>@red@" + player.getUsername() + "@blu@ has just completed the @red@"
							+ collectionLogs.getName() + " <col=f97100>Collection log! @red@(KC:"
							+ collectionLogs.getKillCount(player) + ")");
				}
				if (collectionLogs.getReward() != null) {
					Item reward = (collectionLogs.getReward());
					player.depositItemBank(reward);
					player.sendMessage("Your @red@" + collectionLogs.getName()
							+ "</col> log reward has been put in your bank. @red@x" + reward.getAmount() + " "
							+ reward.getDefinition().getName());
					claimedCollectionRewards.add(collectionLogs.name());
				}
			}
		}
		return false;
	}

	public void checkClaimedLogs() {
		for (CollectionLogs logs : CollectionLogs.values()) {
			claimReward(logs);
		}
	}

	public void incrementLog(CollectionLogs collectionLogs, LogProgress log, Item item, int amount) {
		boolean add = true;
		boolean complete = isCompleted(collectionLogs);
		log.setCompleted(log.getCompleted() + amount);

		if (!collectionLogs.getUniqueItems().contains(item.getId())) {
			return;
		}
		for (Item obtainedItems : log.getObtainedItems()) {
			if (obtainedItems.getId() == item.getId()) {
				obtainedItems.setAmount(obtainedItems.getAmount() + item.getAmount());
				add = false;
				break;
			}
		}
		if (add) {
			player.sendMessage("@red@A unique item has been added to your collection log!");
			log.getObtainedItems().add(item);
		}
		if (!complete && isCompleted(collectionLogs)) {
			finishCollectionLog(collectionLogs);
		}
	}

	public CollectionLogType getType() {
		return type;
	}

	public void setType(CollectionLogType type) {
		this.type = type;
	}

	public ArrayList<CollectionLogs> getCurrentCollectionLogs() {
		return currentCollectionLogs;
	}

	public void setCurrentCollectionLogs(ArrayList<CollectionLogs> currentCollectionLogs) {
		this.currentCollectionLogs = currentCollectionLogs;
	}

	public ArrayList<CollectionLogs> getLogsViewing() {
		return logsViewing;
	}

	public void setLogsViewing(ArrayList<CollectionLogs> logsViewing) {
		this.logsViewing = logsViewing;
	}

	public CollectionLogs getLogCurrentlyViewing() {
		return logCurrentlyViewing;
	}

	public void setLogCurrentlyViewing(CollectionLogs logCurrentlyViewing) {
		this.logCurrentlyViewing = logCurrentlyViewing;
	}

	public List<String> getClaimedCollectionRewards() {
		return claimedCollectionRewards;
	}

	public void setClaimedCollectionRewards(List<String> claimedCollectionRewards) {
		this.claimedCollectionRewards = claimedCollectionRewards;
	}

	public Player getPlayer() {
		return player;
	}

	public void setLogProgress(ArrayList<LogProgress> logProgress) {
		this.logProgress = logProgress;
	}

	public void addDrop(NPC npc, Item item) {
		CollectionLogs collectionLogs = CollectionLogs.forNpcId(npc.getId());
		if (collectionLogs != null) {
			for (LogProgress log : logProgress) {
				if (log.getName().equalsIgnoreCase(collectionLogs.name())) {
					incrementLog(collectionLogs, log, item, 1);
				}
			}
		}
	}

	public void addItem(int itemID, Item item) {
		addItem(itemID, item, 1);
	}

	public void addItem(int itemID, Item item, int amount) {
		CollectionLogs collectionLogs = CollectionLogs.forItemId(itemID);
		if (collectionLogs != null) {
			for (LogProgress log : logProgress) {
				if (log.getName().equalsIgnoreCase(collectionLogs.name())) {
					incrementLog(collectionLogs, log, item, amount);
				}
			}
		}
	}

	public void addItem(CollectionLogs collectionLogs, Item item) {
		addItem(collectionLogs, item, 1);
	}

	public void addItem(CollectionLogs collectionLogs, Item item, int amount) {
		if (collectionLogs != null) {
			for (LogProgress log : logProgress) {
				if (log.getName().equalsIgnoreCase(collectionLogs.name())) {
					incrementLog(collectionLogs, log, item, amount);
				}
			}
		}
	}

	public boolean handleButton(final int buttonId) {
		if (buttonId == 19870) {
			this.openInterface();
			return true;
		}
		if (buttonId >= -31484 && buttonId <= -31335) {
			int index = (buttonId - -31485) / 2;
			if (index < logsViewing.size()) {
				logCurrentlyViewing = logsViewing.get(index);
				openInterface();
			}
			return true;
		} else if (buttonId >= -31526 && buttonId <= -31518) {
			int index = (buttonId - (-31526)) / 2;
			if (index < CollectionLogType.values().length) {
				for (int i = -31484; i <= -31470; i += 2) {
					player.getPacketSender().sendString(i, "");
				}
				type = CollectionLogType.values()[index];
				openInterface();
				logCurrentlyViewing = logsViewing.get(0);
				player.getPacketSender().sendConfig(2451, 0);

				openInterface();
			}
			return true;
		}
		return false;
	}

	@Getter
	@Setter
	public class LogProgress {
		public void setCompleted(int completed) {
			this.completed = completed;
		}

		public void setObtainedItems(ArrayList<Item> obtainedItems) {
			this.obtainedItems = obtainedItems;
		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name1) {
			name = name1;
		}

		private int completed;

		public int getCompleted() {
			return completed;
		}

		private ArrayList<Item> obtainedItems;

		public ArrayList<Item> getObtainedItems() {
			return obtainedItems;
		}

		public LogProgress(String name) {
			this.name = name;
			this.obtainedItems = new ArrayList<>();
		}

	}

}
