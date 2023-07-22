package com.ruseps.world.content.minigames.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class RaichuInstance {

	public static final int RAICHU_NPC_ID = 4340;

	public static void enterVoteBoss(Player player) {
		player.moveTo(new Position(2897, 3230, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RAICHU_INSTANCE));
		spawnRaichu(player);
	}

	public static void leaveVoteBoss(Player player, boolean resetStats) {
		Locations.Location.VOTE_BOSS.leave(player);
		if(resetStats)
			player.restart();
	}

	public static void spawnRaichu(final Player player) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (player.getRegionInstance() == null || !player.isRegistered()) {
					stop();
					return;
				}
				
			   	player.getRegionInstance().spawnNPC(new NPC(RAICHU_NPC_ID, new Position(2905, 3231, player.getPosition().getZ())).setSpawnedFor(player));
				stop();
			}
		});
	}

	public static void handleRaichuDeath(final Player player, NPC n) {
		if(n.getId() == RAICHU_NPC_ID) {
			
			if(player.getRegionInstance() != null)
				player.getRegionInstance().getNpcsList().remove(n);
			leaveVoteBoss(player, true);
			player.getInventory().add(18338, 1).add(995, 50000000 + Misc.getRandom(75000000));
		}
	}

}
