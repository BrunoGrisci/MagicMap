package com.bruno.magicmap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bruno.magicmap.R;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 20/02/15.
 */
public class RemindersListActivity extends Activity {

    static final int PICK_REMINDER_REQUEST = 2;

    private ListView listMyReminders;
    private ArrayList<Reminder> arrayMyReminders = new ArrayList<Reminder>();
    private ArrayAdapter<Reminder> adapter;
    public static String jsonRemindersList;
    Gson gson = new Gson();

    private Button newReminderButton;

    private GoogleMap map;
    private CameraUpdate originCam;
    private CameraUpdate originZoom;
    private Location originLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.reminders_list_layout );

        loadLocationList();

        listMyReminders = (ListView) findViewById(R.id.listMyReminders);
        newReminderButton = (Button) findViewById(R.id.newReminderButton);

        adapter = new ArrayAdapter<Reminder>(this, android.R.layout.simple_list_item_1, arrayMyReminders);
        listMyReminders.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listMyReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

            }
        });

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapReminder)).getMap();
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocationList();
        loadReminderList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveReminderList();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, ReminderCreator.class);
        startActivityForResult(intent, PICK_REMINDER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_REMINDER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int indexLocation = data.getIntExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_LOCATION_INDEX), -1);
                if (indexLocation >= 0) {
                    String name = data.getStringExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_NAME));
                    String message = data.getStringExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_MESSAGE));
                    float circularRadius = data.getFloatExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_RADIUS), 0);
                    int delayTime = data.getIntExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_DELAY_TIME), 0);
                    arrayMyReminders.add(new Reminder(MyLocationListActivity.arrayMyLocations.get(indexLocation), name, message, circularRadius, delayTime));
                    adapter.notifyDataSetChanged();

                    saveReminderList();
                    updateMapMarks();

                }
            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("CANCELED");
            }
        }
    }

    protected void saveReminderList() {
        if (!arrayMyReminders.isEmpty()) {
            jsonRemindersList = gson.toJson(arrayMyReminders);
            Log.d("TAG", "jsonRemindersList = " + jsonRemindersList);

            SharedPreferences savedReminderList = getSharedPreferences(getResources().getString(R.string.SAVED_REMINDER_LIST), MODE_PRIVATE);
            SharedPreferences.Editor editorList = savedReminderList.edit();
            editorList.putString(getResources().getString(R.string.SAVED_REMINDER_LIST), jsonRemindersList);
            editorList.commit();
        }
    }

    protected void loadReminderList() {
        SharedPreferences savedReminderList = getSharedPreferences(getResources().getString(R.string.SAVED_REMINDER_LIST), MODE_PRIVATE);
        jsonRemindersList = savedReminderList.getString(getResources().getString(R.string.SAVED_REMINDER_LIST), null);
        if (jsonRemindersList != null) {
            Type type = new TypeToken<List<Reminder>>(){}.getType();
            List<Reminder> remList = gson.fromJson(jsonRemindersList, type);
            arrayMyReminders.clear();
            arrayMyReminders.addAll(remList);
            adapter.notifyDataSetChanged();
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
            updateMapMarks();
        }
    }

    protected void updateMapMarks() {
        if(!arrayMyReminders.isEmpty()) {
            map.clear();
            for (Reminder rem : arrayMyReminders) {
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
            originCam = CameraUpdateFactory.newLatLng(new LatLng(arrayMyReminders.get(0).getLocation().getLatitude(), arrayMyReminders.get(0).getLocation().getLongitude()));
            map.moveCamera(originCam);
        }
    }
}
