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
public class Wildywyrm extends NPC {
	
	
	public static int[] COMMONLOOT = {15273, 13883, 1726, 248, 1620, 9244, 868, 12154, 1516, 1272, 2358,  1457, 1459, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686};
	public static int[] MEDIUMLOOT = {15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 1726, 9244, 868, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686};
	public static int[] RARELOOT = {15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 18831, 1726, 9244, 868, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 6686, 1080, 1202, 11127, 1164, 6529, 15271};
	public static int[] SUPERRARELOOT = {18782, 20000, 20001, 20002, 6500, 10551, 10548, 10550, 15220, 15018, 15020, 15019, 6585, 4151, 2571, 2577, 11283, 4706, 20072, 15486, 11235, 20061, 11970, 11970, 11970, 13899, 13876, 13870, 13873, 13864, 13858, 13861, 13867, 915, 13896, 13884, 13890, 13902, 13887, 13893, 12601, 1419, 19335, 11848, 11846, 11850, 11852, 11854, 11856, 11728, 15501};
	
	/**
	 * 
	 */
	public static final int NPC_ID = 3334;

	/**
	 * 
	 */
	public static final WildywyrmLocation[] LOCATIONS = {
			new WildywyrmLocation(2652, 2654, 0, "Pest Control"),
			new WildywyrmLocation(2448, 5169, 0, "Tzhaar Dungeon"),
			new WildywyrmLocation(3560, 3098, 0, "Spawned Events"),
			new WildywyrmLocation(3566, 3319, 0, "Barrows")
	};
	
	/**
	 * 
	 */
	private static Wildywyrm current;
	
	/**
	 * 
	 * @param position
	 */
	public Wildywyrm(Position position) {
		
		super(NPC_ID, position);
		
		//setConstitution(96500/3); //7,650
		//setDefaultConstitution(96500);
		
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
		
		WildywyrmLocation location = Misc.randomElement(LOCATIONS);
		Wildywyrm instance = new Wildywyrm(location.copy());
		
		//System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		//System.out.print("spawned.");
		
		World.sendMessage("<img=10> @blu@[WildyWyrm]@red@ A WildyWyrm has spawned at "+location.getLocation()+"!");
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@"+location.getLocation()+""));

	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

		setCurrent(null);

		if(npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Long> killers = new HashMap<>();

		for(Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if(entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();
			
			if(timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();
			
			if(player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());
			
		}

		npc.getCombatBuilder().getDamageMap().clear();
		
		List<Entry<Player, Long>>result = sortEntries(killers);
		int count = 0;
		
		for(Entry<Player, Long> entry : result) {
			
			Player killer = entry.getKey();
			Long damage = entry.getValue();
			
			handleDrop(npc, killer, damage);
			
			if(++count >= 5) {
				break;
			}
			
		}

	}
	
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(10);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		World.sendMessage("<col=FF0000>"+player.getUsername()+ " received a loot from the Wildywyrm!");

		
		//player.getPacketSender().sendMessage("chance: "+chance);
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 7500000 + Misc.getRandom(15000000)), pos, player.getUsername(), false, 150, true, 200));

		if(chance > 9){
			//super rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 300 + Misc.getRandom(200)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 200 + Misc.getRandom(200)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		if(chance > 7) {
			//rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 300 + Misc.getRandom(1600)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 250 + Misc.getRandom(100)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 200 + Misc.getRandom(200)), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
		if(chance > 4) {
			//medium
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 300 + Misc.getRandom(130)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 220 + Misc.getRandom(130)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 240 + Misc.getRandom(130)), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
		if(chance >= 0){
			//common
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 200 + Misc.getRandom(220)), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 200 + Misc.getRandom(230)), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
	

		
	}
	
	
	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, Long damage) {
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
	public static Wildywyrm getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Wildywyrm current) {
		Wildywyrm.current = current;
	}
	
	/**
	 * 
	 * @author Nick Hartskeerl <apachenick@hotmail.com>
	 *
	 */
	public static class WildywyrmLocation extends Position {
		
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
		public WildywyrmLocation(int x, int y, int z, String location) {
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
