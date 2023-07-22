package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.BlessedSpartanLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class BlessedSpartanEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 4;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2326, 3235);
    
    /**
     * The Madara NPC.
     */
    private NPC BlessedSpartan;

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
    public BlessedSpartanEvent() {
        super("Blessed Spartan", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return BlessedSpartan == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        BlessedSpartan = NPC.of(BlessedSpartanLoot.NPC_ID, spawnPos);
        World.register(BlessedSpartan);
        lastHitpointsBroadcasted = BlessedSpartan.getDefaultConstitution();
        sendMessage("<col=FFFFCC><shad=1>The Blessed Spartan Boss Event has begun! Use command ::Spartan to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {
        if (BlessedSpartan.getConstitution() < 1 || BlessedSpartan.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	BlessedSpartan = null;
        over = false;
        sendMessage("The Blessed Spartan has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Blessed Spartan Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Blessed Spartan Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Blessed Spartan Boss Event is about to begin! Use command ::BlessedSpartan to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (BlessedSpartan == null || BlessedSpartan.isDying()) {
            return;
        }

        long currentHitpoints = BlessedSpartan.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= BlessedSpartan.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > BlessedSpartan.getDefaultConstitution() * 0.75) {
            sendMessage("The Blessed Spartan has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= BlessedSpartan.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > BlessedSpartan.getDefaultConstitution() * 0.5) {
            sendMessage("The Blessed Spartan is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= BlessedSpartan.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > BlessedSpartan.getDefaultConstitution() * 0.25) {
            sendMessage("The Blessed Spartan is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
