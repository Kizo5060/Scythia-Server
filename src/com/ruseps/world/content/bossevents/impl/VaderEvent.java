package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.VaderLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class VaderEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 2;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2933, 2720);

    /**
     * The Madara NPC.
     */
    private NPC Vader;

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
    public VaderEvent() {
        super("Vader", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Vader == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {

        Vader = NPC.of(VaderLoot.NPC_ID, spawnPos);
        World.register(Vader);
        lastHitpointsBroadcasted = Vader.getDefaultConstitution();
        sendMessage("<col=FFFFCC><shad=1>The Vader Boss Event has begun! Use command ::Vader to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {

        if (Vader.getConstitution() < 1 || Vader.isDying()) {
            over = true;
        }

    }

    @Override
    public void end() {
    	Vader = null;
        over = false;
        sendMessage("The Vader has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Vader Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Vader Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Vader Boss Event is about to begin! Use command ::Vader to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Vader == null || Vader.isDying()) {
            return;
        }

        long currentHitpoints = Vader.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Vader.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Vader.getDefaultConstitution() * 0.75) {
            sendMessage("The Vader has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Vader.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Vader.getDefaultConstitution() * 0.5) {
            sendMessage("The Vader is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Vader.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Vader.getDefaultConstitution() * 0.25) {
            sendMessage("The Vader is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
