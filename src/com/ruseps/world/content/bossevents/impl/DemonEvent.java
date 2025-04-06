package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.DemonLoot;
import com.ruseps.net.packet.impl.CommandPacketListener;
import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class DemonEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 3 ;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2337, 3209);
    
    /**
     * The Madara NPC.
     */
    private NPC DemonEvent;

    /**
     * The hitpoints that was last broadcasted.
     */
    private long lastHitpointsBroadcasted;

    /**
     * If the event is over.
     */
    private boolean over;

    /**
     * Constructor for the event.
     */
    public DemonEvent() {
        super("Lava Demon", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return DemonEvent == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        DemonEvent = NPC.of(DemonLoot.NPC_ID, spawnPos);
        World.register(DemonEvent);
        lastHitpointsBroadcasted = DemonEvent.getDefaultConstitution();
        sendMessage("@red@<shad=1>The Lava Demon has begun! Use command ::Lava to Teleport, look South.");
        return true;
    }

    @Override
    public void process() {
        if (DemonEvent.getConstitution() < 1 || DemonEvent.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	DemonEvent = null;
        over = false;
        sendMessage("The Lava Demon has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Lava Demon Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Lava Demon Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Lava Demon Event is about to begin! Use command ::Lava to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (DemonEvent == null || DemonEvent.isDying()) {
            return;
        }

        long currentHitpoints = DemonEvent.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

    }

}
