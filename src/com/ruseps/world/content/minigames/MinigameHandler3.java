package com.ruseps.world.content.minigames;

import java.util.ArrayList;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.world.content.raids.MarvelsRaid;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class MinigameHandler3 {

    private ArrayList<Minigame3> minigames = new ArrayList<Minigame3>();


    public MinigameHandler3(Minigame3... minigames) {
        for (Minigame3 minigame : minigames) {
            this.minigames.add(minigame);
        }
    }

    public void addMinigame(Minigame3 minigame) {
        if (!minigames.contains(minigame))
            this.minigames.add(minigame);
    }
    @SuppressWarnings("unlikely-arg-type")
	public void removeMinigame(Minigame minigame) { this.minigames.remove(minigame); }

    public void loadMinigames() {
        TaskManager.submit(new Task() {
            @Override
            public void execute() {
                for (int i = 0; i < minigames.size(); ++i) {
                    TaskManager.submit(minigames.get(i));
                }
                stop();
            }
        });
    }

    public void handleLogout(Player p) {
        if (p == null)
            return;
        for (Minigame3 minigame : minigames) {
            minigame.handleLogout(p);
        }
    }

    public void handlePlayerDeath(Player p) {
        if (p == null)
            return;
        for (Minigame3 minigame : minigames) {
            minigame.handlePlayerDeath(p);
        }
    }

    public boolean isInGame(Player p) {
        for (Minigame3 minigame : minigames)
            if (minigame.isInGame(p))
                return true;
        return false;
    }

    public boolean handleNpcDeath(NPC npc) {
        if (npc == null)
            return false;
        for (Minigame3 minigame : minigames) {

            if (minigame.handleNpcDeath(npc))
                return true;
        }

        return false;
    }

    public final static MinigameHandler3 defaultHandler() {
        return new MinigameHandler3(
                new MarvelsRaid(3000)
        );
    }

    public Minigame3 getMinigame(int index) {
        return this.minigames.get(index);
    }

}
