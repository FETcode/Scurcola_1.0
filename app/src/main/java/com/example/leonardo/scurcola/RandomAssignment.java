package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomAssignment extends AppCompatActivity {

    static final String VILLAGE = "VILLAGE";
    static final String CHARACTERS = "CHARACTERS";
    static final String PLAYERS_NAMES = "PLAYERS_NAMES";
    static final String COUNT = "COUNT";
    static final String PLAYERS = "PLAYERS";
    static final String NAMES = "NAMES";
    static final String CARDS = "CARDS";

    private List<Card> cards; // Players' cards
    private List<String> names; // Players' names
    private List<Player> players; // Players
    int count;
    private String village;

    TextView card;
    TextView name;
    Button pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_assignment);
            // Probably initialize members with default values for a new instance
            initializeVariables();
            Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            village = bundle.getString(VILLAGE);
            cards = getIntent().getParcelableArrayListExtra(CHARACTERS); //Players' cards
            names = getIntent().getStringArrayListExtra(PLAYERS_NAMES); // Retrieve players' names
        }
        }

    @Override
    protected void onStart() {
        super.onStart();

        // Shared Preferences
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        village = prefs.getString(VILLAGE, "Scurcola");
        count = prefs.getInt(COUNT, 0);

        // JSON
        Gson gson = new Gson();
        Type listPlayer = new TypeToken<List<Player>>() {
        }.getType();
        Type listString = new TypeToken<List<String>>() {
        }.getType();
        Type listCard = new TypeToken<List<Card>>() {
        }.getType();

        String playersJSON = prefs.getString(PLAYERS, null);
        String namesJSON = prefs.getString(NAMES, null);
        String cardsJSON = prefs.getString(CARDS, null);
        if (playersJSON != null) {
            players = gson.fromJson(playersJSON, listPlayer);
        }
        if (namesJSON != null) {
            names = gson.fromJson(namesJSON, listString);
        }
        if (cardsJSON != null) {
            cards = gson.fromJson(cardsJSON, listCard);
        }
        if (playersJSON == null) {
            Toast.makeText(RandomAssignment.this, "Run", Toast.LENGTH_SHORT).show();
            fillPlayers();
            randomize();
            assign();
        }
        if (playersJSON != null) {
            System.out.println("Count value NOW is: " + count + " **************************");
            // Display the last texts seen
            card.setText(players.get(count - 1).getCardName());
            name.setText(players.get(count - 1).getName());
            if (count == players.size()) {
                pick.setText(R.string.finish);
            }
            System.out.println("Count FINAL value is: " + count + " **************************");
        }
    }

    public void fillPlayers(){
        for(int i = 0; i < names.size(); i++){
            players.add(new Player());
        }
    }

    public void randomize(){
        long seed = System.nanoTime();
        Collections.shuffle(cards, new Random(seed));
        Collections.shuffle(names, new Random(seed));
        Collections.shuffle(players, new Random(seed));
    }

    public void assign(){
        for(int f = 0; f < players.size(); f++){
            players.get(f).setCard(cards.get(f));
            players.get(f).setName(names.get(f));
            players.get(f).setId(f);
        }
    }

    public void pick(View v) {

        if (count < players.size()) {
            card.setText(players.get(count).getCardName());
            name.setText(players.get(count).getName());
            count++;
        }
        if (pick.getText().equals(getString(R.string.finish))) {
            Intent intent = new Intent(this, Game.class);
            intent.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
            intent.putExtra("VILLAGE", village);
            startActivity(intent);
        }
        if (count == players.size()) {
            pick.setText(R.string.finish);
        }
    }



    public void initializeVariables(){
        players = new ArrayList<>();
        card = (TextView) findViewById(R.id.card);
        name = (TextView) findViewById(R.id.name);
        pick = (Button) findViewById(R.id.pick);
        count = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Gson gson = new Gson();
        String playersJSON = gson.toJson(players);
        String namesJSON = gson.toJson(names);
        String cardsJSON = gson.toJson(cards);

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(COUNT, count);
        editor.putString("lastActivity", getClass().getName());
        editor.putString(PLAYERS, playersJSON);
        editor.putString(NAMES, namesJSON);
        editor.putString(CARDS, cardsJSON);
        editor.apply();
        System.out.println("Count value BEFORE was: " + count + " **************************");
    }
}
