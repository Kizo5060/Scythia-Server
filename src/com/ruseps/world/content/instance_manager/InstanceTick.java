package com.ruseps.world.content.instance_manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ruseps.world.entity.impl.player.Player;

public class InstanceTick {
	
	public static void tick(Player player) {
		if(player.getInstanceManager().inInstance && player.getInstanceManager().timeSelected != LocalDateTime.MIN) {
			if(LocalDateTime.now().until(player.getInstanceManager().timeSelected, ChronoUnit.MINUTES) <= 0) {
				player.getInstanceManager().endInstance(player, false);
			}
		}
	}
}
