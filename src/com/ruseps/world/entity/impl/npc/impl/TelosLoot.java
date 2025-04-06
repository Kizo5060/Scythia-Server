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
import com.ruseps.model.definitions.DropUtils;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.NpcDropItem;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class TelosLoot extends NPC {
	
	public static int spawnTime = 10;
	
	public static int[] COMMONLOOT = { 14793 };
	public static int[] MEDIUMLOOT = { 10822, 11208, 10824, 11206, 10826, 18448, 18950, 18920, 14793, 15501, 6183, 915 };
	public static int[] RARELOOT = { 11039, 11040, 11041, 11042, 11147, 11148, 11149, 10942, 10934, 18057 };
	public static int[] SUPERRARELOOT = { 19007, 19008, 19009, 19010, 19011, 19013 };
	public static int [][] allLoot = {RARELOOT, SUPERRARELOOT };
	/**
	 * The npc id.
	 */
	public static final int BLUE_NPC_ID = 3168;
	
	public static final int GREEN_NPC_ID = 3167;
	
	public static final int RED_NPC_ID = 3169;

	/**
	 * Constructs a new {@link Wyrm}.
	 * 
	 * @param position
	 *            The position.
	 */
	public TelosLoot(Position position) {
		super(BLUE_NPC_ID, position); 
	}
	
	@Override
	public void dropItems(Player killer) {
		handleDrop(this);
		spawnTime = 30;
	}

	/**
	 * Handles the drops for the Wyrm.
	 * 
	 * @param npc
	 *            The npc.
	 */
	
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
	
	/**
	 * Gives the loot to the player.
	 * 
	 * @param player
	 *            The player.
	 * @param npc
	 *            The npc.
	 * @param pos
	 *            The position.
	 */
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
		player.getDailyTaskManager().submitProgressToIdentifier(40, 1);
		World.sendMessage("<img=25> <col=29690e><shad=1>"+player.getUsername()+ " @bla@received a loot from the <col=29690e>Immortal Telos!");
		
		//GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(19994, Misc.inclusiveRandom(5, 10)), pos, player.getUsername(), false, 150, true, 200));

		boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);
		
		if(isWearingCollector) {
			player.addItemToAny(19992, Misc.inclusiveRandom(50, 150));
		}else
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(19992, Misc.inclusiveRandom(50, 150)), pos, player.getUsername(), false, 150, true, 200));


		
		if (chance > 99) {
			// super rare
			player.getCollectionLogManager().addDrop(npc, new Item(superrare));
			if (isWearingCollector) {
				player.addItemToAny(superrare, 1);
				return;
			}
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		} else {
			if (chance > 90) {
				// rare
				player.getCollectionLogManager().addDrop(npc, new Item(rare));
				if (isWearingCollector) {
					player.addItemToAny(rare, 1);
					return;
				}
				GroundItemManager.spawnGroundItem(player,
						new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
				return;
			} else {

				if (chance > 50) {
					if (isWearingCollector) {
						player.addItemToAny(medium, 1);
						return;
					}
					// medium
					GroundItemManager.spawnGroundItem(player,
							new GroundItem(new Item(medium), pos, player.getUsername(), false, 150, true, 200));
					return;
				} else {
					if (chance >= 0) {
						// common
						
						if (isWearingCollector) {
							player.addItemToAny(common, 1);
							return;
						}
						GroundItemManager.spawnGroundItem(player,
								new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Handles the drop.
	 * 
	 * @param npc
	 *            The npc.
	 * @param player
	 *            The player.
	 * @param damage
	 *            The damage.
	 */
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
		
		items.put(19992, new NpcDropItem(19992, new int[] { 50 }, 0));
		
		Arrays.stream(COMMONLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 3)));
		Arrays.stream(MEDIUMLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 5)));
		Arrays.stream(RARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 8)));
		Arrays.stream(SUPERRARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 9)));
		
		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));
		
		NPCDrops.getDrops().put(BLUE_NPC_ID, drops);
	}
	
}

