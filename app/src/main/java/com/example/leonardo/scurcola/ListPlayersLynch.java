package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ListPlayersLynch extends AppCompatActivity {

    ArrayList<Player> highest;
    RecyclerView myList;
    List<Player> voters;
    List<Player> playerLynched;
    int votes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players_lynch);

        playerLynched = new ArrayList<>();
        voters = new ArrayList<>();

        // Get the players and remove the Clairvoyant
        Intent intent = this.getIntent();
        voters = intent.getParcelableArrayListExtra("PLAYERS");
        highest = intent.getParcelableArrayListExtra("HIGHEST");

        for (Player p : highest){
            voters.remove(p);
        }


        myList = (RecyclerView) findViewById(R.id.playersLynch);
        myList.setLayoutManager(new LinearLayoutManager(this));
        CoursesAdapter adapter = new CoursesAdapter(highest);
        myList.setAdapter(adapter);

        // RecyclerView with a click listener

        CoursesAdapter clickAdapter = new CoursesAdapter(highest);

        clickAdapter.setOnEntryClickListener(new CoursesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Player voter = voters.get(position);
                voter.incrementCount();
                votes++;
                if(votes == voters.size()) {
                    for (Player voter1 : highest){ // For each player

                        int highest = playerLynched.size() == 0 ? -1 : playerLynched.get(0).getCount();// if the list is empty initialize it with -1 to signal the first player handled

                        int playerCount = voter1.getCount(); // Get his votes

                        // you need to check for -1 to handle the case when you're dealing with the first player(
                        // which of course will have the highest count as there's no either player to compare it yet)
                        if(playerCount > highest || highest == -1){ // If they're the highest so far, add it to the 1st list
                            playerLynched.clear();
                            playerLynched.add(voter1);
                        }else if(playerCount == highest){ // If they're equal to the current highest one, add it to the 1st list as well
                            // Let the Master decide or a coin
                        }
                    }

                    for (Player voters2 : highest){
                        voters2.setCount(0);
                    }
                    // Finally get back to the previous Activity
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("PLAYERLYNCHED", (ArrayList<? extends Parcelable>) playerLynched);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        myList.setAdapter(clickAdapter);
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