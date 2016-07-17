package com.example.leonardo.scurcola;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    static final String PREFS_NAME = "PREFS_NAME";
    static final String ACTIVITY = "ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
        public void toPlayerSelection(View v){
        Intent intent = new Intent(this, Village.class);
        startActivity(intent);
    }

}
