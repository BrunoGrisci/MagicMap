package com.bruno.magicmap;

import android.location.Location;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocation {

    private double latitude;
    private double longitude;
    private String name;
    private String address;

    public MyLocation (double startLatitude, double startLongitude, String startName) {
        latitude = startLatitude;
        longitude = startLongitude;
        name = startName;
        address = "";
    }

    public MyLocation (double startLatitude, double startLongitude, String startName, String startAddress) {
        latitude = startLatitude;
        longitude = startLongitude;
        name = startName;
        address = startAddress;
    }

    public MyLocation (MyLocation startMyLocal) {
        latitude = startMyLocal.getLatitude();
        longitude = startMyLocal.getLongitude();
        name = startMyLocal.getName();
        address = startMyLocal.getAddress();
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setLongitude(double lon) {
        longitude = lon;
    }

    public void setLatitude(double lat) {
        longitude = lat;
    }

    public void setName(String n) {
        name = n;
    }

    public void setAddress(String a) {
        address = a;
    }

    public void set(MyLocation ml) {
        this.setLatitude(ml.getLatitude());
        this.setLongitude(ml.getLongitude());
        this.setName(ml.getName());
        this.setAddress(ml.getAddress());
    }

    @Override
    public String toString() {
        return name + "\n" + address + "\n" + "Latitude: " + String.valueOf(latitude) + " Longitude: " + String.valueOf(longitude);
    }
}
