/*package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import java.util.concurrent.TimeUnit;

public class SantaEvent extends GameEvent {

    private static final int delay = 1;
    private static final Position spawnPos = new Position(2526, 3932);
    private NPC Santa;
    private long lastHitpointsBroadcasted;
    private boolean over;

    public SantaEvent() {
        super("Santa", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Santa == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        Santa = NPC.of(8540, spawnPos);
        World.register(Santa);
        lastHitpointsBroadcasted = Santa.getDefaultConstitution();
        sendMessage("@blu@<shad=1>The Santa Boss Event has begun! Use command ::Santa to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {
        if (Santa.getConstitution() < 1 || Santa.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	Santa = null;
        over = false;
        sendMessage("The Santa has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Santa Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Santa Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Santa Boss Event is about to begin! Use command ::Santa to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Santa == null || Santa.isDying()) {
            return;
        }

        long currentHitpoints = Santa.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Santa.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Santa.getDefaultConstitution() * 0.75) {
            sendMessage("The Santa has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Santa.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Santa.getDefaultConstitution() * 0.5) {
            sendMessage("The Santa is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Santa.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Santa.getDefaultConstitution() * 0.25) {
            sendMessage("The Santa is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }
}*/
