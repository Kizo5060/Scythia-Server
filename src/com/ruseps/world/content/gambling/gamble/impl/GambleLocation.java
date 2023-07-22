package com.ruseps.world.content.gambling.gamble.impl;

import com.ruseps.model.Locations;
import com.ruseps.model.Position;
//import javafx.geometry.Pos;

import java.util.ArrayList;

public class GambleLocation {
   public boolean inUse;
   public Position position;
   public String pos;


    public static ArrayList<GambleLocation> locations = new ArrayList<>();

    public GambleLocation(boolean inUse, Position position, String pos){
        this.inUse = inUse;
        this.position = position;
        this.pos = pos;
    }

    public static void init(){
        locations.add(new GambleLocation(false,new Position(2596,2696,0),"-"));
        locations.add(new GambleLocation(false,new Position(2596,2700,0),"-"));
        locations.add(new GambleLocation(false,new Position(2596,2704,0),"-"));
        locations.add(new GambleLocation(false,new Position(2596,2706,0),"-"));
        locations.add(new GambleLocation(false,new Position(2596,2708,0),"-"));

        locations.add(new GambleLocation(false,new Position(2608,2696,0),"+"));
        locations.add(new GambleLocation(false,new Position(2608,2700,0),"+"));
        locations.add(new GambleLocation(false,new Position(2608,2704,0),"+"));
        locations.add(new GambleLocation(false,new Position(2608,2706,0),"+"));
        locations.add(new GambleLocation(false,new Position(2608,2708,0),"+"));
    }

    public static ArrayList<GambleLocation> getLocations() {
        return locations;
    }

    public Position getPosition() {
        return position;
    }

    public String getPos() {
        return pos;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public static void setLocations(ArrayList<GambleLocation> locations) {
        GambleLocation.locations = locations;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
