package com.bruno.magicmap;

import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


public class NotificationResult extends ActionBarActivity {

    private Gson gson = new Gson();
    private GoogleMap map;
    private CameraUpdate originCam;
    private CameraUpdate originZoom;
    private Location originLocation;

    private String notificationName;
    private String notificationMessage;
    private String targetAddress;
    private String targetName;
    private Double targetLatitude;
    private Double targetLongitude;
    private float targetRadius;
    private Double userLatitude;
    private Double userLongitude;
    private float distance;

    private TextView notNameText;
    private TextView notMessageText;
    private TextView notDistanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_result);

        notNameText = (TextView) findViewById(R.id.notNameText);
        notMessageText = (TextView) findViewById(R.id.notMessageText);
        notDistanceText = (TextView) findViewById(R.id.textDistance);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapNotification)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            notificationName = extras.getString(getResources().getString(R.string.NOTIFICATION_NAME));
            notificationMessage = extras.getString(getResources().getString(R.string.NOTIFICATION_MESSAGE));
            targetAddress = extras.getString(getResources().getString(R.string.NOTIFICATION_TARGET_ADDRESS));
            targetName = extras.getString(getResources().getString(R.string.NOTIFICATION_TARGET_NAME));
            targetLatitude = extras.getDouble(getResources().getString(R.string.NOTIFICATION_TARGET_LATITUDE));
            targetLongitude = extras.getDouble(getResources().getString(R.string.NOTIFICATION_TARGET_LONGITUDE));
            targetRadius = extras.getFloat(getResources().getString(R.string.NOTIFICATION_TARGET_RADIUS));
            userLatitude = extras.getDouble(getResources().getString(R.string.NOTIFICATION_USER_LATITUDE));
            userLongitude = extras.getDouble(getResources().getString(R.string.NOTIFICATION_USER_LONGITUDE));
            distance = extras.getFloat(getResources().getString(R.string.NOTIFICATION_DISTANCE));

            notNameText.setText(notificationName);
            notMessageText.setText(notificationMessage);
            notDistanceText.setText(String.valueOf(distance));

            updateMapMarks();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateMapMarks() {
        map.clear();
        LatLng position = new LatLng(targetLatitude, targetLongitude);
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(targetName)
                .snippet(targetAddress));

        CircleOptions circle = new CircleOptions();
        circle.center(position);
        circle.radius(targetRadius);
        map.addCircle(circle);

        System.out.println(targetRadius);

        LatLng userPosition = new LatLng(userLatitude, userLongitude);
        map.addMarker(new MarkerOptions()
                .position(userPosition)
                .title("My position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        originCam = CameraUpdateFactory.newLatLng(position);
        map.moveCamera(originCam);
        originZoom = CameraUpdateFactory.zoomTo(15);
        map.animateCamera(originZoom);
    }

}
