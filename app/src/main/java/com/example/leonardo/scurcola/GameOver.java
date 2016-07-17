package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private boolean goodEnd;
    private TextView save;
    private String village;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodEnd = getIntent().getExtras().getBoolean("GOODEND");
        finish = (Button) findViewById(R.id.finish);
        if(goodEnd) {
            setContentView(R.layout.activity_game_over_villagers);
            save = (TextView) findViewById(R.id.textView4);
            village = getIntent().getExtras().getString("VILLAGE");
            save.setText(String.format(getResources().getString(R.string.villagersWin), village));
        }else{
            setContentView(R.layout.activity_game_over_wolves);
        }
    }

    public void end(View v){
        Intent intent = new Intent(this, Dispatcher.class);
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }
}
