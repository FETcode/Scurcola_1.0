package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomAssignment extends AppCompatActivity {

    static final String VILLAGE = "VILLAGE";
    static final String CHARACTERS = "CHARACTERS";
    static final String PLAYERS_NAMES = "PLAYERS_NAMES";
    static final String I = "I";

    private List<Card> cards; // Players' cards
    private List<String> names; // Players' names
    private List<Player> players; // Players
    int i;
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
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            prefs.getString(VILLAGE, "");
            prefs.getInt(I, 0);
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
        for(int i = 0; i < players.size(); i++){
            players.get(i).setCard(cards.get(i));
            players.get(i).setName(names.get(i));
            players.get(i).setId(i);
        }
    }

    public void pick(View v) {

        if (i < players.size()) {
            if (i < players.size()) {
                card.setText(players.get(i).getCardName());
                name.setText(players.get(i).getName());
                i++;
            }
            if (i == players.size()) {
                pick.setText(R.string.finish);
            }
        }else if(i == players.size()){
            Intent intent = new Intent(this, Game.class);
            intent.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
            intent.putExtra("VILLAGE", village);
            startActivity(intent);
        }
    }



    public void initializeVariables(){
        players = new ArrayList<>();
        card = (TextView) findViewById(R.id.card);
        name = (TextView) findViewById(R.id.name);
        pick = (Button) findViewById(R.id.pick);
        i = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(I, i);
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }
}
