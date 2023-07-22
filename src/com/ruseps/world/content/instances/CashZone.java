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

public class CashZone
{
    public static void enterCashZone(final Player p) 
    {
        p.getPacketSender().sendInterfaceRemoval();
		p.getInventory().delete(10835, 1);
        p.moveTo(new Position(2326, 3613, p.getIndex() * 4));
        p.setRegionInstance(new RegionInstance(p, RegionInstanceType.CASH_ZONE_INSTANCE));
        TaskManager.submit(new Task(1, p, false)
        {
            int tick = 0;
            @Override
            public void execute()
            {
            	if(tick >= 3) {
        			NPC n = new NPC(184, new Position(p.getPosition().getX() + Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC no = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC noo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC nooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC noooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC nooooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC noooooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC nooooooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC noooooooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
        			NPC nooooooooo = new NPC(184, new Position(p.getPosition().getX()+ Misc.getRandom(6), p.getPosition().getY() + Misc.getRandom(6), p.getPosition().getZ())).setSpawnedFor(p);
					World.register(n);
					World.register(no);
					World.register(noo);
					World.register(nooo);
					World.register(noooo);
					World.register(nooooo);
					World.register(noooooo);
					World.register(nooooooo);
					World.register(noooooooo);
					World.register(nooooooooo);
					stop();
            	}
                if(tick >= 500) 
                {
                	exitCashZone(p);
                    stop();
                }
                tick++;
            }
        });
    }
    
    public static void exitCashZone(final Player p)
    {
    	 p.moveTo(GameSettings.DEFAULT_POSITION);
    }
}
