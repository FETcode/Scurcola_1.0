package com.example.leonardo.scurcola.master;

import android.content.Context;

import com.example.leonardo.scurcola.Player;
import com.example.leonardo.scurcola.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Master extends Player {
    private String mName;
    private Context context;



    public void setName(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    } // Get Master's name

    public static int randInt(int min, int max) { //Get a random int between two values

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    public Master(Context c){ //it's your contsructor method
        this.context = c;
    }


    public void goodMorning() { // Get a random phrase from a String-Array and display it to the user
        int randomNumber = randInt(0, 8); //Random Number
        String[] arrayPhrases = context.getResources().getStringArray(R.array.goodMorning); // String-Array with 'morning phrases
        List<String> listPhrases = new ArrayList<>(); // List

        Collections.addAll(listPhrases, arrayPhrases); // Insert the String-Array in the List
        String randomPhrase = listPhrases.get(randomNumber); // Get the random phrase

        System.out.printf(randomPhrase); // Display it
    }
}
