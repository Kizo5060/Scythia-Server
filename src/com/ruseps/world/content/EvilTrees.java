package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

	/** Evil Tree's Spawning every 40 minutes **/
	/*@author Levi <www.rune-server.org/members/AuguryPS>
	 */

	/*
	 * Evil Trees
	 * Object id: 11434
	 */

public class EvilTrees {
	

	private static final int TIME = 4000000; //40 minutes? not sure lol
	public static final int MAX_CUT_AMOUNT = 1000;//Amount of logs the tree will give before
											//despawning

	private static Stopwatch timer = new Stopwatch().reset();
	public static EvilTree SPAWNED_TREE = null;
	private static LocationData LAST_LOCATION = null;

	public static class EvilTree {

		public EvilTree(GameObject treeObject, LocationData treeLocation) {
			this.treeObject = treeObject;
			this.treeLocation = treeLocation;
		}

		private GameObject treeObject;
		private LocationData treeLocation;

		public GameObject getTreeObject() {
			return treeObject;
		}

		public LocationData getTreeLocation() {
			return treeLocation;
		}
	}
/*
 * Holds the location data in an enum for where the treee's will spawn
 * 
 */
	public static enum LocationData {

		LOCATION_1(new Position(3093, 3528), "In the wilderness (Level 1)", "Wilderness"), 
		LOCATION_2(new Position(2470, 5166), "Somewhere in the Tzhaar-Dungeon", "TzHaar dungeon"),
		LOCATION_3(new Position(2928, 3453), "Close to a lot of trees", "Woodcutting"), 
		LOCATION_4(new Position(3082, 3416), "Near a bunch of Barberians", "Barberian Villiage"), 
		LOCATION_5(new Position(3043, 2767), "Near Home", "Home"), ;

		private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}

	public static LocationData getRandom() {
		LocationData tree = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return tree;
	}
/*
 * Sequences the spawning so you don't have the same location back to back
 * 
 */
	public static void sequence() {
		if (SPAWNED_TREE == null) {
			if (timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if (LAST_LOCATION != null) {
					if (locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				SPAWNED_TREE = new EvilTree(new GameObject(11434, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(SPAWNED_TREE.treeObject);
				World.sendMessage("<img=10> @blu@[Evil Tree]@bla@ The Evil Tree has sprouted " + locationData.clue + "!");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(26704, "@or2@Evil Tree: @gre@"+EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame+""));

				timer.reset();
			}
		} else {
			if (SPAWNED_TREE.treeObject.getCutAmount() >= MAX_CUT_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}
/*
 * Handles the despawning of the tree
 * and resets the timer 
 */
	public static void despawn(boolean respawn) {
		if (respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if (SPAWNED_TREE != null) {
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;
				}
				if (p.getInteractingObject() != null && p.getInteractingObject().getId() == SPAWNED_TREE.treeObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("@blu@[EVIL TREES]@bla@The Evil Tree has been chopped down");
					p.getPacketSender().sendString(26704, "@or2@Evil Tree: @gre@"+EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame+"");

				}
			}
			CustomObjects.deleteGlobalObject(SPAWNED_TREE.treeObject);
			SPAWNED_TREE = null;
		}
	}
}

