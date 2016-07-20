package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerSelection extends AppCompatActivity {

    static final String VILLAGE = "VILLAGE";
    static final String PROGRESS = "PROGRESS";

    private SeekBar seekBar;
    private int seekBarProgress;
    private int playersAmount;
    private int wolves;
    private TextView playersDisplay;
    private String village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        // Probably initialize members with default values for a new instance
        initializeVariables();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            village = bundle.getString(VILLAGE);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                seekBarProgress = progressValue + 9; //Lowest amount possible is 9
                playersDisplay.setText(String.format("%s", seekBarProgress)); //Display the amount

               if(seekBarProgress > 15){ //If there are 16 or more players you got 3 wolves
                    wolves = 3;
                }
                playersAmount = seekBarProgress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        village = prefs.getString(VILLAGE, village);
        int progress = prefs.getInt(PROGRESS, 0);
        seekBar.setProgress(progress);
        }

    public void toNameSelection(View v){
        System.out.println(playersAmount);
        Intent intent = new Intent(this, NameSelection.class);
        intent.putExtra("PLAYERS_AMOUNT", playersAmount);
        intent.putExtra("WOLVES", wolves);
        intent.putExtra("VILLAGE", village);
        startActivity(intent);
    }

    public void initializeVariables(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playersDisplay = (TextView) findViewById(R.id.playersAmount);
        playersAmount = 9;
        wolves = 2;
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(VILLAGE, village);
        editor.putInt(PROGRESS, seekBarProgress - 9);
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }
}
