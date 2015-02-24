package com.bruno.magicmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


//public class MainMap extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class MainMap extends TabActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final double MIN_ACCURACY = 950.0;
    GoogleApiClient mGoogleApiClient;
    Location userLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    Gson gson = new Gson();

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

                    SharedPreferences savedUserLocation = getSharedPreferences(getResources().getString(R.string.SAVED_USER_LOCATION), MODE_PRIVATE);
                    SharedPreferences.Editor editorList = savedUserLocation.edit();
                    editorList.putLong(getResources().getString(R.string.SAVED_USER_LATITUDE), Double.doubleToRawLongBits(userLocation.getLatitude()));
                    editorList.putLong(getResources().getString(R.string.SAVED_USER_LONGITUDE), Double.doubleToRawLongBits(userLocation.getLongitude()));
                    editorList.putLong(getResources().getString(R.string.SAVED_USER_ACCURACY), Double.doubleToRawLongBits(userLocation.getAccuracy()));
                    editorList.commit();

                    loadLocationList();
                    loadReminderList();
                    if (!RemindersListActivity.arrayMyReminders.isEmpty()) {
                        float distance;
                        Location target = new Location(userLocation);
                        for (Reminder rem : RemindersListActivity.arrayMyReminders) {
                            target.setLatitude(rem.getLocation().getLatitude());
                            target.setLongitude(rem.getLocation().getLongitude());
                            target.setAltitude(0.0);
                            target.setAccuracy(0);
                            distance = userLocation.distanceTo(target);

                            if (distance <= rem.getCircularRadius()) {

                                //rem.setNotify(true);
                                System.out.println(rem.getName());
                                System.out.println(rem.getNotify());

                                if (rem.getNotify()) {

                                    rem.setNotify(false);

                                    System.out.println(rem.getName());
                                    System.out.println(rem.getNotify());

                                    int index = RemindersListActivity.arrayMyReminders.indexOf(rem);

                                    Notification.Builder mBuilder =
                                            new Notification.Builder(MainMap.this)
                                                    .setSmallIcon(R.drawable.icon2)
                                                    .setContentTitle(rem.getName())
                                                    .setContentText(rem.getMessage())
                                                    .setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                                    .setDefaults(Notification.DEFAULT_SOUND);

                                    Intent resultIntent = new Intent(MainMap.this, NotificationResult.class);
                                    resultIntent.putExtra("userLatitude", userLocation.getLatitude());
                                    resultIntent.putExtra("userLongitude", userLocation.getLongitude());
                                    resultIntent.putExtra("targetName", rem.getLocation().getName());
                                    resultIntent.putExtra("targetAddress", rem.getLocation().getAddress());
                                    resultIntent.putExtra("notificationName", rem.getName());
                                    resultIntent.putExtra("notificationMessage", rem.getMessage());
                                    resultIntent.putExtra("targetLatitude", rem.getLocation().getLatitude());
                                    resultIntent.putExtra("targetLongitude", rem.getLocation().getLongitude());
                                    resultIntent.putExtra("targetRadius", rem.getCircularRadius());
                                    resultIntent.putExtra("distance", distance);

                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainMap.this);
                                    stackBuilder.addParentStack(NotificationResult.class);
                                    stackBuilder.addNextIntent(resultIntent);
                                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(index, PendingIntent.FLAG_UPDATE_CURRENT);
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    mNotificationManager.notify(index, mBuilder.build());

                                }
                            }
                            else {
                                rem.setNotify(true);
                            }

                            saveReminderList();
                            System.out.println(rem.getName());
                            System.out.println(rem.getNotify());

                        }
                    }

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

    protected void loadReminderList() {
        SharedPreferences savedReminderList = getSharedPreferences(getResources().getString(R.string.SAVED_REMINDER_LIST), MODE_PRIVATE);
        RemindersListActivity.jsonRemindersList = savedReminderList.getString(getResources().getString(R.string.SAVED_REMINDER_LIST), null);
        if (RemindersListActivity.jsonRemindersList != null) {
            Type type = new TypeToken<List<Reminder>>(){}.getType();
            List<Reminder> remList = gson.fromJson(RemindersListActivity.jsonRemindersList, type);
            RemindersListActivity.arrayMyReminders.clear();
            RemindersListActivity.arrayMyReminders.addAll(remList);
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

    protected void saveReminderList() {
        String jsonRemindersList;
        jsonRemindersList = gson.toJson(RemindersListActivity.arrayMyReminders);

        SharedPreferences savedReminderList = getSharedPreferences(getResources().getString(R.string.SAVED_REMINDER_LIST), MODE_PRIVATE);
        SharedPreferences.Editor editorList = savedReminderList.edit();
        editorList.putString(getResources().getString(R.string.SAVED_REMINDER_LIST), jsonRemindersList);
        editorList.commit();
    }
}
