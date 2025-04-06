package com.ruseps.world.content.starter_progression;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class StarterProgression {

	private final int STARTING_POINT = 42001;

	private static final Item[][] REWARDS = { 
			{ new Item(19994, 5), new Item(8788, 20), new Item(7080, 1) },
			{ new Item(19994, 10), new Item(8788, 25), new Item(20134, 1) },
			{ new Item(19994, 15), new Item(18860, 1), new Item(18864, 1) },
			{ new Item(20106, 1), new Item(20108, 1), new Item(20110, 1) },
			{ new Item(19994, 20), new Item(8788, 30), new Item(14018, 1) },
			{ new Item(19994, 50), new Item(18992, 1), new Item(18994, 1) },
			{ new Item(20521, 1), new Item(8788, 35), new Item(19994, 40) },
			{ new Item(8788, 60), new Item(15501, 3), new Item(19994, 50) },
			{ new Item(18057, 1), new Item(10934, 1), new Item(19918, 1) },
			
			// indexed rewards, so first column = first task reward
	};

	private static Map<Integer, NPCRequirement> tasks = new HashMap<>();

	public static void loadTasks() {
		tasks.put(0, new NPCRequirement(5049, 100)); // task index, npc id, npc amount
		tasks.put(1, new NPCRequirement(4990, 100));
		tasks.put(2, new NPCRequirement(4991, 150));
		tasks.put(3, new NPCRequirement(4992, 200));
		tasks.put(4, new NPCRequirement(4999, 250));
		tasks.put(5, new NPCRequirement(4994, 300));
		tasks.put(6, new NPCRequirement(4981, 350));
		tasks.put(7, new NPCRequirement(4997, 400));
		tasks.put(8, new NPCRequirement(4993, 450)); //
	}

	private final Player player;

	public StarterProgression(Player player) {
		this.player = player;
	}

	public void open() {
		int index = player.getCurrentStarterProgression();
		if (index == -1) {
			return;
		}
		player.getPacketSender().sendWalkableInterface(STARTING_POINT, true);
		updateInterface();
	}

	public void close() {
		player.getPacketSender().sendWalkableInterface(STARTING_POINT, false);
	}

	private void updateInterface() {
		int index = player.getCurrentStarterProgression();
		if (index == -1) {
			return; 
		}

		NPCRequirement requirement = tasks.get(index);
		NpcDefinition npcDefinition = NpcDefinition.forId(requirement.getId());
		if (npcDefinition != null && npcDefinition.getName() != null) {
			String name = NpcDefinition.forId(requirement.getId()).getName();
			player.getPacketSender().sendString(STARTING_POINT + 3, name);
		} else {
			System.out.println("Npcdefinition was null for npc id " + requirement.getId() + " | Task index = " + index);
			System.out.println(npcDefinition);
			System.out.println(npcDefinition.getName());
		}
		Item[] rewards = REWARDS[index];
		player.getPacketSender().sendItemArrayOnInterface(STARTING_POINT + 4, rewards);
		int kc = player.getNpcKillCount(requirement.getId());
		int required = requirement.getAmount();
		int percentage = getPercentage(kc, required);
		player.getPacketSender().updateProgressBar(STARTING_POINT + 5, percentage);
		player.getPacketSender().sendString(STARTING_POINT + 6, percentage + "% (" + kc + "/" + required + ")");
	}

	private int getPercentage(int n, int total) {
		float proportion = ((float) n) / ((float) total);
		return (int) (proportion * 100f);
	}

	public void handleKill(int npcId) {
		int index = player.getCurrentStarterProgression();
		if (index == -1) { // all finished
			return;
		}
		NPCRequirement requirement = tasks.get(index);
		if (requirement == null) {
			player.getPacketSender().sendWalkableInterface(STARTING_POINT, false); // should fix it.
			return;
		}
		if (requirement.getId() != npcId) {
			return;
		}
		if (player.getNpcKillCount(npcId) >= requirement.getAmount()) {
			player.setStarterProgressionCompleted();
			player.getInventory().addItemSet(REWARDS[index]);
			player.sendMessage("Task kill " + requirement.getAmount() + " " + NpcDefinition.forId(npcId).getName()
					+ "'s has been completed");
			if (player.getCurrentStarterProgression() == -1) {
				player.getPacketSender().sendWalkableInterface(STARTING_POINT, false);
			}
		}
		updateInterface();
	}
}