package com.bruno.magicmap;


import com.google.android.gms.location.Geofence;

/**
 * Created by bruno on 20/02/15.
 */
public class Reminder {

    private MyLocation location;
    private String name;
    private String message;
    private float circularRadius;
    private int delayTime;
    private boolean notify;
    private long ID;

    private Geofence.Builder fencer;
    //private Geofence geofence;

    public Reminder (MyLocation startLocation, String startName, String startMessage, float startCircularRadius, int startDelayTime) {
        location = new MyLocation(startLocation);
        name = startName;
        message = startMessage;
        circularRadius = startCircularRadius;
        delayTime = startDelayTime;
        setGeofence();
        notify = true;
        ID = System.nanoTime();
    }

    public Reminder (MyLocation startLocation, String startName, String startMessage, float startCircularRadius) {
        location = new MyLocation(startLocation);
        name = startName;
        message = startMessage;
        circularRadius = startCircularRadius;
        delayTime = 0;
        setGeofence();
        notify = true;
        ID = System.nanoTime();
    }

    public void setLocation(MyLocation l) {
        location.set(l);
        setGeofence();
    }

    public void setName(String n) {
        name = n;
        setGeofence();
    }

    public void setMessage(String m) {
        message = m;
    }

    public void setCircularRadius(float cr) {
        circularRadius = cr;
        setGeofence();
    }

    public void setDelayTime (int dt) {
        delayTime = dt;
        setGeofence();
    }

    public void setNotify(boolean n) {
        notify = n;
    }

    private void setGeofence() {
        fencer = new Geofence.Builder();
        fencer.setCircularRegion(location.getLatitude(), location.getLongitude(), circularRadius);
        fencer.setRequestId(name);
        fencer.setLoiteringDelay(delayTime);

        fencer.setExpirationDuration(Geofence.NEVER_EXPIRE);
        fencer.setNotificationResponsiveness(0);
        fencer.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
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

    public float getCircularRadius() {
        return circularRadius;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public Geofence.Builder getGeofenceBuilder() {
        return fencer;
    }

    public boolean getNotify() {
        return notify;
    }

    public long getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name + " in " + location.getName() + "\n" + message;
    }
}
