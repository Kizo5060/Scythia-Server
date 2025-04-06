package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.OwnerBossLoot;
import java.util.concurrent.TimeUnit;

public class OwnerBossEvent extends GameEvent {

    private static final int delay = 5;
    private static final Position spawnPos = new Position(2853, 3927);
    private NPC OwnerBossEvent;
    private long lastHitpointsBroadcasted;
    private boolean over;

    public OwnerBossEvent() {
        super("Owner Boss", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return OwnerBossEvent == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {
        OwnerBossEvent = NPC.of(OwnerBossLoot.NPC_ID, spawnPos);
        World.register(OwnerBossEvent);
        lastHitpointsBroadcasted = OwnerBossEvent.getDefaultConstitution();
        sendMessage("@red@<shad=1>The Owner Boss Event has begun! Use command ::owner to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {
        if (OwnerBossEvent.getConstitution() < 1 || OwnerBossEvent.isDying()) {
            over = true;
        }
    }

    @Override
    public void end() {
    	OwnerBossEvent = null;
        over = false;
        sendMessage("The Owner Boss has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("The Owner Boss Event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("The Owner Boss Event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("The Owner Boss Event is about to begin! Use command ::owner to join!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (OwnerBossEvent == null || OwnerBossEvent.isDying()) {
            return;
        }

        long currentHitpoints =  OwnerBossEvent.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= OwnerBossEvent.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > OwnerBossEvent.getDefaultConstitution() * 0.75) {
            sendMessage("The Owner Boss has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= OwnerBossEvent.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > OwnerBossEvent.getDefaultConstitution() * 0.5) {
            sendMessage("The Owner Boss is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= OwnerBossEvent.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > OwnerBossEvent.getDefaultConstitution() * 0.25) {
            sendMessage("The Owner Boss is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
