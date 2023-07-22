package com.ruseps.world.content.battle_royale.rotators.melee;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.player.Player;
 
public class ChestRotator {
 
    private static Stopwatch timer = new Stopwatch().reset();
    public static SpawnedPool SPAWNED_POOL = null;
    private static LocationData LAST_LOCATION = null;
 
    public static class SpawnedPool {
 
        public SpawnedPool(GameObject poolObject, LocationData poolLocation) {
            this.poolObject = poolObject;
            this.poolLocation = poolLocation;
        }
 
        public GameObject poolObject; 
        private LocationData poolLocation;
 
        public GameObject getPoolObject() {
            return poolObject;
        }
 
        public LocationData getPoolLocation() {
            return poolLocation;
        }
    }
 
    public static enum LocationData {

       CHEST_LOCATION_1(new Position(3322, 9850, 0), ""),
       CHEST_LOCATION_2(new Position(3322, 9831, 0), ""),
       CHEST_LOCATION_3(new Position(3303, 9831, 0), ""),
       CHEST_LOCATION_4(new Position(3306, 9850, 0), ""),
       CHEST_LOCATION_5(new Position(3318, 9846, 0), ""),
       CHEST_LOCATION_6(new Position(3318, 9846, 0), ""),
       CHEST_LOCATION_7(new Position(3311, 9849, 0), ""),
       CHEST_LOCATION_8(new Position(3319, 9847, 0), ""),
       CHEST_LOCATION_9(new Position(3317, 9835, 0), "");
       
        private LocationData(Position spawnPos, String playerPanelFrame) {
            this.spawnPos = spawnPos;
            this.playerPanelFrame = playerPanelFrame;
        }
 
        private Position spawnPos;
        public String playerPanelFrame;
    }
    
    public static LocationData getLocation() {
        return LAST_LOCATION;
    }
 
    public static LocationData getRandom() {
        LocationData pool = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
        return pool;
    }
   
    public static void sequence() {
		if (SPAWNED_POOL == null) {
			LocationData locationData = getRandom();
			if (LAST_LOCATION != null) {
				if (locationData == LAST_LOCATION) {
					locationData = getRandom();
				}
			}
			LAST_LOCATION = locationData;
			SPAWNED_POOL = new SpawnedPool(new GameObject(11098, locationData.spawnPos), locationData);
			CustomObjects.spawnGlobalObject(SPAWNED_POOL.poolObject);
		}
	}
    
    public static void openChest(Player player) {
         player.performAnimation(new Animation(875));
         player.performGraphic(new Graphic(1177));
    }
 
       
    public static void despawn(boolean respawn) {
		if (respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if (SPAWNED_POOL != null) {
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;

				}
				SPAWNED_POOL = null;
			}
		}
	}
}