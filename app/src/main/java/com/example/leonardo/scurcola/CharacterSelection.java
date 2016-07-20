package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leonardo.scurcola.characters.Clairvoyant;
import com.example.leonardo.scurcola.characters.Demoniac;
import com.example.leonardo.scurcola.characters.Guard;
import com.example.leonardo.scurcola.characters.Masons;
import com.example.leonardo.scurcola.characters.Medium;
import com.example.leonardo.scurcola.characters.Villager;
import com.example.leonardo.scurcola.characters.Werehamster;
import com.example.leonardo.scurcola.characters.Wolf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CharacterSelection extends AppCompatActivity {

    static final String VILLAGE = "VILLAGE";

    private List<String> players; // Where we'll list players' names
    private List<Card> characters; // Where the picked cards will be stored
        private List<Demoniac> demoniacList;
        private List<Guard> guardList;
        private List<Masons> masonsList;
        private List<Medium> mediumList;
        private List<Villager> villagerList;
        private List<Werehamster> werehamsterList;
        private List<Clairvoyant> clairvoyantList;
        private List<Wolf> wolfList;

    private List<Button> buttonList;
    Button first;
    Button second;
    Button third;
    Button fourth;
    Button fifth;
    Button sixth;
    Button seventh;
    Button eighth;
    Button ninth;

    boolean one;
    boolean two;
    boolean three;
    boolean four;
    boolean five;
    boolean six;
    boolean seven;
    boolean eight;
    boolean nine;


    private TextView countText;
    boolean status;
    int count;
    Bundle bundle;
    private String village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);
        initializeVariables();
        if(bundle != null) {
            village = getIntent().getExtras().getString(VILLAGE);
        }
        buttonList.add(first);
        buttonList.add(second);
        buttonList.add(third);
        buttonList.add(fourth);
        buttonList.add(fifth);
        buttonList.add(sixth);
        countText = (TextView) findViewById(R.id.count);
        count = players.size() - wolfList.size() - villagerList.size() - clairvoyantList.size();
        countText.setText(String.valueOf(count));
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        prefs.getString(VILLAGE, "Scurcola");

        status = prefs.getBoolean("STATUS", false);
        // Buttons' status
        one = prefs.getBoolean("ONE", false);
        two = prefs.getBoolean("TWO", false);
        three = prefs.getBoolean("THREE", false);
        four = prefs.getBoolean("FOUR", false);
        five = prefs.getBoolean("FIVE", false);
        six = prefs.getBoolean("SIX", false);
        seven = prefs.getBoolean("SEVEN", false);
        eight = prefs.getBoolean("EIGHT", false);
        nine = prefs.getBoolean("NINE", false);

        // Set them back to their status
        first.setEnabled(one);
        second.setEnabled(two);
        third.setEnabled(three);
        fourth.setEnabled(four);
        fifth.setEnabled(five);
        sixth.setEnabled(six);
        seventh.setEnabled(seven);
        eighth.setEnabled(eight);
        ninth.setEnabled(nine);
    }

    public void addButton(View v){ //When the user wants to ADD characters

        if(demoniacList.size() == 0){ // If there are no picked characters yet, enable that character's button
            first.setEnabled(true);
        }if (guardList.size() == 0){
            second.setEnabled(true);
        }if (masonsList.size() == 0){
            third.setEnabled(true);
        }if (mediumList.size() == 0){
            fourth.setEnabled(true);
        }if (demoniacList.size() + guardList.size() + masonsList.size() + mediumList.size() + villagerList.size() + werehamsterList.size() < players.size()) {
            fifth.setEnabled(true);
        }else{ // If all the lists bound together < players, enable the button. Else, disable it
            fifth.setEnabled(false);
        }if (werehamsterList.size() == 0){
            sixth.setEnabled(true);
        }


        if(demoniacList.size() != 0){ // If they're full, disable the button
            first.setEnabled(false);
        }if (guardList.size() != 0){
            second.setEnabled(false);
        }if (masonsList.size() != 0){
            third.setEnabled(false);
        }if (mediumList.size() != 0){
            fourth.setEnabled(false);
        }if (werehamsterList.size() != 0){
            sixth.setEnabled(false);
        }

        status = true;
        seventh.setEnabled(false);
        eighth.setEnabled(true);
    }

    public void removeButton(View v){ // When the user wants to REMOVE characters

        if(demoniacList.size() == 0){ // If they're empty, disable the button
            first.setEnabled(false);
        }if (guardList.size() == 0){
            second.setEnabled(false);
        }if (masonsList.size() == 0){
            third.setEnabled(false);
        }if (mediumList.size() == 0){
            fourth.setEnabled(false);
        }if(villagerList.size() == 5){
            fifth.setEnabled(false);
        }if (werehamsterList.size() == 0){
            sixth.setEnabled(false);
        }

        if(demoniacList.size() != 0){ // If there are some cards chosen then turn the Button enabled to remove the cards
            first.setEnabled(true);
        }if (guardList.size() != 0){
            second.setEnabled(true);
        }if (masonsList.size() != 0){
            third.setEnabled(true);
        }if (mediumList.size() != 0){
            fourth.setEnabled(true);
        }if(villagerList.size() > 5){
            fifth.setEnabled(true);
        }if (werehamsterList.size() != 0){
            sixth.setEnabled(true);
        }

        status = false;
        eighth.setEnabled(false);
        seventh.setEnabled(true);
    }

    public void characterClick(View v){ // When the user clicks on one of the characters button
        if(status){ // If the user wants to ADD characters
            switch (v.getId()){ // Check which character's button was clicked and add it to the list, increment the count by 1
                case R.id.first:
                    demoniacList.add(new Demoniac());
                    first.setEnabled(false);
                    count--;
                    break;
                case R.id.second:
                    guardList.add(new Guard());
                    second.setEnabled(false);
                    count--;
                    break;
                case R.id.third:
                    masonsList.add(new Masons());
                    masonsList.add(new Masons());
                    third.setEnabled(false);
                    count-= 2;
                    break;
                case R.id.fourth:
                    mediumList.add(new Medium());
                    fourth.setEnabled(false);
                    count--;
                    break;
                case R.id.fifth: // If all the lists bound together < players - 1, add a villager
                    if (demoniacList.size() + guardList.size() + masonsList.size() +
                            mediumList.size() + villagerList.size() + werehamsterList.size() < players.size() -1) {
                        villagerList.add(new Villager());
                        count--;
                    } else { // If they're < players, add the last villager before they're == players
                        villagerList.add(new Villager());
                        count--;
                        fifth.setEnabled(false);
                    }
                    break;
                default:
                    werehamsterList.add(new Werehamster());
                    sixth.setEnabled(false);
                    count--;
                    break;
            }

            if(count == 0){ // If amount of characters picked == 0, disable every button and add the finish one
                for(Button b : buttonList) {
                    b.setEnabled(false);
                }
                ninth.setVisibility(View.VISIBLE);
            }

        } else { // If the user wants to REMOVE a character
            switch (v.getId()){ // Check which character's button was clicked and remove it from the list, decrement the count by 1
                case R.id.first:
                    demoniacList.remove(demoniacList.size()-1);
                    first.setEnabled(false);
                    count++;
                    break;
                case R.id.second:
                    guardList.remove(guardList.size()-1);
                    second.setEnabled(false);
                    count++;
                    break;
                case R.id.third:
                    masonsList.remove(masonsList.size()-1);
                    masonsList.remove(masonsList.size()-1);
                    third.setEnabled(false);
                    count+= 2;
                    break;
                case R.id.fourth:
                    mediumList.remove(mediumList.size()-1);
                    fourth.setEnabled(false);
                    count++;
                    break;
                case R.id.fifth:
                    villagerList.remove(villagerList.size()-1);
                    if(villagerList.size() == 5){
                        fifth.setEnabled(false);
                    }
                    count++;
                    break;
                default:
                    werehamsterList.remove(werehamsterList.size()-1);
                    sixth.setEnabled(false);
                    count++;
                    break;
            }

            if(count != 0){ // If amount of characters picked != amount of players, remove the finish button
                ninth.setVisibility(View.GONE);
            }

        }

        // Update the count of characters picked
        countText.setText(String.valueOf(count));

    }

    public void toRandomAssignment(View v) {
        characters.addAll(demoniacList);
        characters.addAll(guardList);
        characters.addAll(masonsList);
        characters.addAll(mediumList);
        characters.addAll(villagerList);
        characters.addAll(werehamsterList);
        characters.addAll(clairvoyantList);
        characters.addAll(wolfList);

        Intent intent = new Intent(this, RandomAssignment.class);
        intent.putExtra("VILLAGE", village);
        Bundle bundle = new Bundle();
        bundle.putSerializable("CHARACTERS", (Serializable) characters);
        intent.putExtras(bundle);
        intent.putStringArrayListExtra ("PLAYERS_NAMES", (ArrayList<String>) players);
        startActivity(intent);
}

    public void initializeVariables(){
        players = new ArrayList<>();
        characters = new ArrayList<>();
        buttonList = new ArrayList<>();
        demoniacList = new ArrayList<>();
        guardList = new ArrayList<>();
        masonsList = new ArrayList<>();
        mediumList = new ArrayList<>();
        villagerList = new ArrayList<>();
        werehamsterList = new ArrayList<>();
        clairvoyantList = new ArrayList<>();
        wolfList = new ArrayList<>();

        players = getIntent().getStringArrayListExtra("PLAYERS_NAMES"); // Retrieve players' names

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

        first = (Button) findViewById(R.id.first);
        second = (Button) findViewById(R.id.second);
        third = (Button) findViewById(R.id.third);
        fourth = (Button) findViewById(R.id.fourth);
        fifth = (Button) findViewById(R.id.fifth);
        sixth = (Button) findViewById(R.id.sixth);
        seventh = (Button) findViewById(R.id.seventh);
        eighth = (Button) findViewById(R.id.eighth);
        ninth = (Button) findViewById(R.id.ninth);

        status = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        one = first.isEnabled();
        two = second.isEnabled();
        three = third.isEnabled();
        four = fourth.isEnabled();
        five = fifth.isEnabled();
        six = sixth.isEnabled();
        seven = seventh.isEnabled();
        eight = eighth.isEnabled();
        nine = ninth.isEnabled();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("lastActivity", getClass().getName());
        // Buttons status
        editor.putBoolean("ONE", one);
        editor.putBoolean("TWO", two);
        editor.putBoolean("THREE", three);
        editor.putBoolean("FOUR", four);
        editor.putBoolean("FIVE", five);
        editor.putBoolean("SIX", six);
        editor.putBoolean("SEVEN", seven);
        editor.putBoolean("EIGHT", eight);
        editor.putBoolean("NINE", nine);

        editor.putBoolean("STATUS", status);
        editor.putString(VILLAGE, village);
        editor.apply();
    }

}
