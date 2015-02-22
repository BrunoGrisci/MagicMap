package com.bruno.magicmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bruno.magicmap.R;

import java.util.ArrayList;

/**
 * Created by bruno on 20/02/15.
 */
public class RemindersListActivity extends Activity {

    static final int PICK_REMINDER_REQUEST = 2;

    private ListView listMyReminders;
    private ArrayList<Reminder> arrayMyReminders = new ArrayList<Reminder>();
    private ArrayAdapter<Reminder> adapter;

    private Button newReminderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.reminders_list_layout );

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
        Intent intent = new Intent(this, ReminderCreator.class);
        startActivityForResult(intent, PICK_REMINDER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_REMINDER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("CANCELED");
            }
        }
    }
}
