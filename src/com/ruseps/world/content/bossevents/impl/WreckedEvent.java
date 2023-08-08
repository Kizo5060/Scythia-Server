package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.WreckedLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class WreckedEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 40;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(3354, 3035);

    /**
     * The Madara NPC.
     */
    private NPC Wrecked;

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
    public WreckedEvent() {
        super("Wrecked", TimeUnit.SECONDS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Wrecked == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {

        Wrecked = NPC.of(WreckedLoot.NPC_ID, spawnPos);
        World.register(Wrecked);
        lastHitpointsBroadcasted = Wrecked.getDefaultConstitution();
            sendMessage("<col=FFFFCC><shad=1>The Witch Queen Boss Event has begun! Use command ::witch to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {

        if (Wrecked.getConstitution() < 1 || Wrecked.isDying()) {
            over = true;
        }

    }

    @Override
    public void end() {
    	Wrecked = null;
        over = false;
        sendMessage("Wrecked's Girl has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {

    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Wrecked == null || Wrecked.isDying()) {
            return;
        }

        long currentHitpoints = Wrecked.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Wrecked.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Wrecked.getDefaultConstitution() * 0.75) {
            sendMessage("The Wrecked's Girl has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Wrecked.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Wrecked.getDefaultConstitution() * 0.5) {
            sendMessage("The Wrecked's Girl is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Wrecked.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Wrecked.getDefaultConstitution() * 0.25) {
            sendMessage("The Wrecked's Girl has become weak!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
