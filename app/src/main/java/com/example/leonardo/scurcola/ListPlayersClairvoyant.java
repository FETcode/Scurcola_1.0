package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListPlayersClairvoyant extends Activity {

    RecyclerView myList;
    List<Player> playerProbed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players_clairvoyant);

        playerProbed = new ArrayList<>();

        // Get the players and remove the Clairvoyant
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final ArrayList<Player> playersNoClairvoyant = (ArrayList<Player>) bundle.getSerializable("PLAYERS");

        Iterator<Player> i = playersNoClairvoyant.iterator();
        while (i.hasNext()) {
            Player player = i.next(); // must be called before you can call i.remove()
            if(player.getCardName().equals("Clairvoyant")) {
                i.remove();
            }
        }

        myList = (RecyclerView) findViewById(R.id.playersNoClairvoyant);
        myList.setLayoutManager(new LinearLayoutManager(this));
        CoursesAdapter adapter = new CoursesAdapter(playersNoClairvoyant);
        myList.setAdapter(adapter);

        // RecyclerView with a click listener

        CoursesAdapter clickAdapter = new CoursesAdapter(playersNoClairvoyant);

        clickAdapter.setOnEntryClickListener(new CoursesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                if (playersNoClairvoyant != null) {
                    Player player = playersNoClairvoyant.get(position);
                    playerProbed.add(player);
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("PLAYERPROBED", (Serializable) playerProbed);
                intent.putExtras(bundle);
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




