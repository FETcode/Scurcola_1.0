package com.example.leonardo.scurcola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity {

    static final String VILLAGE = "VILLAGE";
    static final String PLAYERS = "PLAYERS";
    static final String RECENTLY_KILLED = "RECENTLY_KILLED";
    static final String PLAYERS_CAN_VOTE = "PLAYERS_CAN_VOTE";
    static final String PLAYERS_NO_WOLVES = "PLAYERS_NO_WOLVES";
    static final String HIGHEST = "HIGHEST";

    static final String LAST_LYNCHED = "LAST_LYNCHED";
    static final String PLAYER_CHOSE = "PLAYER_CHOSE";
    static final String PLAYER_PROBED = "PLAYER_PROBED";
    static final String CLAIRVOYANT = "CLAIRVOYANT";
    static final String GUARD = "GUARD";
    static final String MEDIUM = "MEDIUM";

    static final String NIGHT_COUNTER = "NIGHT_COUNTER";
    static final String DAY_COUNTER = "DAY_COUNTER";
    static final String NIGHT_INSIDE_COUNTER = "NIGHT_INSIDE_COUNTER";
    static final String DAY_INSIDE_COUNTER = "DAY_INSIDE_COUNTER";
    static final String WOLVES_LEFT = "WOLVES_LEFT";
    static final String VILLAGERS_LEFT = "VILLAGERS_LEFT";
    static final String NIGHT = "NIGHT";

    static final String PREFS_NAME = "PREFS_NAME";
    static final String ACTIVITY = "ACTIVITY";

    /* ---------- Variables Start ---------- */
    List<Player> players; // All the active players
    List<Player> recentlyKilled;
    List<Player> playersCanVote;
    List<Player> playersNoWolves;
    ArrayList<Player> highest;

    private String village;
    private String activity;
    String saved;

    ArrayAdapter<String> adapter;
    ArrayList<String> messages;
    Button next;
    ListView screen; // Where all the messages will be shown

    // Players
    Player lastLynched;
    Player playerChose;
    Player playerProbed;
    Player clairvoyant;
    Player guard;
    Player medium;

    // Counters
    int nightCounter;
    int dayCounter;
    int nightInsideCounter;
    int dayInsideCounter;
    int wolvesLeft;
    int villagersLeft;
    boolean night;

    public static final int REQUEST_SAVAGE = 1; // Kill
    public static final int REQUEST_VOTE = 2; // Vote
    public static final int REQUEST_LYNCH = 3; // Lynch
    public static final int REQUEST_CLAIRVOYANT = 4; // Clairvoyant's turn
    public static final int REQUEST_GUARD = 5; // Guard's turn
    /* ---------- Variables End ---------- */

    /* ---------- onCreate() ---------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        activity = settings.getString(ACTIVITY, "Game");

        // Probably initialize members with default values for a new instance
        initializeVariables();
        // Initialize Characters
        initializeCharacters();
        // Initialize Lists
        initializeLists();
        // Greetings to the user
        greetings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);

        // Restore value of members from saved state
        village = prefs.getString(VILLAGE, "");

        // Lists
        String playersJSON = getPreferences(MODE_PRIVATE).getString(PLAYERS, null);
        String recentlyKilledJSON = getPreferences(MODE_PRIVATE).getString(RECENTLY_KILLED, null);
        String playersCanVoteJSON = getPreferences(MODE_PRIVATE).getString(PLAYERS_CAN_VOTE, null);
        String playersNoWolvesJSON = getPreferences(MODE_PRIVATE).getString(PLAYERS_NO_WOLVES, null);
        String highestJSON = getPreferences(MODE_PRIVATE).getString(HIGHEST, null);

        String lastLynchedJSON = getPreferences(MODE_PRIVATE).getString(LAST_LYNCHED, null);
        String playerChoseJSON = getPreferences(MODE_PRIVATE).getString(PLAYER_CHOSE, null);
        String playerProbedJSON = getPreferences(MODE_PRIVATE).getString(PLAYER_PROBED, null);
        String clairvoyantJSON = getPreferences(MODE_PRIVATE).getString(CLAIRVOYANT, null);
        String guardJSON = getPreferences(MODE_PRIVATE).getString(GUARD, null);
        String mediumJSON = getPreferences(MODE_PRIVATE).getString(MEDIUM, null);

            Type type = new TypeToken <List<Player>>(){}.getType();
            Type type1 = new TypeToken <Player>(){}.getType();

            players = new Gson().fromJson(playersJSON, type);
            recentlyKilled = new Gson().fromJson(recentlyKilledJSON, type);
            playersCanVote = new Gson().fromJson(playersCanVoteJSON, type);
            playersNoWolves = new Gson().fromJson(playersNoWolvesJSON, type);
            highest = new Gson().fromJson(highestJSON, type);

            // Players
            lastLynched = new Gson().fromJson(lastLynchedJSON, type1);
            playerChose = new Gson().fromJson(playerChoseJSON, type1);
            playerProbed = new Gson().fromJson(playerProbedJSON, type1);
            clairvoyant = new Gson().fromJson(clairvoyantJSON, type1);
            guard = new Gson().fromJson(guardJSON, type1);
            medium = new Gson().fromJson(mediumJSON, type1);

            // Counters
            nightCounter = prefs.getInt(NIGHT_COUNTER, 0);
            dayCounter = prefs.getInt(DAY_COUNTER, 0);
            nightInsideCounter = prefs.getInt(NIGHT_INSIDE_COUNTER, 0);
            dayInsideCounter = prefs.getInt(DAY_INSIDE_COUNTER, 0);
            wolvesLeft = prefs.getInt(WOLVES_LEFT, 0);
            villagersLeft = prefs.getInt(VILLAGERS_LEFT, 0);
            night = prefs.getBoolean(NIGHT, true);
        }

    /* ---------- MAIN GAME ---------- */
    public void game(View v) {
        if (night) {
            // Night starts
            night();
        } else {
            // Day starts
            day();
        }
    }


    /* ---------- Night mechanics ---------- */
    public void night() {
        switch (nightCounter) {
            case 1: // First night, Master dies
                switch (nightInsideCounter) {
                    case 1:
                        write(R.string.night, village); // Say it's night, everybody close their eyes
                        nightInsideCounter++;
                        break;
                    case 2:
                        masonsShow(); // Masons see each other
                        nightInsideCounter++;
                        break;
                    case 3:
                        wolvesShow(); // Wolves see each other
                        nightInsideCounter++;
                        break;
                    default:
                        write(R.array.goodMorning, randInt(0, 7), village); // It's morning
                        nightInsideCounter = 1; // Reset the internal counter
                        nightCounter++; // Increment the counter to switch to the 2nd night next time
                        night = false; // Night has ended
                        break;
                }
                break;
            default: // All the other nights
                switch (nightInsideCounter) {
                    case 1:
                        write(R.string.night, village); // Say it's night, everybody close their eyes
                        recentlyKilled.clear(); // Clear all the recently killed players to get the new ones
                        nightInsideCounter++;
                        break;
                    case 2:
                        clairvoyant(); // Clairvoyant's turn
                        Toast.makeText(Game.this, String.valueOf(clairvoyant.getLifeStatus()), Toast.LENGTH_SHORT).show();
                        if(clairvoyant.getLifeStatus()) {
                        /* --- Let him choose ---*/
                            Intent intent = new Intent(this, ListPlayersClairvoyant.class);
                            intent.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                            startActivityForResult(intent, REQUEST_CLAIRVOYANT);
                        /* --------------------- */
                        }
                        if(medium == null) {
                            nightInsideCounter++;
                        }
                        if(guard == null) {
                            nightInsideCounter++;
                        }
                        nightInsideCounter++;
                        break;
                    case 3:
                        medium(); // Medium's turn
                        if(medium.getLifeStatus()) {
                            if (lastLynched.getCardName().equals("Wolf")) { // Check if the last lynched player was a Wolf
                                // Say he is
                                write(lastLynched.getName() + " è un lupo!");
                            } else {
                                // Say he's not
                                write(lastLynched.getName() + " non è un lupo.");
                            }
                        }
                        if(guard == null) {
                            nightInsideCounter++;
                        }
                        nightInsideCounter++;
                        break;
                    case 4:
                            guard(); // Guard's turn
                            if (guard.getLifeStatus()) {
                        /* -- Let him choose -- */
                                Intent intent1 = new Intent(this, ListPlayersGuard.class);
                                intent1.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                                startActivityForResult(intent1, REQUEST_GUARD);
                        /* -------------------- */
                            }
                            nightInsideCounter++;
                            break;
                    case 5:
                        wolves(); // Wolves have dinner :)
                        /* -- Let 'em choose -- */
                        Intent intent2 = new Intent(this, ListPlayersWolves.class);
                        intent2.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                        startActivityForResult(intent2, REQUEST_SAVAGE);
                        /* -------------------- */
                        nightInsideCounter++;
                        break;
                    default:
                        villagersLeft = 0;
                        for(Player player : players){
                            if(!player.getCardName().equals("Wolf")){
                                villagersLeft++;
                            }
                        }
                        if(villagersLeft == wolvesLeft){
                            //Game over. Wolves win
                            Toast.makeText(Game.this, "Game Over. Wolves win.", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(this, GameOver.class);
                            intent1.putExtra("GOODEND", false);
                            startActivity(intent1);
                        }

                        write(R.array.goodMorning, randInt(0, 7), village); // It's morning
                        nightInsideCounter = 1; // Reset the internal counter
                        nightCounter++; // Increment the counter to switch to the next night
                        night = false; // Night has ended
                        break;
                }
                break;
        }
    }

    /* ---------- Day mechanics ---------- */
    public void day(){
        switch (dayCounter){

            case 1: // First Day
                switch (dayInsideCounter){
                    case 1:
                        write(R.string.savagedMaster); // Display the Master's died
                        dayInsideCounter++;
                        break;
                    case 2:
                        write(R.string.talk); // Players have a max of 3 minutes to discuss
                        dayInsideCounter++;
                        break;
                    case 3:
                        /* -- Let 'em vote -- */
                        System.out.println("[1] The players are " + players.size() + ".");
                        Intent intent = new Intent(this, ListPlayersVote.class);
                        intent.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                        startActivityForResult(intent, REQUEST_VOTE);
                         /* ----------------- */
                        dayInsideCounter++;
                        break;
                    case 4:
                        write(R.string.speeches); // Those accused have their own speech
                        dayInsideCounter++;
                        break;
                    case 5:
                        /* -- Let 'em choice -- */
                        Intent intent1 = new Intent(this, ListPlayersLynch.class);
                        intent1.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                        intent1.putParcelableArrayListExtra("HIGHEST", highest);
                        startActivityForResult(intent1, REQUEST_LYNCH);
                        /* -------------------- */
                        dayInsideCounter++;
                        break;
                    default:
                        // Display who's been killed
                        dayInsideCounter = 1;
                        dayCounter++;
                        night = true;
                        break;
                }
                break;
            default: // All the other days
                switch (dayInsideCounter){
                    case 1:
                        if(recentlyKilled.size() == 0){
                            write("Stanotte nessuno è stato ucciso.");
                        } else {
                            for (int i = 0; i < recentlyKilled.size(); i++) {
                                write(recentlyKilled.get(i).getName() + " è stato ucciso questa notte!");
                            }
                        }
                        dayInsideCounter++;
                        break;
                    case 2:
                        write(R.string.talk); // Players have a max of 3 minutes to discuss
                        dayInsideCounter++;
                        break;
                    case 3:
                        /* -- Let 'em vote -- */
                        Intent intent = new Intent(this, ListPlayersVote.class);
                        intent.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                        startActivityForResult(intent, REQUEST_VOTE);
                        /* -------------------- */
                        dayInsideCounter++;
                        break;
                    case 4:
                        write(R.string.speeches); // Those accused have their own speech
                        dayInsideCounter++;
                        break;
                    case 5:
                        /* -- Let 'em choice -- */
                        Intent intent1 = new Intent(this, ListPlayersLynch.class);
                        intent1.putParcelableArrayListExtra("PLAYERS", (ArrayList<? extends Parcelable>) players);
                        intent1.putParcelableArrayListExtra("HIGHEST", highest);
                        startActivityForResult(intent1, REQUEST_LYNCH);
                        /* -------------------- */
                        dayInsideCounter++;
                        break;
                    default:
                        // Display who's been killed

                        //Check if every Wolf has been killed
                        wolvesLeft = 0;
                        for(Player player : players){
                            if(player.getCardName().equals("Wolf")){
                                wolvesLeft++;
                            }
                        }
                        villagersLeft = 0;
                        for(Player player : players){
                            if(!player.getCardName().equals("Wolf")){
                                villagersLeft++;
                            }
                        }
                        if(wolvesLeft == 0){
                            // Game over. Villagers win.
                            Toast.makeText(Game.this, "Game over. Villagers have killed all the wolves!", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(this, GameOver.class);
                            intent2.putExtra("VILLAGE", village);
                            intent2.putExtra("GOODEND", true);
                            startActivity(intent2);
                        }
                        if(villagersLeft == wolvesLeft){
                            //Game over. Wolves win
                            Toast.makeText(Game.this, "Game Over. Wolves win.", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(this, GameOver.class);
                            intent2.putExtra("GOODEND", false);
                            startActivity(intent2);
                        }

                        dayInsideCounter = 1;
                        dayCounter++;
                        night = true;
                        break;
                }
                break;
        }
    }


    /* ---------- Characters call ---------- */
    public void masonsShow(){
        write(R.string.masonsCall);
    } // 1st night
    public void wolvesShow(){
        write(R.array.wolvesCall, 0);
    } // 1st night
    public void clairvoyant(){
        write(R.string.clairvoyantCall);
    }
    public void medium(){
        write(R.string.mediumCall);
    }
    public void guard(){
        write(R.string.guardCall);
    }
    public void wolves(){
        write(R.array.wolvesCall, 1);
    }


    /* ---------- Utilities ---------- */
    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public void write(String msg){
        messages.add(msg);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        screen.setAdapter(adapter);
    }
    public void write(int id){
        String msg = getResources().getString(id);
        messages.add(msg);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        screen.setAdapter(adapter);
    }
    public void write(int id, int position){
        String[] msg = getResources().getStringArray(id);
        messages.add(msg[position]);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        screen.setAdapter(adapter);
    }
    public void write(int id, int position, String text){
        String[] msg = getResources().getStringArray(id);
        messages.add(String.format(msg[position], text));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        screen.setAdapter(adapter);
    }
    public void write(int id, String text){
        String msg = getResources().getString(id);
        messages.add(String.format(msg, text));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        screen.setAdapter(adapter);
    }
    public void greetings(){
        write(R.array.greetings, randInt(0, 3), village);
    }


    /* ---------- MECHANICS AFTER EVERY CHOICE ---------- */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* -------------------------------------------- */
        /* ---------- If they want to SAVAGE ---------- */
        /* -------------------------------------------- */

        if(requestCode == REQUEST_SAVAGE) {

            if(resultCode == Activity.RESULT_OK) {
                // Get the Player clicked
                List<Player> playerSavaged = data.getParcelableArrayListExtra("PLAYERSAVAGED");
                if (playerSavaged != null) {
                    playerChose = playerSavaged.get(0);
                }

                if(playerChose.getCardName().equals("Werehamster") || playerChose.getProtected()){
                   // Nobody was savaged (the Werehamster or protected players can't be savaged)
                    playerChose.setProtected(false);
                } else {
                    // Savage the player chose
                    playerChose.setLifeStatus(false);
                    recentlyKilled.add(playerChose);

                    if(playerChose.getId() == clairvoyant.getId()){
                        clairvoyant.setLifeStatus(false);
                    }  else if (medium != null && playerChose.getId() == medium.getId()){
                        medium.setLifeStatus(false);
                    } else if (guard != null && playerChose.getId() == guard.getId()){
                        guard.setLifeStatus(false);
                    }
                    Toast.makeText(Game.this, String.valueOf(playerChose.getLifeStatus()), Toast.LENGTH_SHORT).show();

                    Iterator<Player> i = players.iterator();
                    while (i.hasNext()) {
                        Player player = i.next(); // must be called before you can call i.remove()
                        if(player.getId() == playerChose.getId()) {
                            i.remove();
                        }
                    }

                    Iterator<Player> n = playersNoWolves.iterator();
                    while (n.hasNext()) {
                        Player player = n.next(); // must be called before you can call i.remove()
                        if(player.getId() == playerChose.getId()) {
                            n.remove();
                        }
                    }

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Some stuff that will happen if there's no result
            }
        }


        /* ------------------------------------------ */
        /* ---------- If they want to VOTE ---------- */
        /* ------------------------------------------ */
        if(requestCode == REQUEST_VOTE) {

            if(resultCode == Activity.RESULT_OK) {


                // Get the Player clicked
                ArrayList<Player> highestList = data.getParcelableArrayListExtra("HIGHEST");
                ArrayList<Player> highestList1 = data.getParcelableArrayListExtra("HIGHEST1");
                highest.clear();
                // Add the most voted players to a signle List, highest
                    highest.addAll(highestList);
                    highestList.clear();

                    highest.addAll(highestList1);
                    highestList1.clear();

                // Write the names in chat
                write("Il villaggio ha i propri sospettati:");
                for (Player player : highest){
                    write(player.getName());
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Some stuff that will happen if there's no result
            }

        }

        /* ------------------------------------------- */
        /* ---------- If they want to LYNCH ---------- */
        /* ------------------------------------------- */

        if(requestCode == REQUEST_LYNCH) {

            if(resultCode == Activity.RESULT_OK) {
                // Get the Player clicked
                List<Player> playerLynched = data.getParcelableArrayListExtra("PLAYERLYNCHED");
                if (playerLynched != null) {
                    lastLynched = playerLynched.get(0);
                }
                // Kill the player and display the death message
                lastLynched.setLifeStatus(false);

                if(lastLynched.getId() == clairvoyant.getId() && clairvoyant != null){
                    clairvoyant.setLifeStatus(false);
                }  else if (medium != null && lastLynched.getId() == medium.getId()){
                    medium.setLifeStatus(false);
                } else if (guard != null && lastLynched.getId() == guard.getId()){
                    guard.setLifeStatus(false);
                }
                Toast.makeText(Game.this, String.valueOf(clairvoyant.getLifeStatus()), Toast.LENGTH_SHORT).show();

                Iterator<Player> i = players.iterator();
                while (i.hasNext()) {
                    Player player = i.next(); // must be called before you can call i.remove()
                    if(player.getId() == lastLynched.getId()) {
                        i.remove();
                    }
                }

                Iterator<Player> n = playersNoWolves.iterator();
                while (n.hasNext()) {
                    Player player = n.next(); // must be called before you can call i.remove()
                    if(player.getId() == lastLynched.getId()) {
                        n.remove();
                    }
                }

                write(lastLynched.getName() + " è stato linciato dai popolani.");

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Some stuff that will happen if there's no result
            }
        }

        /* --------------------------------- */
        /* ---------- CLAIRVOYANT ---------- */
        /* --------------------------------- */

        if(requestCode == REQUEST_CLAIRVOYANT) {

            if(resultCode == Activity.RESULT_OK) {
                // Get the Player clicked
                Bundle bundle = data.getExtras();
                List<Player> playerProbedList = data.getParcelableArrayListExtra("PLAYERPROBED");
                if (playerProbedList != null) {
                    playerProbed = playerProbedList.get(0);
                }

                if(playerProbed.getCardName().equals("Wolf")){
                    // Say he is
                    write(playerProbed.getName() + " è un lupo!");
                } else if(playerProbed.getCardName().equals("Werehamster")) {
                    playerProbed.setLifeStatus(false);

                    Iterator<Player> i = players.iterator();
                    while (i.hasNext()) {
                        Player player = i.next(); // must be called before you can call i.remove()
                        if(player.getId() == playerProbed.getId()) {
                            i.remove();
                        }
                    }

                    Iterator<Player> n = playersNoWolves.iterator();
                    while (n.hasNext()) {
                        Player player = n.next(); // must be called before you can call i.remove()
                        if(player.getId() == playerProbed.getId()) {
                            n.remove();
                        }
                    }

                    recentlyKilled.add(playerProbed);
                    write(playerProbed.getName() + " non è un lupo.");
                } else if(!playerProbed.getCardName().equals("Wolf")){
                    // Say he's not
                    write(playerProbed.getName() + " non è un lupo.");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Some stuff that will happen if there's no result
            }
        }

        /* --------------------------- */
        /* ---------- GUARD ---------- */
        /* --------------------------- */

        if(requestCode == REQUEST_GUARD) {

            if(resultCode == Activity.RESULT_OK) {

                // Get the clicked Player
                int position = data.getIntExtra("pos", 0);
                Player player = players.get(position);
                player.setProtected(true);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Some stuff that will happen if there's no result
            }
        }
    }

    public void initializeCharacters() {
        for (Player player : players) {
            if (player.getCardName().equals("Clairvoyant")) {
                clairvoyant = player;
            }
            if (player.getCardName().equals("Guard")) {
                guard = player;
            }
            if (player.getCardName().equals("Medium")) {
                medium = player;
            }
        }

    }

    public void initializeVariables() {
        screen = (ListView) findViewById(R.id.screen);
        messages = new ArrayList<>();
        next = (Button) findViewById(R.id.nextMessage);
        nightCounter = 1;
        dayCounter = 1;
        nightInsideCounter = 1;
        dayInsideCounter = 1;
        night = true;
        wolvesLeft = 0;
        villagersLeft = 0;
        recentlyKilled = new ArrayList<>();
        playersCanVote = new ArrayList<>();
        playersNoWolves = new ArrayList<>();
        highest = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            village = bundle.getString(VILLAGE);
            players = getIntent().getParcelableArrayListExtra("PLAYERS"); // Active players
        }
        for(Player player : players){ // WolvesLeft
            if(player.getCardName().equals("Wolf")){
                wolvesLeft++;
            }
        }
        for(Player player : players){
            if(player.getCardName().equals("Villager")){
                villagersLeft++;
            }
        }
    }

    public void initializeLists(){
        // Players who can vote (w/ ghosts)
        for(Player player : players){
            playersCanVote.add(player);
        }
        // Players without Wolves
        for(Player player : players){
            playersNoWolves.add(player);
        }

        Iterator<Player> i = playersNoWolves.iterator();
        while (i.hasNext()) {
            Player player = i.next(); // must be called before you can call i.remove()
            if(player.getCardName().equals("Wolf")) {
                i.remove();
            }
        }
        for(Player player : players){
            System.out.println(player.getCardName());
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(VILLAGE, village);

        // Lists
        String playersJSON = new Gson().toJson(players);
        String recentlyKilledJSON = new Gson().toJson(recentlyKilled);
        String playersCanVoteJSON = new Gson().toJson(playersCanVote);
        String playersNoWolvesJSON = new Gson().toJson(playersNoWolves);
        String highestJSON = new Gson().toJson(highest);

        editor.putString(PLAYERS, playersJSON).apply();
        editor.putString(RECENTLY_KILLED, recentlyKilledJSON).apply();
        editor.putString(PLAYERS_CAN_VOTE, playersCanVoteJSON).apply();
        editor.putString(PLAYERS_NO_WOLVES, playersNoWolvesJSON).apply();
        editor.putString(HIGHEST, highestJSON).apply();

        // Players
        editor.putString(LAST_LYNCHED, new Gson().toJson(lastLynched)).apply();
        editor.putString(PLAYER_CHOSE, new Gson().toJson(playerChose)).apply();
        editor.putString(PLAYER_PROBED, new Gson().toJson(playerProbed)).apply();
        editor.putString(CLAIRVOYANT, new Gson().toJson(clairvoyant)).apply();
        editor.putString(GUARD, new Gson().toJson(guard)).apply();
        editor.putString(MEDIUM, new Gson().toJson(medium)).apply();

        // Counters
        editor.putInt(NIGHT_COUNTER, nightCounter);
        editor.putInt(DAY_COUNTER, dayCounter);
        editor.putInt(NIGHT_INSIDE_COUNTER, nightInsideCounter);
        editor.putInt(DAY_INSIDE_COUNTER, dayInsideCounter);
        editor.putInt(WOLVES_LEFT, wolvesLeft);
        editor.putInt(VILLAGERS_LEFT, villagersLeft);
        editor.putBoolean(NIGHT, night);

        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }
}
