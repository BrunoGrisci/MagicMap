package com.bruno.magicmap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bruno.magicmap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocationListActivity extends Activity {

    static final int PICK_LOCATION_REQUEST = 1;

    private ListView listMyLocations;
    public static ArrayList<MyLocation> arrayMyLocations = new ArrayList<MyLocation>();
    private ArrayAdapter<MyLocation> adapter;
    public static String jsonMyLocationsList;
    Gson gson = new Gson();

    private Button newLocationButton;

    private GoogleMap map;
    private CameraUpdate originCam;
    private CameraUpdate originZoom;
    private Location originLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation_list_layout);

        listMyLocations = (ListView) findViewById(R.id.listMyLocations);
        newLocationButton = (Button) findViewById(R.id.newLocationButton);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
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

        adapter = new ArrayAdapter<MyLocation>(this, android.R.layout.simple_list_item_1, arrayMyLocations);
        listMyLocations.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listMyLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocationList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLocationList();

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, LocationSelector.class);
        startActivityForResult(intent, PICK_LOCATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Double latitude = data.getDoubleExtra(getResources().getString(R.string.EXTRA_MESSAGE_LATITUDE), 0.0);
                Double longitude = data.getDoubleExtra(getResources().getString(R.string.EXTRA_MESSAGE_LONGITUDE), 0.0);
                String name = data.getStringExtra(getResources().getString(R.string.EXTRA_MESSAGE_LOCATION_NAME));
                String address = data.getStringExtra(getResources().getString(R.string.EXTRA_MESSAGE_ADDRESS));
                Double camLat = data.getDoubleExtra(getResources().getString(R.string.EXTRA_MESSAGE_CAMERA_LATITUDE), 0.0);
                Double camLon = data.getDoubleExtra(getResources().getString(R.string.EXTRA_MESSAGE_CAMERA_LONGITUDE), 0.0);
                arrayMyLocations.add(new MyLocation(latitude, longitude, name, address));
                adapter.notifyDataSetChanged();

                saveLocationList();
                updateMapMarks();

            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("CANCELED");
            }
        }
    }

    protected void updateMapMarks() {
        if(!arrayMyLocations.isEmpty()) {
            map.clear();
            for (MyLocation loc : arrayMyLocations) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                        .title(loc.getName())
                        .snippet(loc.getAddress()));
            }
            originCam = CameraUpdateFactory.newLatLng(new LatLng(arrayMyLocations.get(0).getLatitude(), arrayMyLocations.get(0).getLongitude()));
            map.moveCamera(originCam);
        }
    }

    protected void saveLocationList() {
        if (!arrayMyLocations.isEmpty()) {
            jsonMyLocationsList = gson.toJson(arrayMyLocations);
            Log.d("TAG", "jsonMyLocationList = " + jsonMyLocationsList);

            SharedPreferences savedLocationList = getSharedPreferences(getResources().getString(R.string.SAVED_LOCATION_LIST), MODE_PRIVATE);
            SharedPreferences.Editor editorList = savedLocationList.edit();
            editorList.putString(getResources().getString(R.string.SAVED_LOCATION_LIST), jsonMyLocationsList);
            editorList.commit();
        }
    }

    protected void loadLocationList() {
        SharedPreferences savedLocationList = getSharedPreferences(getResources().getString(R.string.SAVED_LOCATION_LIST), MODE_PRIVATE);
        jsonMyLocationsList = savedLocationList.getString(getResources().getString(R.string.SAVED_LOCATION_LIST), null);
        if (jsonMyLocationsList != null) {
            Type type = new TypeToken<List<MyLocation>>() {}.getType();
            List<MyLocation> locList = gson.fromJson(jsonMyLocationsList, type);
            arrayMyLocations.clear();
            arrayMyLocations.addAll(locList);
            adapter.notifyDataSetChanged();
            System.out.println(locList.toString());
            updateMapMarks();
        }
    }

}