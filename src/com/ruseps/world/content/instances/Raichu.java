package com.ruseps.world.content.instances;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Graphic;
import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Raichu
{
    public static void startRaichuEvent(final Player p) {
        p.getPacketSender().sendInterfaceRemoval();
        p.moveTo(new Position(2334, 3208, 0));
        TaskManager.submit(new Task(1, p, false) {
            int tick = 0;
            @Override
            public void execute() 
            {
            	if(tick == 50)
            	{
            		World.sendMessage("@blu@<img=10>The Voting Event will begin in 15 seconds @ ::voteboss!");
            	}
            	if(tick == 25) 
            	{
            		World.sendMessage("@blu@<img=10>The Voting Event will begin in 30 seconds @ ::voteboss!");
            	}
                if(tick >= 100)
                {
                    NPC n = new NPC(4340, new Position(p.getPosition().getX(), p.getPosition().getY() - 2, p.getPosition().getZ()));
                    p.getRegionInstance().spawnNPC(new NPC(4340, new Position(2897, 3230, 0)));
                    World.sendMessage("@blu@<img=10>The Voting Event has begun! Join @ ::voteboss!");
                    n.getCombatBuilder().attack(p);
                    n.performGraphic(new Graphic(2009));
                    stop();
                }
                
                tick++;
            }
        });
    }
}
