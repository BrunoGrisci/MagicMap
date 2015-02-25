package com.bruno.magicmap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by bruno on 20/02/15.
 */
public class WelcomeScreenActivity extends Activity {

    Gson gson = new Gson();
    private GoogleMap map;
    private CameraUpdate originCam;
    private CameraUpdate originZoom;
    private Location originLocation;

    private TextView userLocation;

    private double userLatitude;
    private double userLongitude;
    private double userAccuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.welcome_screen_layout );

        userLocation = (TextView) findViewById(R.id.textCurrentLocation);
        loadUserLocation();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapWelcome)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);

        originZoom = CameraUpdateFactory.zoomTo(5);
        map.animateCamera(originZoom);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        loadLocationList();
        loadReminderList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocationList();
        loadReminderList();
        loadUserLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void loadUserLocation() {
        SharedPreferences savedUserLocation = getSharedPreferences(getResources().getString(R.string.SAVED_USER_LOCATION), MODE_PRIVATE);
        userLatitude = Double.longBitsToDouble(savedUserLocation.getLong(getResources().getString(R.string.SAVED_USER_LATITUDE), 0));
        userLongitude = Double.longBitsToDouble(savedUserLocation.getLong(getResources().getString(R.string.SAVED_USER_LONGITUDE), 0));
        userAccuracy = Double.longBitsToDouble(savedUserLocation.getLong(getResources().getString(R.string.SAVED_USER_ACCURACY), 0));

        String latText = String.valueOf(userLatitude);
        String.format(latText, "%.2f");
        String lonText = String.valueOf(userLongitude);
        String.format(lonText, "%.2f");
        userLocation.setText(latText + ", " + lonText);
    }

    protected void loadReminderList() {
        SharedPreferences savedReminderList = getSharedPreferences(getResources().getString(R.string.SAVED_REMINDER_LIST), MODE_PRIVATE);
        RemindersListActivity.jsonRemindersList = savedReminderList.getString(getResources().getString(R.string.SAVED_REMINDER_LIST), null);
        if (RemindersListActivity.jsonRemindersList != null) {
            Type type = new TypeToken<List<Reminder>>(){}.getType();
            List<Reminder> remList = gson.fromJson(RemindersListActivity.jsonRemindersList, type);
            RemindersListActivity.arrayMyReminders.clear();
            RemindersListActivity.arrayMyReminders.addAll(remList);
            updateMapMarks();
        }
    }

    protected void loadLocationList() {
        SharedPreferences savedLocationList = getSharedPreferences(getResources().getString(R.string.SAVED_LOCATION_LIST), MODE_PRIVATE);
        MyLocationListActivity.jsonMyLocationsList = savedLocationList.getString(getResources().getString(R.string.SAVED_LOCATION_LIST), null);
        if (MyLocationListActivity.jsonMyLocationsList != null) {
            Type type = new TypeToken<List<MyLocation>>(){}.getType();
            List<MyLocation> locList = gson.fromJson(MyLocationListActivity.jsonMyLocationsList, type);
            MyLocationListActivity.arrayMyLocations.clear();
            MyLocationListActivity.arrayMyLocations.addAll(locList);
        }
    }

    protected void updateMapMarks() {
        map.clear();
        if(!RemindersListActivity.arrayMyReminders.isEmpty()) {
            for (Reminder rem : RemindersListActivity.arrayMyReminders) {
                LatLng position = new LatLng(rem.getLocation().getLatitude(), rem.getLocation().getLongitude());
                map.addMarker(new MarkerOptions()
                        .position(position)
                        .title(rem.getName() + " in " + rem.getLocation().getName())
                        .snippet(rem.getMessage()));
                CircleOptions circle = new CircleOptions();
                circle.center(position);
                circle.radius(rem.getCircularRadius());
                map.addCircle(circle);
            }
            originCam = CameraUpdateFactory.newLatLng(new LatLng(RemindersListActivity.arrayMyReminders.get(0).getLocation().getLatitude(), RemindersListActivity.arrayMyReminders.get(0).getLocation().getLongitude()));
            map.moveCamera(originCam);
        }
    }
}