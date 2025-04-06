package com.ruseps.world.content.afk_spots;
 
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.player.Player;
 
public class AfkTree {
 
    public static final int STARTING_HP = 10000;
 
    private static Stopwatch timer = new Stopwatch().reset();
    public static SpawnedPool SPAWNED_POOL = null;
    private static LocationData LAST_LOCATION = null;
    private static final int LEVEL_REQ = 1;
    private static final int WC_XP = 500;
 
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

        WELL_LOCATION(new Position(2768, 2634, 0), "", "");
       
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
			SPAWNED_POOL = new SpawnedPool(new GameObject(16604, locationData.spawnPos), locationData);
			CustomObjects.spawnGlobalObject(SPAWNED_POOL.poolObject);
		} 
		else {
            if (SPAWNED_POOL.health <= 0) {
               
            	SPAWNED_POOL.health += 10000;
            }
		}
	}
	/*
     * Random New Blood Bottle Chance
     */
    public static void bloodBottle(Player p) {
		if (p.getPosition().getZ() > 0) {
			return;
		}
		if (Misc.getRandom(3000) == 1) {
			 Item box = new Item(455, 1);
	            int r = Misc.getRandom(500);
	            if (r >= 0 && r <= 50) {
	             p.getInventory().add(box.getId(), box.getAmount());
	            }
			if (box != null) {
				box.setAmount(1);
				 p.getInventory().add(box.getId(), box.getAmount());
				 p.sendMessage("@red@Your Scratch Card random prize has been sent to your inventory, or bank if inventory was full.");
			}
		}
	}
    
    public static void fillBloodEssence(Player player) {
       Task bloodTask = new Task(2, player, false) {
    	   
            @Override
            protected void execute() {
          
            	
               if(player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < LEVEL_REQ) {
                    player.sendMessage("@red@The AFK Tree requires " + LEVEL_REQ + " Woodcutting.");
                    stop();
                    return;
                }
            
                	player.performAnimation(new Animation(875));
                	player.performGraphic(new Graphic(1177));
                    SPAWNED_POOL.health -= 1;
                    player.getSkillManager().addExperience(Skill.WOODCUTTING, WC_XP);
            	 	player.getInventory().add(5022, 1);
            	 	bloodBottle(player);
            	 	
            	 	if (Misc.getRandom(50000) == 0) {
            	 	    player.getInventory().add(18782, 1);
            	 	    World.sendMessage("@blu@<img=10>[Rare Item] " + player.getUsername() + " has received a Dragonkin Lamp!");
            	 	}
            	 	if (Misc.getRandom(14000) == 3) {
                        player.getInventory().add(13016, 1);
                        World.sendMessage("@blu@<img=10>[Skilling Pets] " + player.getUsername() + " has received a Pet Box!");
                        player.getPacketSender().sendMessage("@red@Good luck on the pet!");
                    } else if (player.getRights() == PlayerRights.GOLD_MEMBER || player.getRights() == PlayerRights.MODERATOR) {
                        player.getInventory().add(5022, 3);
                    } else if (player.getRights() == PlayerRights.PLATINUM_MEMBER || player.getRights() == PlayerRights.DIAMOND_MEMBER
                    	|| player.getRights() == PlayerRights.ADMINISTRATOR) {
                        player.getInventory().add(5022, 5);
                    } else if (player.getRights() == PlayerRights.RUBY_MEMBER || player.getRights() == PlayerRights.DRAGONSTONE_MEMBER 
                    	|| player.getRights() == PlayerRights.OWNER){
                        player.getInventory().add(5022, 10);
                    
                    }
            	}
                   
            @Override
            	public void stop() {
            		super.stop();
            		player.setCurrentTask(null);
            		player.performAnimation(new Animation(65535));
            	}
       		};
 
        			player.setCurrentTask(bloodTask);
        			TaskManager.submit(bloodTask);
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