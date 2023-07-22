package com.ruseps.world.content.instances;

import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/*
 * @author Ajw
 * Simple instance for Kbd
 */
public class KingBlackDragon {

	public static NPC KBD;

	/*
	 * Handles creating the instance for the player
	 */
	public static void createInstance(Player player) {
		removeInstancesFull(player);
		player.getPacketSender().sendInterfaceRemoval();
		player.moveTo(new Position(2273, 4682, player.getIndex() * 4));
		instanceNpcs(player);
		player.setPlayerInstanced(true);
	}
	
	/*
	 * Handles creating the instanced npcs
	 */
	public static void instanceNpcs(Player player) {
		KBD = new NPC(50, new Position(2271, 4697, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(KBD);
	}
	
	/*
	 * Handles removing the instanced npcs fully when leaving the area
	 */
	public static void removeInstancesFull(Player player) {
		World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.KING_BLACK_DRAGON,  player.getIndex() * 4));
		player.setPlayerInstanced(false);
	}
	/*
	 * Handles removing the instances temporarily when staying in the instance like
	 * logging in/out of the game
	 */
	public static void removeInstancesTemp(Player player) {
		World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.KING_BLACK_DRAGON,  player.getIndex() * 4));
		player.setPlayerInstanced(true);
	}
	
	/*
	 * Choices between multi area or instanced area
	 */
	
	public static void getDialogue(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		DialogueManager.start(player, 200);
		player.setDialogueActionId(200);

		}
	
	/*
	 * Start multi area
	 */
	public static void startMulti(Player player) {
		TeleportHandler.teleportPlayer(player, new Position(2273, 4682, 0), player.getSpellbook().getTeleportType());

	}
	
	/*
	 * Start instanced area
	 */
	public static void startInstanced(Player player) {
		createInstance(player);
	}
	
	
}

	

