package com.bruno.magicmap;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by bruno on 20/02/15.
 */
public class WelcomeScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.welcome_screen_layout );
    }
}
