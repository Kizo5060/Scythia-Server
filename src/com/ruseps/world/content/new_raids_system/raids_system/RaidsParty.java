package com.ruseps.world.content.new_raids_system.raids_system;

import com.ruseps.util.Stopwatch;
import com.ruseps.world.entity.impl.player.Player;

import java.util.ArrayList;

public class RaidsParty {

    private ArrayList<Player> partyMembers;

    private Player partyLeader;

    private RaidsPartyStatus status;

    private Stopwatch joiningTimer;

    private int floor;

    public RaidsParty(Player partyLeader) {
        this.partyLeader = partyLeader;
        this.partyMembers = new ArrayList<>(5);
        this.partyMembers.add(partyLeader);
        this.status = RaidsPartyStatus.COLLECTING_MEMBERS;
        this.joiningTimer = new Stopwatch();
        this.floor = partyLeader.getIndex() * 4;
    }

    public Player getPartyLeader() {
        if (partyMembers.size() == 0) {
            return null;
        }
        return partyMembers.get(0);
    }

    public void addPlayer(Player toAdd) {
        partyMembers.add(toAdd);
    }

    public void setPartyLeader(Player partyLeader) {
        this.partyLeader = partyLeader;
    }

    public ArrayList<Player> getPartyMembers() {
        return partyMembers;
    }

    public RaidsPartyStatus getStatus() {
        return status;
    }

    public void setStatus(RaidsPartyStatus status) {
        this.status = status;
    }

    public boolean isLeader(Player player) {
        return player.getName().equalsIgnoreCase(partyLeader.getName());
    }

    public Stopwatch getJoiningTimer() {
        return joiningTimer;
    }

    public int getFloor() {
        return floor;
    }

}
