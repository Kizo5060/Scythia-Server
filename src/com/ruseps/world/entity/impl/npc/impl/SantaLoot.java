package com.ruseps.world.entity.impl.npc.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.NpcDropItem;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;


// * A class that handles the Wyrm boss.
 //* 
 //* @author Blake
// *
 
public class SantaLoot extends NPC {
	
	public static int spawnTime = 10;
	
	public static int[] COMMONLOOT = { 6183, 18338, 19994, 19992, 19990, 19888, 11896 };
	public static int[] MEDIUMLOOT = { 5090, 5091, 5092, 11658, 14044, 14046, 14045, 14047, 14048, 15501 };
	public static int[] RARELOOT = { 21055, 13016, 621, 10942, 18057, 21056, 6859, 1050, 14595, 14603 };
	public static int[] SUPERRARELOOT = {18689, 19116, 3527, 14602, 14605, 11651};
	public static int [][] allLoot = {SUPERRARELOOT,RARELOOT };

	public SantaLoot(Position position) {
		super(8540, position); 
	}
	
	@Override
	public void dropItems(Player killer) {
		handleDrop(this);
		spawnTime = 30;
	}

	public static void handleDrop(NPC npc) {
		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Long> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());
		}

		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Long>> result = sortEntries(killers);
		
		int count = 0;
		
		for (Entry<Player, Long> entry : result) {

			Player killer = entry.getKey();
			Long damage = entry.getValue();

			handleDrop(npc, killer, damage);

			if (++count >= 10) {
				break;
			}

		}

}
	
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(100);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int medium = MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)]; 
		player.getDailyTaskManager().submitProgressToIdentifier(21, 1);
		player.getDailyTaskManager().submitProgressToIdentifier(20, 1);
		player.getDailyTaskManager().submitProgressToIdentifier(17, 1);
		player.getDailyTaskManager().submitProgressToIdentifier(16, 1);
		World.sendMessage("<img=469> <col=dbffba><shad=1>"+player.getUsername()+ " received a loot from the @gre@Mighty Santa!");
		
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(19994, Misc.inclusiveRandom(5, 10)), pos, player.getUsername(), false, 150, true, 200));

		if (chance > 99) {
			//super rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance > 95) {
			//rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance > 50) {
			//medium
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(medium), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance >= 0) {
			//common
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
	}

	private static void handleDrop(NPC npc, Player player, Long damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
			
		});

		return sortedEntries;
		
	}

	public static final void loadDrops() {
		Map<Integer, NpcDropItem> items = new HashMap<>();
		
		items.put(19994, new NpcDropItem(19994, new int[] { 5 }, 0));
		
		Arrays.stream(COMMONLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 3)));
		Arrays.stream(MEDIUMLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 5)));
		Arrays.stream(RARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 8)));
		Arrays.stream(SUPERRARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 10)));
		
		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));
		
		NPCDrops.getDrops().put(8540, drops);
	}
	
}