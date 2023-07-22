/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruseps.world.content.upgrading;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.world.World;
import com.ruseps.world.content.fuser.FuserEnum;
import com.ruseps.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class UpgradeListener {
 	private Player player;

 	private static int upgrade_cost_id = 19992;
	public UpgradeListener(Player player) {
		this.player = player;
	}
	public void upgrade() {
		Arrays.stream(UpgradeData.values()).forEach(val -> {
			if (val.getRequired().getId() == player.getUpgradeSelection().getId()) {
				if (getRestrictions(val)) {
					player.getInventory().delete(val.getRequired());//activate for deletion.
					player.getInventory().delete(upgrade_cost_id, val.getCoinsRequired());//change item id for requirement here
					TimerTask task = new TimerTask() {
						int tick = 0;

						@Override
						public void run() {
							if (tick == 0) {
								player.getPacketSender().sendMessage("@red@You try to upgrade....");
							} else if (tick == 3) {
								boolean success = random.nextInt(100) <= val.getChance() ? true : false;
								if (success) {
									player.getPacketSender().sendMessage("@blu@You succesfully upgraded your item!");
									World.sendMessage("<shad=0><col=23545>@yel@[UPGRADE] "+player.getUsername()+" @red@has created a "+ "@yel@" + val.getReward().getDefinition().getName() +"!");
									player.getInventory().add(val.getReward());
								} else {
									player.getPacketSender().sendMessage("@red@You failed to upgrade!");
								}
								cancel();
							}
							tick++;
						}

					};

					Timer timer = new Timer();
					timer.schedule(task, 500, 500);
					;
				}
			} else {
			}
		});
	}

	private boolean getRestrictions(UpgradeData data) {
		if (!player.getInventory().contains(data.getRequired().getId()) || player.getInventory().getAmount(19992) < data.getCoinsRequired()) {
			player.getPacketSender().sendMessage("@red@You do not have the required items!");
			return false;
		}
		return true;
	}

	private Random random = new Random();
}

