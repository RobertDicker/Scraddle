package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements PlayerListAdapter.OnPlayerListener {

    ArrayList<Player> mPlayers = new ArrayList<>();
    RecyclerView mRecyclerViewPlayers;
    int activePlayer;
    EditText scoreEditText;
    PlayerListAdapter playerAdapter;
    GameDataAdapter gameData;
    Match currentMatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar

        setContentView(R.layout.activity_main);


        gameData = new GameDataAdapter(getApplicationContext());
        initialiseMatch(getIntent().getIntExtra("gameMode", -1));



        scoreEditText = findViewById(R.id.editTextAddScore);

        mRecyclerViewPlayers = findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerListAdapter(mPlayers, currentMatch, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
    }


    // Create all the players and create a match
    private void initialiseMatch(int gameMode) {


        currentMatch = gameMode == 0 ? new Match(getIntent().getStringArrayListExtra("selectedPlayersIds")) : gameData.getAllMatches().get(gameData.getAllMatches().size() - 1);

        ArrayList<String> playerIds = currentMatch.getPlayerIds();
        for (String playerId : playerIds) {
            mPlayers.add(gameData.getIndividualPlayer(playerId));
        }

        //Set player 1's first turn, will cycle through players in order.
        if (gameMode == 0) {
            mPlayers.get(0).setPlayerTurn(true);

        }

    }


    public void onPlayerClick(int position) {

        Player player = mPlayers.get(position);
        //Set active player to the current selected player and rotate to the top of screen.

        changePlayer(mPlayers.size() - position);
    }


    public void addToPlayer(View view) {
        int tempScore = Integer.parseInt(scoreEditText.getText().toString());
        Player currentPlayer = mPlayers.get(activePlayer);

        if (scoreEditText.getText().toString().trim().length() > 0) {
            currentMatch.addScoreToPlayer(currentPlayer.getPlayerID(), tempScore);

            //Check for score records
            if (tempScore > currentPlayer.getPersonalBest()) {
                currentPlayer.setPersonalBest(tempScore);
            }
            // Next Player
            changePlayer(-1);
        }
    }

    private void changePlayer(int rotateDistance) {

        mPlayers.get(activePlayer).setPlayerTurn(false);
        Collections.rotate(mPlayers, rotateDistance);
        mPlayers.get(activePlayer).setPlayerTurn(true);
        playerAdapter.notifyDataSetChanged();
        scoreEditText.setText("");
        scoreEditText.setHint("Add " + mPlayers.get(0).getName() + "'s score");
    }

    public void endMatch(View view) {

        for (Player player : mPlayers) {
            player.setPlayerTurn(false);
            gameData.updatePlayerData(player);
        }
        gameData.addMatch(currentMatch);
        gameData.persistGameData();
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void saveMatch(View view) {
        gameData.addMatch(currentMatch);
        for (Player player : mPlayers) {
            gameData.updatePlayerData(player);
        }
        gameData.persistGameData();
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}
