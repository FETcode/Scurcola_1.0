package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardo.scurcola.characters.Clairvoyant;
import com.example.leonardo.scurcola.characters.Villager;
import com.example.leonardo.scurcola.characters.Wolf;
import com.example.leonardo.scurcola.master.Master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NameSelection extends AppCompatActivity {
    static final String PLAYERS_AMOUNT = "PLAYERS_AMOUNT";
    static final String PLAYERS_AMOUNT2 = "PLAYERS_AMOUNT2";
    static final String VILLAGE = "VILLAGE";

    private int playersAmount; // How many players are playing
    private int playersAmount2;
    private List<String> players; // Where we'll list players' names
    private List<Master> master;
    private EditText namesDisplay; // Where the user enter the names
    private TextView whoIsMaster; // Our question to the user
    private TextView playersLeft;

    private List<Card> characters; // Where the picked cards will be stored
        private List<Villager> villagerList;
        private List<Clairvoyant> clairvoyantList;
        private List<Wolf> wolfList;

    private Button next;
    private Button back;
    private Button finish;
    private String village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_selection);

        // Probably initialize members with default values for a new instance
        initializeVariables();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            village = bundle.getString(VILLAGE);
            playersAmount = bundle.getInt(PLAYERS_AMOUNT);
            playersAmount2 = playersAmount;
            playersLeft.setText(String.valueOf(playersAmount2));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        village = prefs.getString(VILLAGE, village);
        playersAmount = prefs.getInt(PLAYERS_AMOUNT, playersAmount);
        playersAmount2 = prefs.getInt(PLAYERS_AMOUNT2, playersAmount);
        if(playersAmount > playersAmount2) {
            whoIsMaster.setText(R.string.whoIsPlaying); // and edit our question properly.
            playersLeft.setText(String.valueOf(playersAmount2));
        }if (playersAmount2 == 0){
            next.setVisibility(View.GONE); // we want to hide the Next button,
            finish.setVisibility(View.VISIBLE); // see the Finish one,
            namesDisplay.setVisibility(View.INVISIBLE); // and disable the EditText.
            playersLeft.setVisibility(View.INVISIBLE);
        }
    }

    // ### NEXT, BACK and FINISH buttons ###/

    // Next
    public void addPlayer(View v) {
        String name = namesDisplay.getText().toString(); // Get the name entered by the user

        if (name.equals("") && playersAmount2 == playersAmount) {
            Toast.makeText(NameSelection.this, "Inserisci il nome del Master", Toast.LENGTH_SHORT).show();
        } else if (name.equals("")) {
            Toast.makeText(NameSelection.this, "Inserisci un nome", Toast.LENGTH_SHORT).show();
        } else {

            if (playersAmount2 == playersAmount) { // If there are no names chosen yet,
                next.setVisibility(View.VISIBLE); // we want to see the Next button,
                finish.setVisibility(View.GONE); // and hide the Finish one.
                back.setEnabled(true);
                whoIsMaster.setText(R.string.whoIsPlaying); // and edit our question properly.

                master.add(new Master(this)); // Create the Master
                master.get(0).setName(name);
                namesDisplay.setText(""); // Clear the text area
                playersAmount2--;
                playersLeft.setText(String.valueOf(playersAmount2));


            } else if (playersAmount2 != playersAmount) { // If the names are being chosen,
                back.setEnabled(true); // we want to be able to remove previous names,
                whoIsMaster.setText(R.string.whoIsPlaying); // and edit our question properly.
                players.add(name); // Add the name to the list
                namesDisplay.setText(""); // Clear the text area
                playersAmount2--;
                playersLeft.setText(String.valueOf(playersAmount2));
            }
            if (playersAmount2 == 0) { // If all the names have been chosen,
                next.setVisibility(View.GONE); // we want to hide the Next button,
                finish.setVisibility(View.VISIBLE); // see the Finish one,
                namesDisplay.setVisibility(View.INVISIBLE); // and disable the EditText.
                playersLeft.setVisibility(View.INVISIBLE);
            }
        }
    }

    // Back
    public void removePlayer(View v){
        if (playersAmount2 == playersAmount - 1) { // If we're back to 0,
            back.setEnabled(false); // disable the Back button,
            whoIsMaster.setText(R.string.whoMaster); // and change the question
        }else if(playersAmount2 < playersAmount){
            next.setVisibility(View.VISIBLE);
            finish.setVisibility(View.GONE);
            namesDisplay.setVisibility(View.VISIBLE);
            playersLeft.setVisibility(View.VISIBLE);
            players.remove(players.size() - 1); // Remove the last added name
        }
        playersAmount2++;
        playersLeft.setText(String.valueOf(playersAmount2));
    }

    // Finish
    public void toNextActivity(View v){
        if(playersAmount == 9){ // If there are 9 players only
            characters.addAll(villagerList);
            characters.addAll(clairvoyantList);
            characters.addAll(wolfList);

            Intent intent = new Intent(this, RandomAssignment.class);
            intent.putExtra("VILLAGE", village);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CHARACTERS", (Serializable) characters);
            intent.putExtras(bundle);
            intent.putStringArrayListExtra("PLAYERS_NAMES", (ArrayList<String>) players);
            startActivity(intent);
        }else { // If there are more than 9 players
            Intent intent = new Intent(this, CharacterSelection.class);
            intent.putStringArrayListExtra("PLAYERS_NAMES", (ArrayList<String>) players);
            intent.putExtra("VILLAGE", village);
            startActivity(intent);
        }
    }



    public void initializeVariables(){
        players = new ArrayList<>();
        namesDisplay = (EditText) findViewById(R.id.names);
        next = (Button) findViewById(R.id.next);
        finish = (Button) findViewById(R.id.finish);
        back = (Button) findViewById(R.id.back);
        whoIsMaster = (TextView) findViewById(R.id.whoMaster);
        playersLeft = (TextView) findViewById(R.id.playersLeft);
        master = new ArrayList<>();

        characters = new ArrayList<>();
        villagerList = new ArrayList<>();
        clairvoyantList = new ArrayList<>();
        wolfList = new ArrayList<>();

        // The default characters
        clairvoyantList.add(new Clairvoyant());
        wolfList.add(new Wolf());
        wolfList.add(new Wolf());
        if (players.size() >= 16){
            wolfList.add(new Wolf());
        }
        villagerList.add(new Villager());
        villagerList.add(new Villager());
        villagerList.add(new Villager());
        villagerList.add(new Villager());
        villagerList.add(new Villager());
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PLAYERS_AMOUNT, playersAmount);
        editor.putInt(PLAYERS_AMOUNT2, playersAmount2);
        editor.putString(VILLAGE, village);
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }

}
