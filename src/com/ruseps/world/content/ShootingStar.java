package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class ShootingStar {

	private static final int TIME = 1000000;
	                              
	public static final int MAXIMUM_MINING_AMOUNT = 800;
	
	private static Stopwatch timer = new Stopwatch().reset();
	public static CrashedStar CRASHED_STAR = null;
	private static LocationData LAST_LOCATION = null;
	
	public static class CrashedStar {
		
		public CrashedStar(GameObject starObject, LocationData starLocation) {
			this.starObject = starObject;
			this.starLocation = starLocation;
		}
		
		private GameObject starObject;
		private LocationData starLocation;
		
		public GameObject getStarObject() {
			return starObject;
		}
		
		public LocationData getStarLocation() {
			return starLocation;
		}
	}

	public static enum LocationData {
		LOCATION_1(new Position(3053, 3301), "south of the Catherby Farming patches", "Farming"),
		LOCATION_3(new Position(2480, 3433), "at the Gnome Agility Course", "Gnome Course"),
		LOCATION_4(new Position(2035, 4621), "in the Mining Zone", "Mining"),
		LOCATION_5(new Position(3091, 3528), "in the Wilderness (Edgeville)", "Edgeville Wild"),
		LOCATION_6(new Position(2582, 4844), "near the Runecrafting Fire altar", "Fire Altar"),
		LOCATION_7(new Position(3478, 4834), "near the Runecrafting Water altar", "Water Altar"),
		LOCATION_8(new Position(2793, 4830), "near the Runecrafting Mind altar", "Mind Altar"),
		LOCATION_9(new Position(2846, 4829), "near the Runecrafting Air altar", "Air Altar"),
		LOCATION_10(new Position(2995, 3911), "outside the Wilderness Agility Course", "Wild. Course");

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
		LocationData star = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return star;
	}

	public static void sequence() {
		if(CRASHED_STAR == null) {
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				CRASHED_STAR = new CrashedStar(new GameObject(38660, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(CRASHED_STAR.starObject);
				World.sendMessage("<img=10> @blu@[Shooting Star]@bla@ A star has just crashed "+locationData.clue+"!");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(26703, "@or2@Crashed star: @gre@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+""));
				timer.reset();
			}
		} else {
			if(CRASHED_STAR.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if(CRASHED_STAR != null) {
			for(Player p : World.getPlayers()) {
				if(p == null) {
					continue;
				}
				p.getPacketSender().sendString(26703, "@or2@Crashed star: @gre@N/A ");
				if(p.getInteractingObject() != null && p.getInteractingObject().getId() == CRASHED_STAR.starObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The star has been fully mined.");
				}
			}
			CustomObjects.deleteGlobalObject(CRASHED_STAR.starObject);
			CRASHED_STAR = null;
		}
	}
}
