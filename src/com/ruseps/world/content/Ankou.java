package com.ruseps.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class Ankou extends NPC {
	
	
	public static int[] COMMONLOOT = {15273, 13883, 1726, 248, 1620, 9244, 868, 12154, 1516, 1272, 2358,  1457, 1459, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686};
	public static int[] MEDIUMLOOT = {15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 1726, 9244, 868, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686};
	public static int[] RARELOOT = {15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 18831, 1726, 9244, 868, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 6686, 1080, 1202, 11127, 1164, 6529, 15271};
	public static int[] SUPERRARELOOT = {18782, 20000, 20001, 20002, 6500, 10551, 10548, 10550, 15220, 15018, 15020, 15019, 6585, 4151, 2571, 2577, 11283, 4706, 20072, 15486, 11235, 20061, 11970, 11970, 11970, 13899, 13876, 13870, 13873, 13864, 13858, 13861, 13867, 6199, 13896, 13884, 13890, 13902, 13887, 13893, 12601, 1419, 19335, 11848, 11846, 11850, 11852, 11854, 11856, 11728, 15501};
	
	/**
	 * 
	 */
	public static final int NPC_ID = 4383;

	/**
	 * 
	 */
	public static final AnkouLocation[] LOCATIONS = {
			new AnkouLocation(3303, 3931, 0, "Revenant Town"),
			new AnkouLocation(3237, 3750, 0, "Revenant Town"),
			new AnkouLocation(3157, 3887, 0, "Revenant Town"),
			new AnkouLocation(3193, 3677, 0, "Revenant Town")
	};
	
	/**
	 * 
	 */
	private static Ankou current;
	
	/**
	 * 
	 * @param position
	 */
	public Ankou(Position position) {
		
		super(NPC_ID, position);
		
		
	}
	
	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(6000, false) { //6000
			
			@Override
			public void execute() {
				spawn();
			}
			
		});
	
	}
	
	/**
	 * 
	 */
	public static void spawn() {
		
		if(getCurrent() != null) {
			return;
		}
		
		AnkouLocation location = Misc.randomElement(LOCATIONS);
		Ankou instance = new Ankou(location.copy());
		
		//System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		//System.out.print("spawned.");
		
		World.sendMessage("<img=10> @blu@[Ankou]@red@ An Ankou has spawned somewhere in the revenants town!");
		World.sendMessage("<img=10> @blu@[Ankou]@red@ Find it and kill it for a guranteed pvp item!");

	}

	/**
	 * 
	 * @param npc
	 */
	
	public static void giveLoot(Player player, NPC npc, Position pos) {
		World.sendMessage("<col=FF0000>"+player.getUsername()+ " received a loot from the Ankou!");
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 300 + Misc.getRandom(1600)), pos, player.getUsername(), false, 150, true, 200));
		
		
	}
	
	
	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
			
		});

		return sortedEntries;
		
	}

	/**
	 * 
	 * @return
	 */
	public static Ankou getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Ankou current) {
		Ankou.current = current;
	}
	
	/**
	 * 
	 * @author Nick Hartskeerl <apachenick@hotmail.com>
	 *
	 */
	public static class AnkouLocation extends Position {
		
		/**
		 * 
		 */
		private String location;
		
		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 * @param location
		 */
		public AnkouLocation(int x, int y, int z, String location) {
			super(x, y, z);
			setLocation(location);
		}

		/**
		 * 
		 * @return
		 */
	
		
		String getLocation() {
			return location;
		}

		/**
		 * 
		 * @param location
		 */
		public void setLocation(String location) {
			this.location = location;
		}
		
	}

	
	
}
