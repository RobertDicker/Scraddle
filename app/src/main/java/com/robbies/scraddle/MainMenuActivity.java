package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startNew(View view) {
        Intent intent = new Intent(this, SelectPlayers.class);
        startActivity(intent);
    }

    public void continueOldGame(View view) {
    }

    public void settings(View view) {
    }

    public void createPlayers(View view) {
    }
}
