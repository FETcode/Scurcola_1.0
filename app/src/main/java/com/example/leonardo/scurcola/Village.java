package com.example.leonardo.scurcola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Village extends AppCompatActivity {

    String village;
    EditText villageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);
        villageText = (EditText) findViewById(R.id.village);
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
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }

}
