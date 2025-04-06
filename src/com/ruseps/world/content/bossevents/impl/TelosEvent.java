package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.TelosLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class TelosEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 1;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2335, 3246);
    
    /**
     * The Madara NPC.
     */
    private NPC Telos;

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
    public TelosEvent() {
        super("Telos", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Telos == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        Telos = NPC.of(TelosLoot.BLUE_NPC_ID, spawnPos);
        World.register(Telos);
        lastHitpointsBroadcasted = Telos.getDefaultConstitution();
        sendMessage("@gre@<shad=1>The Telos Boss Event has begun! Use command ::Telos to Teleport, look North.");
        return true;
    }

    @Override
    public void process() {
        if (Telos.getConstitution() < 1 || Telos.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	Telos = null;
        over = false;
        sendMessage("The Telos has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Telos Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Telos Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Telos Boss Event is about to begin! Use command ::Telos to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Telos == null || Telos.isDying()) {
            return;
        }

        long currentHitpoints = Telos.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Telos.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Telos.getDefaultConstitution() * 0.75) {
            sendMessage("The Telos has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Telos.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Telos.getDefaultConstitution() * 0.5) {
            sendMessage("The Telos is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Telos.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Telos.getDefaultConstitution() * 0.25) {
            sendMessage("The Telos is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
