package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerListAdapter.OnPlayerListener {

    List<Player> mPlayers = new ArrayList<Player>();
    RecyclerView mRecyclerViewPlayers;
    int activePlayer = 0;
    EditText scoreEditText;
    PlayerListAdapter playerAdapter;
    GameDataAdapter gameData = new GameDataAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar

        setContentView(R.layout.activity_main);

        Intent intent = new Intent();


        initialisePlayers(getIntent().getStringArrayListExtra("selectedPlayersIds"));

        mPlayers.get(0).setPlayerTurn(true);

        scoreEditText = findViewById(R.id.editTextAddScore);


        mRecyclerViewPlayers = findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerListAdapter(mPlayers, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));


    }

    private void initialisePlayers(ArrayList<String> playerIds) {

        for (String playerId : playerIds) {
            mPlayers.add(gameData.getIndividualPlayer(playerId));
            Log.d("-----------------------", playerId);
        }


    }


    public void onPlayerClick(int position) {

        Player player = mPlayers.get(position);
        mPlayers.get(activePlayer).setPlayerTurn(false);
        player.setPlayerTurn(true);
        Collections.rotate(mPlayers, mPlayers.size() - position);
        playerAdapter.notifyDataSetChanged();
        scoreEditText.setHint("Add" + mPlayers.get(0).getName() + "'s score");
    }


    public void addToPlayer(View view) {

        if (scoreEditText.getText().toString().trim().length() > 0) {
            mPlayers.get(activePlayer).addScore(scoreEditText.getText().toString());
            // Change Player
            mPlayers.get(activePlayer).setPlayerTurn(false);
            Collections.rotate(mPlayers, 1);
            mPlayers.get(activePlayer).setPlayerTurn(true);
            playerAdapter.notifyDataSetChanged();
            scoreEditText.setText("");
            scoreEditText.setHint("Add " + mPlayers.get(0).getName() + "'s score");
        }


    }
}
