package com.bruno.magicmap;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


//public class MainMap extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class MainMap extends TabActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final double MIN_ACCURACY = 950.0;
    GoogleApiClient mGoogleApiClient;
    Location userLocation;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator(getResources().getString(R.string.tabWelcomeScreen)).setContent(new Intent(this, WelcomeScreenActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator(getResources().getString(R.string.tabMyLocationList)).setContent(new Intent(this, MyLocationListActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("third").setIndicator(getResources().getString(R.string.tabRemindersList)).setContent(new Intent(this, RemindersListActivity.class)));
        mTabHost.setCurrentTab(0);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the location provider.
                if (userLocation == null || userLocation.getAccuracy() >= location.getAccuracy()) {
                    userLocation = location;
                    System.out.println(userLocation.getLatitude());
                    System.out.println(userLocation.getLongitude());
                    System.out.println(userLocation.getAccuracy());

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        //buildGoogleApiClient();
        //mGoogleApiClient.connect();
        userLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        //System.out.println(userLocation);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_map, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location lastLocation = new Location(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
        if (userLocation == null) {
            userLocation.set(lastLocation);
            System.out.println("CONNECTED");
            System.out.println(userLocation.getLatitude());
            System.out.println(userLocation.getLongitude());
            System.out.println(userLocation.getAccuracy());
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectedResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
