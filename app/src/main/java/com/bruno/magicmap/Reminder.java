package com.bruno.magicmap;

import com.bruno.magicmap.MyLocation;

/**
 * Created by bruno on 20/02/15.
 */
public class Reminder {

    private MyLocation location;
    private String name;
    private String message;
    private double circularRadius;

    public Reminder (MyLocation startLocation, String startName, String startMessage, double startCircularRadius) {
        location = new MyLocation(startLocation);
        name = startName;
        message = startMessage;
        circularRadius = startCircularRadius;
    }

    public void setLocation(MyLocation l) {
        location.set(l);
    }

    public void setName(String n) {
        name = n;
    }

    public void setMessage(String m) {
        message = m;
    }

    public void setCircularRadius(double cr) {
        circularRadius = cr;
    }

    public MyLocation getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public double getCircularRadius() {
        return circularRadius;
    }
}
