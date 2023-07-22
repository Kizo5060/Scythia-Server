package com.ruseps.world.content.minigames;

import java.util.ArrayList;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.world.content.raids.GodsRaid;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class MinigameHandler2 {

    private ArrayList<Minigame2> minigames = new ArrayList<Minigame2>();


    public MinigameHandler2(Minigame2... minigames) {
        for (Minigame2 minigame : minigames) {
            this.minigames.add(minigame);
        }
    }

    public void addMinigame(Minigame2 minigame) {
        if (!minigames.contains(minigame))
            this.minigames.add(minigame);
    }
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
        for (Minigame2 minigame : minigames) {
            minigame.handleLogout(p);
        }
    }

    public void handlePlayerDeath(Player p) {
        if (p == null)
            return;
        for (Minigame2 minigame : minigames) {
            minigame.handlePlayerDeath(p);
        }
    }

    public boolean isInGame(Player p) {
        for (Minigame2 minigame : minigames)
            if (minigame.isInGame(p))
                return true;
        return false;
    }

    public boolean handleNpcDeath(NPC npc) {
        if (npc == null)
            return false;
        for (Minigame2 minigame : minigames) {

            if (minigame.handleNpcDeath(npc))
                return true;
        }

        return false;
    }

    public final static MinigameHandler2 defaultHandler() {
        return new MinigameHandler2(
                new GodsRaid(3000)
        );
    }

    public Minigame2 getMinigame(int index) {
        return this.minigames.get(index);
    }

}
