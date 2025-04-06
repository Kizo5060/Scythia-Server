package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.DragonLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class DragonEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 3;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(2565, 4945);
    
    /**
     * The Madara NPC.
     */
    private NPC Dragon;

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
    public DragonEvent() {
        super("Dragon", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Dragon == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        Dragon = NPC.of(DragonLoot.BLUE_NPC_ID, spawnPos);
        World.register(Dragon);
        lastHitpointsBroadcasted = Dragon.getDefaultConstitution();
        sendMessage("@blu@<shad=1>The Dragon Boss Event has begun! Use command ::Dragon to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {
        if (Dragon.getConstitution() < 1 || Dragon.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	Dragon = null;
        over = false;
        sendMessage("The Dragon has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Dragon Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Dragon Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Dragon Boss Event is about to begin! Use command ::Dragon to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Dragon == null || Dragon.isDying()) {
            return;
        }

        long currentHitpoints = Dragon.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Dragon.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Dragon.getDefaultConstitution() * 0.75) {
            sendMessage("The Dragon has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Dragon.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Dragon.getDefaultConstitution() * 0.5) {
            sendMessage("The Dragon is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Dragon.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Dragon.getDefaultConstitution() * 0.25) {
            sendMessage("The Dragon is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
