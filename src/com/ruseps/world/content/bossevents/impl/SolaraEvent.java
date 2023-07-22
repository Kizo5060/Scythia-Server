package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.AuroraLoot;

import java.util.concurrent.TimeUnit;

/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class SolaraEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 6;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2311, 3212);
    
    /**
     * The Madara NPC.
     */
    private NPC AuroraEvent;

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
    public SolaraEvent() {
        super("Solara's Shooter", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return AuroraEvent == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        AuroraEvent = NPC.of(AuroraLoot.NPC_ID, spawnPos);
        World.register(AuroraEvent);
        lastHitpointsBroadcasted = AuroraEvent.getDefaultConstitution();
        sendMessage("<col=FFFFCC><shad=1>The Solara's Shooter has begun! Use command ::solara to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {
        if (AuroraEvent.getConstitution() < 1 || AuroraEvent.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	AuroraEvent = null;
        over = false;
        sendMessage("The Aurora's Shooter has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Aurora's Shooter Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Aurora's Shooter Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Aurora's Shooter Event is about to begin! Use command ::Aurora to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (AuroraEvent == null || AuroraEvent.isDying()) {
            return;
        }

        long currentHitpoints = AuroraEvent.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= AuroraEvent.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > AuroraEvent.getDefaultConstitution() * 0.75) {
            sendMessage("The Aurora's Shooter has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= AuroraEvent.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > AuroraEvent.getDefaultConstitution() * 0.5) {
            sendMessage("The Aurora's Shooter is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= AuroraEvent.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > AuroraEvent.getDefaultConstitution() * 0.25) {
            sendMessage("The Aurora's Shooter is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
