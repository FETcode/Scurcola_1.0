package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ListPlayersWolves extends Activity {

    ArrayList<Player> playersNoWolves;
    RecyclerView myList;
    List<Player> playerSavaged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players_wolves);

        playerSavaged = new ArrayList<>();

        Intent intent = this.getIntent();
        playersNoWolves = intent.getParcelableArrayListExtra("PLAYERS");

        myList = (RecyclerView) findViewById(R.id.playersNoWolves);
        myList.setLayoutManager(new LinearLayoutManager(this));
        CoursesAdapter adapter = new CoursesAdapter(playersNoWolves);
        myList.setAdapter(adapter);

        // RecyclerView with a click listener

        CoursesAdapter clickAdapter = new CoursesAdapter(playersNoWolves);

        clickAdapter.setOnEntryClickListener(new CoursesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                // Stuff that will happen when a list item is clicked
                if (playersNoWolves != null) {
                    Player player = playersNoWolves.get(position);
                    playerSavaged.add(player);
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("PLAYERSAVAGED", (ArrayList<? extends Parcelable>) playerSavaged);
                setResult(Activity.RESULT_OK, intent);
                finish();
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




