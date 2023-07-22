package com.ruseps.world.content.chestsystem;

import java.util.HashMap;
import java.util.Map;

import com.ruseps.util.RandomUtility;
import com.ruseps.world.entity.impl.player.Player;

public class ChestViewer {

	private Player player;

	public ChestViewer(Player player) {
		this.player = player;
	} 

	public void display(int boxId, String name, int[] common, int[] uncommon, int[] rare) {
		player.getPacketSender().sendInterface(48030);
		updateInterface(boxId, name, common, uncommon, rare);
	}

	private void updateInterface(int boxId, String name, int[] common, int[] uncommon, int[] rare) {
		resetItems(player, common, uncommon, rare);
		player.getPacketSender().sendString(48035, name);
		player.getPacketSender().sendItemOnInterface(48045, boxId, 0, 1);

		for (int i = 0; i < common.length; i++) {
			player.getPacketSender().sendItemOnInterface(48051, common[i], i, 1);
		}

		for (int i = 0; i < uncommon.length; i++) {
			player.getPacketSender().sendItemOnInterface(48061, uncommon[i], i, 1);
		}

		for (int i = 0; i < rare.length; i++) {
			player.getPacketSender().sendItemOnInterface(48071, rare[i], i, 1);
		}

	}
	
	public static void resetItems(Player player, int[] common, int[] uncommon, int[] rare) {
		int int1 = 48051;
		int int2 = 48061;
		int int3 = 48071;
		
		for (int i = 0; i < common.length; i++) {
			player.getPacketSender().sendItemOnInterface(int1, 0, 0);
		}

		for (int i = 0; i < uncommon.length; i++) {
			player.getPacketSender().sendItemOnInterface(int2, 0, 0);
		}

		for (int i = 0; i < rare.length; i++) {
			player.getPacketSender().sendItemOnInterface(int3, 0, 0);
		}
	}

	public void open(int boxId, int[] common, int[] uncommon, int[] rare) {
		int reward = -1;
		int chance = RandomUtility.inclusiveRandom(0, 100);

		if (chance > 90) {
			reward = common[RandomUtility.exclusiveRandom(0, common.length)];
		} else if (chance > 35) {
			reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
		} else if (chance > 0) {
			reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
		}

		player.getInventory().delete(boxId);
		player.getInventory().add(reward, 1);
	}

	public void openAll(int boxId, int[] common, int[] uncommon, int[] rare) {
		int amount = player.getInventory().getAmount(boxId);
		Map<Integer, Integer> rewards = new HashMap<>();
		for (int i = 0; i < amount; i++) {
			int reward = -1;
			int chance = RandomUtility.inclusiveRandom(0, 100);

			if (chance > 90) { // OH LOL
				reward = common[RandomUtility.exclusiveRandom(0, common.length)];
			} else if (chance > 35) {
				reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
			} else if (chance > 0) {
				reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
			}

			rewards.merge(reward, 1, Integer::sum);

		}
		player.getInventory().delete(boxId, amount);
		boolean bank = amount <= player.getInventory().getFreeSlots();
		rewards.entrySet().forEach(r -> {
			if (bank) {
				player.getInventory().add(r.getKey(), r.getValue());
			} else {
				player.getBank(0).add(r.getKey(), r.getValue());
			}
		});

	}

}
