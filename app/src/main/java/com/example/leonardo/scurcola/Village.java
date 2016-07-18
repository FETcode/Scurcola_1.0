package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Village extends AppCompatActivity {

    static final String TEXT_LEFT = "TEXT_LEFT";

    String village;
    String textLeft;
    EditText villageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);
        villageText = (EditText) findViewById(R.id.village);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        if(!TEXT_LEFT.equals("")) {
            textLeft = prefs.getString(TEXT_LEFT, "");
            villageText.setText(textLeft);
        }
    }

    public void toPlayerSelection(View v){
        village = villageText.getText().toString();
        if(village.equals("")){
            Toast.makeText(Village.this, "Inserisci il nome del villaggio!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, PlayerSelection.class);
            intent.putExtra("VILLAGE", village);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.putString("TEXT_LEFT", villageText.getText().toString());
        editor.apply();
    }

}
