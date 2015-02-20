package com.bruno.magicmap;

import android.location.Location;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocation {

    private Location local;
    private String name;

    public MyLocation (Location startLocation, String startName) {
        local = new Location(startLocation);
        name = startName;
    }

    public MyLocation (MyLocation startMyLocal) {
        local = new Location(startMyLocal.getLocation());
        name = startMyLocal.getName();
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return local;
    }

    public void setLocation(Location l) {
        local.set(l);
    }

    public void setName(String n) {
        name = n;
    }

    public void set(MyLocation ml) {
        this.setLocation(ml.getLocation());
        this.setName(ml.getName());
    }
}
