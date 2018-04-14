package com.devband.tronwalletforandroid.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class CommonActivity extends AppCompatActivity {

    protected void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
