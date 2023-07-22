package com.ruseps.world.content.afk_spots;
 
import com.ruseps.model.Item;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Skill;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.player.Player;
 
public class IceCoinRock {
 
    public static final int STARTING_HP = 10000;
 
    private static Stopwatch timer = new Stopwatch().reset();
    public static SpawnedPool SPAWNED_POOL = null;
    private static LocationData LAST_LOCATION = null;
    private static final int LEVEL_REQ = 1;
    private static final int MINING_XP = 500;
 
    public static class SpawnedPool {
 
        public SpawnedPool(GameObject poolObject, LocationData poolLocation) {
            this.poolObject = poolObject;
            this.poolLocation = poolLocation;
            this.health = STARTING_HP;
        }
 
        public GameObject poolObject; 
        private LocationData poolLocation;
        public int health;
 
        public GameObject getPoolObject() {
            return poolObject;
        }
 
        public LocationData getPoolLocation() {
            return poolLocation;
        }
    }
 
    public static enum LocationData {

        ICE_ROCK_LOCATION(new Position(2768, 2634, 0), "", "");
       
        private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
            this.spawnPos = spawnPos;
            this.clue = clue;
            this.playerPanelFrame = playerPanelFrame;
        }
 
        private Position spawnPos;
        private String clue;
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
			SPAWNED_POOL = new SpawnedPool(new GameObject(15_000, locationData.spawnPos), locationData);
			CustomObjects.spawnGlobalObject(SPAWNED_POOL.poolObject);
		} 
		else {
            if (SPAWNED_POOL.health <= 0) {
               
            	SPAWNED_POOL.health += 10000;
            }
		}
	}
    
    public static void mineIceCoins(Player player) {
       Task iceTask = new Task(2, player, false) {
    	   int tick = 0;
    	   
            @Override
            protected void execute() {
            	if(tick == 1) {
                	player.performAnimation(new Animation(10224));
                	player.performGraphic(new Graphic(2009));
                    SPAWNED_POOL.health -= 1;
                    player.getSkillManager().addExperience(Skill.MINING, MINING_XP);
            	 	player.getInventory().add(19994, 1);
            		tick = 0;
            	}
            	
               if(player.getSkillManager().getCurrentLevel(Skill.MINING) < LEVEL_REQ) {
                    player.sendMessage("@red@The Ice Coin Rock requires " + LEVEL_REQ + " Mining.");
                    stop();
                    return;
                }
               	tick++;
            	}
                   
            @Override
            	public void stop() {
            		super.stop();
            		player.setCurrentTask(null);
            		player.performAnimation(new Animation(65535));
            	}
       		};
 
        			player.setCurrentTask(iceTask);
        			TaskManager.submit(iceTask);
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