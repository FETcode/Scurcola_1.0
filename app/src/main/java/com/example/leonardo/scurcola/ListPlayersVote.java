package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListPlayersVote extends AppCompatActivity {

    static final String PLAYERS = "PLAYERS";

    RecyclerView myList;
    private ArrayList<Player> players; // Players
    int votes = 0;
    public ArrayList<Player> highestList = new ArrayList<>();
    public ArrayList<Player> highestList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players_vote);

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        String playersJSON = prefs.getString(PLAYERS, null);
        Type type = new TypeToken<List<Player>>(){}.getType();
        Gson gson = new Gson();
        if(playersJSON != null) {
            players = gson.fromJson(playersJSON, type);
        }

        Intent intent = this.getIntent();
        if(intent != null) {
            players = intent.getParcelableArrayListExtra("PLAYERS");
        }

        for(Player p : players){
            System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
        }

        myList = (RecyclerView) findViewById(R.id.playersVote);
        myList.setLayoutManager(new LinearLayoutManager(this));
        CoursesAdapter adapter = new CoursesAdapter(players);
        myList.setAdapter(adapter);


        // RecyclerView with a click listener

        CoursesAdapter clickAdapter = new CoursesAdapter(players);

        clickAdapter.setOnEntryClickListener(new CoursesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                // Let each player vote (ghosts too)
                Player player = players.get(position);
                player.incrementCount();
                //Toast.makeText(ListPlayersVote.this, String.valueOf(player.getCount()), Toast.LENGTH_SHORT).show();
                votes++;

                if(votes == players.size()) {
                    for (Player player1 : players){ // For each player
                        // first of all you need to move the initialization for highest and highest1 INSIDE the loop
                        // with your current code the lists highestlist and highestlis1 each have ONE Player object which is empty
                        // (because in the default constructor of Player you do nothing), you then use the empty initial Plyer in each list to
                        // compare the vote count, this will mostly result in the first if clause(playerCount > highest) being triggered,
                        // which will add the initial empty Player object to highestlist1
                        int highest = highestList.size() == 0 ? -1 : highestList.get(0).getCount();// if the list is empty initialize it with -1 to signal the first player handled
                        int highest1 = highestList1.size() == 0 ? -1 : highestList1.get(0).getCount();

                        int playerCount = player1.getCount(); // Get his votes

                        // you need to check for -1 to handle the case when you're dealing with the first player(
                        // which of course will have the highest count as there's no either player to compare it yet)
                        if(playerCount > highest || highest == -1){ // If they're the highest so far, add it to the 1st list
                            if(playerCount != 0) {
                                highestList1.addAll(highestList);
                                highestList.clear();
                                highestList.add(player1);
                            }
                            // the same for highest1
                        } else if (playerCount > highest1 || highest1 == -1){ // If they're the 2nd highest so far, add it to the 2nd list
                            if(playerCount != 0) {
                                highestList1.clear();
                                highestList1.add(player1);
                            }
                        }else if(playerCount == highest){ // If they're equal to the current highest one, add it to the 1st list as well
                            highestList1.add(player1);
                        } else if (playerCount == highest1){ // If they're equal to the current 2nd highest one, add it to the 2nd list as well
                            highestList1.add(player1);
                        }
                    }

                    for(Player p : players){
                        System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
                    }

                    for(Player p : highestList){
                        System.out.println("--- HIGHESTLIST ---");
                        System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
                    }
                    for(Player p : highestList1){
                        System.out.println("--- HIGHESTLIST1 ---");
                        System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
                    }

                    for (Player player2 : players){
                        player2.setCount(0);
                    }

                    for(Player p : highestList){
                        System.out.println("--- HIGHESTLIST ---");
                        System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
                    }
                    for(Player p : highestList1){
                        System.out.println("--- HIGHESTLIST1 ---");
                        System.out.println("NAME: " + p.getName() + " - CARD: " + p.getCardName() + " - COUNT: " + p.getCount());
                    }
                    // Finally get back to the previous Activity
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("HIGHEST", highestList);
                    intent.putParcelableArrayListExtra("HIGHEST1", highestList1);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        myList.setAdapter(clickAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        String playersJSON = gson.toJson(players);
        editor.putString(PLAYERS, playersJSON);

        editor.putInt("VOTES", votes);
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }
}
