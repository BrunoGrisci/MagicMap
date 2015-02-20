package com.bruno.magicmap;

import android.app.TabActivity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


//public class MainMap extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class MainMap extends TabActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Removes the action bar from the app
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_map);

        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator(getResources().getString(R.string.tabWelcomeScreen)).setContent(new Intent(this, WelcomeScreenActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator(getResources().getString(R.string.tabMyLocationList)).setContent(new Intent(this, MyLocationListActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("third").setIndicator(getResources().getString(R.string.tabRemindersList)).setContent(new Intent(this, RemindersListActivity.class)));
        mTabHost.setCurrentTab(0);

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
}
