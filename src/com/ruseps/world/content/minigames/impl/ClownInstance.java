package com.ruseps.world.content.minigames.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
/** M.O.B. **/
public class ClownInstance {
	public static void startFight(final Player p) 
	{
		p.getPacketSender().sendInterfaceRemoval();
		p.getInventory().delete(11180, 1);
		p.moveTo(new Position(3018, 4401, p.getIndex() * 4));
		p.setRegionInstance(new RegionInstance(p, RegionInstanceType.CLOWN));
		p.sendMessage("@blu@Welcome to your private It's Instance. Stay as long as you like!");
		TaskManager.submit(new Task(1, p, false)
		{
			int tick = 0;
			@Override
			public void execute()
			{
				if(tick >= 4)
				{
					NPC n = new NPC(3014, new Position(p.getPosition().getX(), p.getPosition().getY() - 2, p.getPosition().getZ())).setSpawnedFor(p);
					World.register(n);
					p.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(p);
					n.performGraphic(new Graphic(2009));
					stop();
				}
				tick++;
			}
		});
	}
}