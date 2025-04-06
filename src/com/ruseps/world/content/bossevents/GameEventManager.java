package com.ruseps.world.content.bossevents;

import com.google.common.reflect.ClassPath;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.ruseps.world.content.bossevents.impl.DemonEvent;
import com.ruseps.world.World;
import com.ruseps.world.content.bossevents.impl.BlessedSpartanEvent;
import com.ruseps.world.content.bossevents.impl.DragonEvent;
import com.ruseps.world.content.bossevents.impl.TelosEvent;
import com.ruseps.world.content.bossevents.impl.VaderEvent;
import com.ruseps.world.content.bossevents.impl.ReaperEvent;
import com.ruseps.world.content.bossevents.impl.InheritedEvent;
import com.ruseps.world.content.bossevents.impl.OwnerBossEvent;
//import com.ruseps.world.content.bossevents.impl.SantaEvent;
import com.ruseps.net.packet.impl.CommandPacketListener;



/**
 * The event manager.
 *
 * @author Gabriel || Wolfsdarker
 */
public class GameEventManager {
	

    /**
     * Logger for the game event manager.
     */
    private static final Logger log = Logger.getLogger(GameEventManager.class.getName());

    /**
     * The location for the events.
     */
    private static final String eventsLocation = GameEventManager.class.getPackage().getName() + ".impl";

    /**
     * The current events.
     */
    private static final HashMap<String, GameEvent> events = new HashMap<>(0);

    /**
     * The delay to start the events.
     */
    private static final long startDelay = TimeUnit.SECONDS.toMillis(1);

    /**
     * The instant the events got loaded
     */
    private static long loadInstant;

    /**
     * Return the game events.
     *
     * @return the events
     */
    public static HashMap<String, GameEvent> getEvents() {
        return events;
    }
    
    public static void startDemonEvent() {
        GameEvent demonEvent = events.get("Lava Demon");
        if (!demonEvent.isActive() && demonEvent.canStart() && demonEvent.start()) {
            demonEvent.setActive(true);
            demonEvent.setLastEventInstant(System.currentTimeMillis());
        }
    }

    /**
     * Loads all the events.
     */
    public static void loadEvents() {
        loadInstant = System.currentTimeMillis();
        try {
            ClassPath cp = ClassPath.from(GameEvent.class.getClassLoader());
            for (ClassPath.ClassInfo ci : cp.getTopLevelClasses(eventsLocation)) {
                Class<?> c = Class.forName(ci.getName());
                if (c != null && c.getSuperclass() == GameEvent.class) {
                    GameEvent event = (GameEvent) c.getDeclaredConstructor().newInstance();
                    events.put(event.name(), event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Explicitly register the DemonEvent
        events.put("Lava Demon", new DemonEvent());
        
        // Explicitly register the BlessedSpartanEvent
        events.put("Blessed Spartan", new BlessedSpartanEvent());
        
        // Explicitly register the DragonEvent
        events.put("Dragon", new DragonEvent());
        
        // Explicitly register the OwnerBossEvent
        events.put("Owner Boss", new OwnerBossEvent());
        
     // Explicitly register the OwnerBossEvent
     //   events.put("Santa", new SantaEvent());
        
        // Explicitly register the TelosEvent
        events.put("Telos", new TelosEvent());
        
        // Explicitly register the VaderEvent
        events.put("Vader", new VaderEvent());
        
     // Explicitly register the VaderEvent
        events.put("Inherited", new InheritedEvent());

        // Explicitly register the ReaperEvent
        events.put("NexArch's Shooter", new ReaperEvent());

        log.info("Loaded " + events.size() + " game events.");
        log.info("Starting all game events in " + startDelay + " seconds.");
        TaskManager.submit(eventTask);
    }

    /**
     * The task to process all events.
     */
    private static final Task eventTask = new Task(1) {
        @Override
        protected void execute() {

            if (System.currentTimeMillis() - loadInstant < startDelay) {
                return;
            }

            if (events.isEmpty()) {
                return;
            }

            for (GameEvent event : events.values()) {
                if (event.isActive()) {
                    if (event.isOver()) {
                        event.end();
                        event.setActive(false);
                        continue;
                    }
                    event.process();
                    event.eventMessages(event.getEventDuration() + event.getLastEventInstant() - System.currentTimeMillis());
                } else {
                    if (System.currentTimeMillis() - event.getLastEventInstant() > event.getDelayBetweenEvents()) {
                        if (event.canStart() && event.start()) {
                            event.setActive(true);
                            event.setLastEventInstant(System.currentTimeMillis());
                        }
                    } else {
                        event.preEventMessages(event.getDelayBetweenEvents() + event.getLastEventInstant() - System.currentTimeMillis());
                    }

                }
            }
        }
    };

    public static void loadEvent(String eventName) {
        loadInstant = System.currentTimeMillis();
        try {
            ClassPath cp = ClassPath.from(GameEvent.class.getClassLoader());
            for (ClassPath.ClassInfo ci : cp.getTopLevelClasses(eventsLocation)) {
                World.sendMessage(ci.getName());
                Class c = Class.forName(ci.getName());
                if (c != null && c.getSuperclass() == GameEvent.class) {
                    GameEvent event = (GameEvent) Class.forName(ci.getName()).newInstance();
                    World.sendMessage(event.getName());
                    if (event.name().equalsIgnoreCase("DragonEvent")) {
                        events.put(event.name(), event);   
                        log.info("Loaded event: " + eventName);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        switch(eventName.toLowerCase()){
        
        case "lava":
        	 events.put("Lava Demon", new DemonEvent());
        	 break;
        case "blessed":
             // Explicitly register the BlessedSpartanEvent
             events.put("Blessed Spartan", new BlessedSpartanEvent());
             break;
        case "dragon":
             // Explicitly register the DragonEvent
             events.put("Dragon", new DragonEvent());
             break;
             
        case "owner":
             
             // Explicitly register the OwnerBossEvent
             events.put("Owner Boss", new OwnerBossEvent());
             break;
        case "santa":
             
           //  events.put("Santa", new SantaEvent());
             break;
        case "telos":
             // Explicitly register the TelosEvent
             events.put("Telos", new TelosEvent());
             break;
             
        case "vader":
             // Explicitly register the VaderEvent
             events.put("Vader", new VaderEvent());
             break;
             
          // Explicitly register the VaderEvent
        case "inherited":
             events.put("Inherited", new InheritedEvent());
             break;

        case "shooter":
             // Explicitly register the ReaperEvent
             events.put("NexArch's Shooter", new ReaperEvent());
             break;
 
        }
        	
        	
        
       
        log.info("Starting all game events in " + startDelay + " seconds.");
        TaskManager.submit(eventTask);
    }
   
}