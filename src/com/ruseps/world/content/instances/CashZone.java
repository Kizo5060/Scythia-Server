package com.ruseps.world.content.instances;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CashZone {
    private static final int SPAWN_LIMIT = 12;
    private static final int SPAWN_TIMER = 2; // in seconds
    private static final int MAX_RUNTIME = 30 * 60; // 30 minutes

    // Map to associate players with their spawned NPCs
    private static Map<Player, List<NPC>> playerNPCsMap = new HashMap<>();

    public static void enterCashZone(final Player p) {
    	if(p.isInCashZone()) {
    		p.sendMessage("@red@You Are Already In Cash Zone Instance");
    		return;
    	} 	
        p.getPacketSender().sendInterfaceRemoval();
        p.setInCashZone(true);
        p.getInventory().delete(10835, 1);
        p.moveTo(new Position(2326, 3613, p.getIndex() * 4));
        RegionInstance instance = new RegionInstance(p, RegionInstanceType.CASH_ZONE_INSTANCE);
        p.setRegionInstance(instance);

        List<NPC> playerNPCs = new ArrayList<>();
        playerNPCsMap.put(p, playerNPCs);

        TaskManager.submit(new Task(1, p, false) {
            int tick = 0;

            @Override
            public void execute() {
                if (tick % SPAWN_TIMER == 0 && tick < MAX_RUNTIME) {
                    spawnNpcsIfNeeded(p);
                    int x = p.getPosition().getX();
                    int y = p.getPosition().getY();
                    if((y > 3660 || y < 3580) && (x > 2360 || 2280 > x)){
                    	   List<NPC> instance = World.getNpcs().stream()
                   				.filter(t -> t != null && t.getPosition().getZ() == p.getIndex() * 4)
                   				.collect(Collectors.toList());

                   		for (NPC fl : instance) {
                   			World.deregister(fl);
                   		}
                   		p.setInCashZone(false);
                    	exitCashZone(p,false);
                    	//instance.destruct();
                        stop();
                    }
                } else if (tick >= MAX_RUNTIME) {
                	   List<NPC> instance = World.getNpcs().stream()
               				.filter(t -> t != null && t.getPosition().getZ() == p.getIndex() * 4)
               				.collect(Collectors.toList());

               		for (NPC fl : instance) {
               			World.deregister(fl);
               		}
               		p.setInCashZone(false);
                    exitCashZone(p,true);
                  //  instance.destruct();
                    stop();
                }
                tick++;
            }
        });
    }

    private static void spawnNpcsIfNeeded(Player p) {
        List<NPC> playerNPCs = playerNPCsMap.get(p);
        if (playerNPCs.size() < SPAWN_LIMIT) {
            Position npcPosition = generateValidPosition(p);
            if (npcPosition != null) {
                NPC n = new NPC(184, npcPosition).setSpawnedFor(p);
                World.register(n);
                playerNPCs.add(n);
            }
        }
    }

    public static void exitCashZone(final Player p,boolean move) {
    	if(move)
    		p.moveTo(GameSettings.DEFAULT_POSITION);
        List<NPC> instance = World.getNpcs().stream()
				.filter(t -> t != null && t.getPosition().getZ() == p.getIndex() * 4)
				.collect(Collectors.toList());

		for (NPC fl : instance) {
			World.deregister(fl);
		}
        List<NPC> playerNPCs = playerNPCsMap.get(p);
        if (playerNPCs != null) {
            for (NPC npc : playerNPCs) {
                World.deregister(npc);
            }
            playerNPCs.clear();
            playerNPCsMap.remove(p);
        }
    }

    private static Position generateValidPosition(Player playerPosition) {
        // Generate a random position around the player within the instance boundaries
        int x = 2335 + Misc.getRandom(6);
        int y = 3617 + Misc.getRandom(6);
        int z = playerPosition.getIndex() * 4;

        // Validate the position to ensure it's within the instance boundaries
        // Implement your validation logic here if necessary

        return new Position(x, y, z);
    }
}