package com.bruno.magicmap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocationListActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.bruno.magicmap.MESSAGE";

    private ListView listMyLocations;
    private ArrayList<MyLocation> arrayMyLocations = new ArrayList<MyLocation>();

    private Button newLocationButton;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation_list_layout);

        listMyLocations = (ListView) findViewById(R.id.listMyLocations);
        newLocationButton = (Button) findViewById(R.id.newLocationButton);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        final ArrayAdapter<MyLocation> adapter = new ArrayAdapter<MyLocation>(this, android.R.layout.simple_list_item_1, arrayMyLocations);
        listMyLocations.setAdapter(adapter);

        /*newLocationButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {

                if (!locationLatitudeInput.getText().toString().isEmpty() && !locationLongitudeInput.getText().toString().isEmpty() && !locationLatitudeInput.getText().toString().isEmpty()) {
                    double lat = Double.valueOf(locationLatitudeInput.getText().toString());
                    double lon = Double.valueOf(locationLongitudeInput.getText().toString());
                    String name = locationNameInput.getText().toString();
                    arrayMyLocations.add(new MyLocation(lat, lon, name));

                }
                adapter.notifyDataSetChanged();

            }
        });*/

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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, LocationSelector.class);
        intent.putExtra(EXTRA_MESSAGE, "nothing");
        startActivity(intent);
    }

}