package com.ruseps.world.content.interface_timers;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.world.entity.impl.player.Player;

public class x2DamageTimer 
{
	Player player;

	public x2DamageTimer(Player player)
	{
		this.player = player;
	}

	public boolean isActive() 
	{
		return player.getDmgTimeLeft() > 0;
	}
	
	public void handleLogin()
	{
		if(isActive())
		{
			player.getPacketSender().sendWalkableInterface(48304, true);
			run();
		}
	}

	public void init() 
	{
		if (isActive()) 
		{
			player.sendMessage("@red@x2 Damage is still active");
			return;
		}
		
		player.setDmgTimeLeft(300000);
		player.setx2DMG(true);
		player.getInventory().delete(600, 1);
		player.performAnimation(new Animation(866));
		player.performGraphic(new Graphic(2009));
		player.sendMessage("@blu@You now have x2 Damage!");
		player.getPacketSender().sendWalkableInterface(48304, true);
		player.forceChat("X2 Damage! Argggggh!");
		run();
	}
	
	private void run() {
		player.getPacketSender().sendString(48306, "Time left: " + (player.getDmgTimeLeft() / 5000) + " minutes");	
		TaskManager.submit(new Task(100, true) {
			@Override
			protected void execute() {
				if (!isActive()) {
					player.sendMessage("@red@Your x2 Damage Potion has run out!");
					player.setx2DMG(false);
					player.getPacketSender().sendWalkableInterface(48304, false);
					stop();
					return;
				}

				player.getPacketSender().sendString(48306, "" + (player.getDmgTimeLeft() / 5000) + " minutes");													// too?no i dont want decrease
				player.setDmgTimeLeft(player.getDmgTimeLeft() - 5000);

			}
		});
	}

}
