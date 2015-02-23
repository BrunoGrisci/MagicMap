package com.bruno.magicmap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class ReminderCreator extends ActionBarActivity {

    private EditText nameReminderEdit;
    private EditText messageReminderEdit;
    private EditText circularRadiusReminderEdit;
    private EditText delayTimeReminderEdit;
    private Spinner locationReminderEdit;
    private Button saveRemiderButton;
    private ArrayAdapter<MyLocation> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_creator);

        context = this;

        nameReminderEdit = (EditText) findViewById(R.id.nameReminderEdit);
        messageReminderEdit = (EditText) findViewById(R.id.messageReminderEdit);
        circularRadiusReminderEdit = (EditText) findViewById(R.id.circularRadiusReminderEdit);
        delayTimeReminderEdit = (EditText) findViewById(R.id.delayTimeReminderEdit);

        locationReminderEdit = (Spinner) findViewById(R.id.locationReminderEdit);
        adapter = new ArrayAdapter<MyLocation>(this, android.R.layout.simple_list_item_1, MyLocationListActivity.arrayMyLocations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationReminderEdit.setAdapter(adapter);
        locationReminderEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        saveRemiderButton = (Button) findViewById(R.id.saveRemiderButton);
        saveRemiderButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {

                Boolean completedName = !nameReminderEdit.getText().toString().isEmpty();
                Boolean completedMessage = !messageReminderEdit.getText().toString().isEmpty();
                Boolean completedLocation = !MyLocationListActivity.arrayMyLocations.isEmpty();
                Boolean completedRadius = !circularRadiusReminderEdit.getText().toString().isEmpty();
                Boolean completedDelayTime = !delayTimeReminderEdit.getText().toString().isEmpty();

                if (completedName && completedMessage && completedLocation && completedRadius && completedDelayTime) {
                    int indexMyLocation = locationReminderEdit.getSelectedItemPosition();
                    String name = nameReminderEdit.getText().toString();
                    String message = messageReminderEdit.getText().toString();
                    float circularRadius = Float.parseFloat(circularRadiusReminderEdit.getText().toString());
                    int delayTime = Integer.parseInt(delayTimeReminderEdit.getText().toString());

                    Intent intent = new Intent(context, RemindersListActivity.class);
                    intent.putExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_LOCATION_INDEX), indexMyLocation);
                    intent.putExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_NAME), name);
                    intent.putExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_MESSAGE), message);
                    intent.putExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_RADIUS), circularRadius);
                    intent.putExtra(getResources().getString(R.string.EXTRA_MESSAGE_REMINDER_DELAY_TIME), delayTime);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "First complete all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (MyLocationListActivity.arrayMyLocations.isEmpty()) {
            Toast.makeText(getApplicationContext(), "First create a location.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MyLocationListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder_creator, menu);
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
}

