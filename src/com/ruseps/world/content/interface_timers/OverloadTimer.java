package com.ruseps.world.content.interface_timers;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.world.entity.impl.player.Player;

public class OverloadTimer {

	Player player;

	public OverloadTimer(Player player) {
		this.player = player;
	}

	public boolean isActive() {
		return player.getTimeLeft() > 0;
	}
	

	public void handleLogin() {
		if(isActive()) {
			player.getPacketSender().sendWalkableInterface(48300, true);
			System.out.println("Login -->");
			
			System.out.println("Time left: " + player.getTimeLeft());
			run();
		}
	}

	public void init() {
		if (isActive()) {
			player.sendMessage("Overload is still active");
			return;
		}
		
		player.setTimeLeft(32400);
		player.getPacketSender().sendWalkableInterface(48300, true);
		run();
	}
	
	private void run() {
		System.out.println("Time left: " + player.getTimeLeft());
		player.getPacketSender().sendString(48302, "Time left: " + (player.getTimeLeft() / 5000) + " minutes");	
		TaskManager.submit(new Task(100, true) {
			@Override
			protected void execute() {
				if (!isActive()) {
					player.getPacketSender().sendWalkableInterface(48300, false);
					player.sendMessage("@red@Your Overload has run out!");
					stop();
					return;
				}
				

				player.getPacketSender().sendString(48302, "" + (player.getTimeLeft() / 5000) + " minutes");													// too?no i dont want decrease
				player.setTimeLeft(player.getTimeLeft() - 5000);

			}
		});
	}

}
