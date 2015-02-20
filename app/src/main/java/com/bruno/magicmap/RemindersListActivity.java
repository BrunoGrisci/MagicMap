package com.bruno.magicmap;

import android.app.Activity;
import android.os.Bundle;

import com.bruno.magicmap.R;

/**
 * Created by bruno on 20/02/15.
 */
public class RemindersListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.reminders_list_layout );
    }
}
