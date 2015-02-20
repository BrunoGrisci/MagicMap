package com.bruno.magicmap;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bruno.magicmap.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocationListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ListView listMyLocations;
    private ArrayList<String> arrayMyLocations = new ArrayList<String>();
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation_list_layout);

        listMyLocations = (ListView) findViewById(R.id.listMyLocations);

        buildGoogleApiClient();

        arrayMyLocations.add("test");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayMyLocations);
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
    public void onConnected(Bundle connectionHint) {
        mLastLocation = new Location(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
        if (mLastLocation != null) {
            System.out.println(String.valueOf(mLastLocation.getLatitude()));
            System.out.println(String.valueOf(mLastLocation.getLongitude()));

            MyLocation l1 = new MyLocation(mLastLocation, "here");

            System.out.println(l1.getName());
            System.out.println(l1.getLocation().getLongitude());

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectedResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

}