package com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_two;


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
import com.ruseps.world.content.new_raids_system.raids_system.RaidsConstants;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A class that handles the Wyrm boss.
 * 
 * @author Blake
 *
 */
public class R1P2NPC1 extends NPC {
	
	public static int spawnTime = 10;
	
	/**
	 * The npc id.
	 */
	public static final int FIRST_NPC_ID = RaidsConstants.R1P2_THIRD_NPC_ID;
	
	public R1P2NPC1(Position position) {
		super(FIRST_NPC_ID, position); 
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
		
		
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(19994, Misc.inclusiveRandom(10, 15)), pos, player.getUsername(), false, 150, true, 200));

		if (chance > 99) {
			player.getPointsHandler().incrementRaidsOnePoints(15 + (Misc.getRandom(5)));
			player.getPacketSender().sendMessage("<img=10>@blu@"+player.getUsername()+" you have received Raids One Points!");
			player.getPacketSender().sendMessage("<img=10>@blu@You now have " + player.getPointsHandler().getRaidsOnePoints() + " points.");
			return;
		}
		
		if (chance > 95) {
			player.getPointsHandler().incrementRaidsOnePoints(10 + (Misc.getRandom(5)));
			player.getPacketSender().sendMessage("<img=10>@blu@"+player.getUsername()+" you have received Raids One Points!");
			player.getPacketSender().sendMessage("<img=10>@blu@You now have " + player.getPointsHandler().getRaidsOnePoints() + " points.");
			return;
		}
		
		if (chance > 50) {
			player.getPointsHandler().incrementRaidsOnePoints(5 + (Misc.getRandom(5)));
			player.getPacketSender().sendMessage("<img=10>@blu@"+player.getUsername()+" you have received Raids One Points!");
			player.getPacketSender().sendMessage("<img=10>@blu@You now have " + player.getPointsHandler().getRaidsOnePoints() + " points.");
			return;
		}
		
		if (chance >= 0) {
			player.getPointsHandler().incrementRaidsOnePoints(5);
			player.getPacketSender().sendMessage("<img=10>@blu@"+player.getUsername()+" you have received 5 Raids One Points!");
			return;
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
		
		items.put(19992, new NpcDropItem(19992, new int[] { 20 }, 0));
		
		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));
		
		NPCDrops.getDrops().put(FIRST_NPC_ID, drops);
	}
	
}

