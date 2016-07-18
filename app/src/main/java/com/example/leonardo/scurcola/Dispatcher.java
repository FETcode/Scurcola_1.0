package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Dispatcher extends Activity {
boolean debug = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;
if(!debug) {
    try {
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        activityClass = Class.forName(
                prefs.getString("lastActivity", MainActivity.class.getName()));
    } catch (ClassNotFoundException ex) {
        activityClass = MainActivity.class;
    }
}else {
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        activityClass = MainActivity.class;
}
        startActivity(new Intent(this, activityClass));
    }
}