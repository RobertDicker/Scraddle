package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    public static final int NEWGAME = 0;
    public static final int CONTINUE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startNew(View view) {

        Intent intent = new Intent(this, SelectPlayers.class);
        intent.putExtra("gameMode", NEWGAME);
        startActivity(intent);
    }

    public void continueOldGame(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("gameMode", CONTINUE);
        startActivity(intent);
    }

    public void settings(View view) {
    }

    public void createPlayers(View view) {
    }

    public void goToAnagramActivity(View view) {

        Intent intent = new Intent(this, AnagramChecker.class);

        startActivity(intent);
    }
}
