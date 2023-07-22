package com.ruseps.world.content.combat.strategy.zulrah.npc;

import java.lang.reflect.InvocationTargetException;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.combat.strategy.zulrah.Zulrah;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Blue implements CombatStrategy {

	private static NPC ZULRAH;
	
	private static boolean isIdle;
	private static int phaseID;
	private static int constitution;
	public static boolean setConstitution = false;
	
	public void spawn(Player player, int zulrahConstitution, int phaseID) {
		Position respawn = new Position(player.getPosition().getX(), player.getPosition().getY(), player.getIndex() *4);
		player.moveTo(respawn);
		setConstitution = false;
		Blue.phaseID = phaseID;
		ZULRAH = new NPC(Zulrah.BLUE_ZULRAH_ID, new Position(Zulrah.zulrahPhases.get(phaseID).getZulrahX(), Zulrah.zulrahPhases.get(phaseID).getZulrahY(),player.getIndex() *4 ));
		World.register(ZULRAH);
		ZULRAH.performAnimation(Zulrah.RISE);
		Blue.constitution = 5000 - zulrahConstitution;
		ZULRAH.setEntityInteraction(player);
		ZULRAH.getCombatBuilder().attack(player);
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		// TODO Auto-generated method stub
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
			case 3:
			case 6:
				normalAttack(entity, victim);
				break;
		}
		return true; 
	}
	
	private void normalAttack(Character entity, Character victim) {
		int rand = Misc.getRandom(12);
		if(rand != 9 && rand != 4 && rand != 5 && rand != 6 && rand != 7) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1046, 44, 1, 43, 43, 0).sendProjectile();
					}
					if(tick == 1) {
						if(victim.isPlayer()) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.MAGIC, true)).handleAttack();
						}
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
						new Projectile(entity, victim, 1044, 44, 1, 43, 43, 0).sendProjectile();
					} 
					if(tick == 1) {
						if(victim.isNpc()) {
							return;
						}
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.RANGED, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else {
			despawn(entity, (Player) victim);
		}
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
		// TODO Auto-generated method stub
		return null;
	}
	
	public static boolean isIdle() {
		return isIdle;
	}

	public static void setIdle(boolean isIdle) {
		Blue.isIdle = isIdle;
	}

}
