package com.ruseps.world.content.combat.strategy.zulrah.npc;

import java.lang.reflect.InvocationTargetException;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.combat.strategy.zulrah.Zulrah;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Green implements CombatStrategy {
	
	private static NPC ZULRAH;
	private static NPC SNAKELING1;
	private static NPC SNAKELING2;
	private static NPC CLOUD1;
	private static NPC CLOUD2;
	private static NPC CLOUD3;
	private static NPC CLOUD4;
	private static NPC CLOUD5;
	private static NPC CLOUD6;
	private static NPC CLOUD7;
	private static NPC CLOUD8;
	
	private static NPC[] CloudTiles = {CLOUD1, CLOUD2, CLOUD3, CLOUD4, CLOUD5, CLOUD6, CLOUD7, CLOUD8};
	
	public static void spawnCloudTiles(Character victim) { 
		for(int i = 0; i < 8; i++) {
			CloudTiles[i] = new NPC(0, new Position(Zulrah.TOXIC_FUME_LOCATIONS_1[i][0], Zulrah.TOXIC_FUME_LOCATIONS_1[i][1], victim.getIndex() * 4));
			World.register(CloudTiles[i]);
		}
	}
	public static void despawnCloudTiles(Player player) { 
		for(int i = 0; i < 8; i++) {
			World.deregister(CloudTiles[i]);
		}
	}
	
	private static boolean isIdle;
	private static int phaseID;
	private static int constitution;
	public static boolean setConstitution = false;
	
	public void spawn(Player player, int zulrahConstitution, int phaseID) {
		Position respawn = new Position(player.getPosition().getX(), player.getPosition().getY(), player.getIndex() *4);
		player.moveTo(respawn);
		
		setConstitution = false;
		Green.phaseID = phaseID;
		ZULRAH = new NPC(Zulrah.GREEN_ZULRAH_ID, new Position(Zulrah.zulrahPhases.get(phaseID).getZulrahX(), Zulrah.zulrahPhases.get(phaseID).getZulrahY(), player.getIndex() *4 ));
		World.register(ZULRAH);
		//Green.constitution = 5000 - zulrahConstitution;
		ZULRAH.performAnimation(Zulrah.RISE);
		ZULRAH.setEntityInteraction(player);
		ZULRAH.getCombatBuilder().attack(player);
		
		
		//its the green spawn or despawn or something in green. so far only seen it bug on green zulrah
	
		
	}
	
	public static void despawn(Character entity, Player player) {
		setIdle(true);
		TaskManager.submit(new Task(1, true) {
			int tick;
			@Override
			public void execute() {
				if(tick == 0) {
					ZULRAH.performAnimation(Zulrah.DIVE);
					TaskManager.submit(new Task(2, ZULRAH, false) {
						@Override
						public void execute() {
							World.deregister(ZULRAH);
						
							stop();
						}
					});							
				}
				if(tick == 3) {
					try {
						Zulrah.getNextPhase(player, entity.getConstitution(), Zulrah.zulrahPhases.get(phaseID).getNextPhase());
						player.getPA().sendMessage("phase: "+Zulrah.zulrahPhases.get(phaseID).getNextPhase());

						player.getPA().sendMessage("should be respawning now");
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						player.getPA().sendMessage("ZULRAH DIPPED");
					}
					stop();
					setIdle(false);
				}
				tick++;
			} 
		});
	}

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		if(!setConstitution) {
			entity.setConstitution(5000 - constitution);
			setConstitution = true;
		}
		if(isIdle()) {
			return true;
		}
		switch(phaseID) {
			case 1:
				if(victim.isPlayer()) {
				greenZulrahFullCloudSpawn(entity, victim);
				}
				break;
			case 4:
			case 7:
				if(victim.isNpc()) {
				
				}
				greenZulrahRightCloudSpawn(entity, victim);
				break;
			case 8:
				if(victim.isPlayer()) {
				rangeMage(entity, victim);
				}
				break;
		}
		return true; 
	}

	@Override
	public int attackDelay(Character entity) {
		return 7;
	}

	@Override
	public int attackDistance(Character entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return null;
	}

	public static boolean isIdle() {
		return isIdle;
	}

	public static void setIdle(boolean isIdle) {
		Green.isIdle = isIdle;
	}
	
	public static void greenZulrahFullCloudSpawn(Character entity, Character victim) {
		setIdle(true);
		spawnCloudTiles(victim);
		TaskManager.submit(new Task(1, true) {
			int tick;
			int cloud = 1;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				Player player = (Player) victim;
				player.setCloudsSpawned(true);
				for(int i = 1; i < 4; i++) {
					if(tick == i*4) {
						entity.getCombatBuilder().attack(CloudTiles[cloud]);
					}
					
					if(tick == i*4+2){
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, CloudTiles[cloud], 1045, 44, 1, 43, 0, 0).sendProjectile();
						new Projectile(entity, CloudTiles[cloud+1], 1045, 44, 1, 43, 0, 0).sendProjectile();
					}
					
					if(tick == i*4+3){
						CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud].getPosition()), (Player) victim, 40);
						CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud+1].getPosition()), (Player) victim, 40);
						cloud = cloud + 2;
					} 
					if(tick == 16) {
						entity.getCombatBuilder().attack(CloudTiles[4]);
					}
				}
				if(tick == 17) {
					setIdle(false);
					
					despawn(entity, (Player) victim);
				}
				tick++;
			}
		});
	}
	
	public static void greenZulrahRightCloudSpawn(Character entity, Character victim) {
		setIdle(true);
		spawnCloudTiles(victim);
		TaskManager.submit(new Task(1, true) {
			int tick;
			int cloud = 3;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				for(int i = 1; i < 3; i++) {
					if(tick == i*4) {
						entity.getCombatBuilder().attack(CloudTiles[cloud]);
					}
					
					if(tick == i*4+2){
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, CloudTiles[cloud], 1045, 44, 1, 43, 0, 0).sendProjectile();
						new Projectile(entity, CloudTiles[cloud+1], 1045, 44, 1, 43, 0, 0).sendProjectile();
					}
					
					if(tick == i*4+3){
						if(victim.isNpc()) {
							
						} else {
							CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud].getPosition()), (Player) victim, 40);
							CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud+1].getPosition()), (Player) victim, 40);
							cloud = cloud + 2;
						}
						
					} 
					if(tick == 16) {
						entity.getCombatBuilder().attack(CloudTiles[4]);
					}
				}
				if(tick == 17) {
					setIdle(false);
					if(victim.isPlayer()) {
					despawn(entity, (Player) victim);
					}
				}
				tick++;
			}
		});
	}
	
	public static void snakelingAndClouds(Character entity, Character victim) {
		setIdle(true);
		Position position1 = new Position(victim.getPosition().getX(),victim.getPosition().getY()+1, victim.getIndex() * 4);
		Position position2 = new Position(victim.getPosition().getX(),victim.getPosition().getY()-1, victim.getIndex() * 4);
		CloudTiles[1] = new NPC(0, position1);
		CloudTiles[2] = new NPC(0, position2);
		World.register(CloudTiles[1]);
		World.register(CloudTiles[2]);
		TaskManager.submit(new Task(1, true) {
			int tick;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				if(tick == 3) {
					entity.performAnimation(new Animation(5069));
					new Projectile(entity, CloudTiles[1], 1044, 44, 1, 43, 43, 0).sendProjectile();
					new Projectile(entity, CloudTiles[2], 1044, 44, 1, 43, 43, 0).sendProjectile();
					World.deregister(CloudTiles[1]);
					World.deregister(CloudTiles[2]);
				} else if(tick == 4) {
					SNAKELING1 = new NPC(2045, position1);
					SNAKELING2 = new NPC(2045, position2);
					World.register(SNAKELING1);
					World.register(SNAKELING2);
				} else if(tick == 6) {
					greenZulrahRightCloudSpawn(entity, victim);
					stop();
				}
				tick++;
			}
		});
	}
	
	private void rangeMage(Character entity, Character victim) {
		int rand = Misc.getRandom(12);
		if(rand != 9 && rand != 4 && rand != 5 && rand != 6 && rand != 7) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1044, 44, 1, 43, 43, 0).sendProjectile();
					} 
					if(tick == 1) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.RANGED, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else if(rand == 4 || rand == 5 || rand == 6 || rand == 7) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1046, 44, 1, 43, 43, 0).sendProjectile();
					}
					if(tick == 1) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.MAGIC, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else {
			if(victim.isNpc()) {
				return;
			}
			despawn(entity, (Player) victim);
			
		}
	}

}

