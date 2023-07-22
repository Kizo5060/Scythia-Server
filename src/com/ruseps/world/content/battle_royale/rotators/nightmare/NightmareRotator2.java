package com.ruseps.world.content.battle_royale.rotators.nightmare;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.player.Player;
 
public class NightmareRotator2 {
 
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

    	 CHEST_LOCATION_1(new Position(3280, 9837, 0), ""),
         CHEST_LOCATION_2(new Position(3270, 9831, 0), ""),
         CHEST_LOCATION_3(new Position(3265, 9844, 0), ""),
         CHEST_LOCATION_4(new Position(3283, 9850, 0), ""),
         CHEST_LOCATION_5(new Position(3264, 9852, 0), ""),
         CHEST_LOCATION_6(new Position(3263, 9838, 0), ""),
         CHEST_LOCATION_7(new Position(3284, 9833, 0), "");
       
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
			SPAWNED_POOL = new SpawnedPool(new GameObject(11099, locationData.spawnPos), locationData);
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