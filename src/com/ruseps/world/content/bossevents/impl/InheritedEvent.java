package com.ruseps.world.content.bossevents.impl; 

import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.npc.impl.InheritedLoot;

import java.util.concurrent.TimeUnit;

//so watch.
/**
 * The boss Madara event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class InheritedEvent extends GameEvent {

    /**
     * The delay between the events in hours.
     */
    private static final int delay = 1;

    /**
     * The spawn position for the Madara.
     */
    private static final Position spawnPos = new Position(3354, 3035);

    /**
     * The Madara NPC.
     */
    private NPC Inherited;

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
    public InheritedEvent() {
        super("Inherited", TimeUnit.HOURS.toMillis(delay));
    }

    @Override
    public boolean canStart() {
        return Inherited == null;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {

        Inherited = NPC.of(InheritedLoot.NPC_ID, spawnPos);
        World.register(Inherited);
        lastHitpointsBroadcasted = Inherited.getDefaultConstitution();
            sendMessage("@mag@<shad=1>Inherited's Girlfriend has spawned! Use command ::witch to Teleport to the Event.");
        return true;
    }

    @Override
    public void process() {

        if (Inherited.getConstitution() < 1 || Inherited.isDying()) {
            over = true;
        }

    }

    @Override
    public void end() {
    	Inherited = null;
        over = false;
        sendMessage("Inherited's Girl has been defeated!");
        lastHitpointsBroadcasted = 0;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {

    }

    @Override
    public void eventMessages(long timeLeft) {

        if (Inherited == null || Inherited.isDying()) {
            return;
        }

        long currentHitpoints = Inherited.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= Inherited.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > Inherited.getDefaultConstitution() * 0.75) {
            sendMessage("Inherited's Girl has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Inherited.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > Inherited.getDefaultConstitution() * 0.5) {
            sendMessage("Inherited's Girl is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= Inherited.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > Inherited.getDefaultConstitution() * 0.25) {
            sendMessage("Inherited's Girl has become weak!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }

}
