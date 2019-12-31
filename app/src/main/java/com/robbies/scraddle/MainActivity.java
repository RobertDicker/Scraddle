package com.robbies.scraddle;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerAdapter.OnPlayerListener {

    List<Player> mAllPlayers = new ArrayList<Player>();
    RecyclerView mRecyclerViewPlayers;
    int activePlayer = 0;
    EditText scoreEditText;
    PlayerAdapter playerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar

        setContentView(R.layout.activity_main);

        Player player1 = new Player("Robbie");
        Player player2 = new Player("Katherine");
        Player player3 = new Player("Steve");
        mAllPlayers.add(player1);
        mAllPlayers.add(player2);
        mAllPlayers.add(player3);

        player1.setPlayerTurn(true);

        scoreEditText = findViewById(R.id.editTextAddScore);


        mRecyclerViewPlayers = findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerAdapter(mAllPlayers, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));


    }


    public void onPlayerClick(int position) {

        Player player = mAllPlayers.get(position);
        mAllPlayers.get(activePlayer).setPlayerTurn(false);
        player.setPlayerTurn(true);
        Collections.rotate(mAllPlayers, mAllPlayers.size() - position);
        playerAdapter.notifyDataSetChanged();
        scoreEditText.setHint("Add" + mAllPlayers.get(0).getName() + "'s score");
    }


    public void addToPlayer(View view) {

        if (scoreEditText.getText().toString().trim().length() > 0) {
            mAllPlayers.get(activePlayer).addScore(scoreEditText.getText().toString());
            // Change Player
            mAllPlayers.get(activePlayer).setPlayerTurn(false);
            Collections.rotate(mAllPlayers, 1);
            mAllPlayers.get(activePlayer).setPlayerTurn(true);
            playerAdapter.notifyDataSetChanged();
            scoreEditText.setText("");
            scoreEditText.setHint("Add " + mAllPlayers.get(0).getName() + "'s score");
        }


    }
}
