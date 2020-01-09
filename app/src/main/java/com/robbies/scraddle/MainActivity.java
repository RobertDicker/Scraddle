package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    int increment = 0;
    TextView currentPlayerTurn;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar

        setContentView(R.layout.activity_main);


        gameData = new GameDataAdapter(getApplicationContext());
        initialiseMatch(getIntent().getIntExtra("gameMode", -1));
        currentPlayerTurn = findViewById(R.id.textViewCurrentPlayerTurn);
        button = findViewById(R.id.buttonAddToPlayer);


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

        //Set active player to the current selected player and rotate to the top of screen.

        changePlayer(mPlayers.size() - position);
    }


    public void addToPlayer(View view) {


        Player currentPlayer = mPlayers.get(activePlayer);

        if (increment > 0) {
            currentMatch.addScoreToPlayer(currentPlayer.getPlayerID(), increment);

            //Check for score records
            if (increment > currentPlayer.getPersonalBest()) {
                currentPlayer.setPersonalBest(increment);
            }
            // Next Player
            increment = 0;
            button.setText("Add\n" + increment);
            changePlayer(-1);
        }
    }

    private void changePlayer(int rotateDistance) {

        mPlayers.get(activePlayer).setPlayerTurn(false);
        Collections.rotate(mPlayers, rotateDistance);
        mPlayers.get(activePlayer).setPlayerTurn(true);
        playerAdapter.notifyDataSetChanged();

        //Set the title to show who's turn it is
        String turnString = mPlayers.get(0).getName() + "'s Turn";


        currentPlayerTurn.setText(turnString);
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

    public void increment(View view) {


        switch (view.getId()) {

            case R.id.buttonAddOne:
                increment++;
                break;
            case R.id.buttonAddFive:
                increment += 5;
                break;
            case R.id.buttonAddTen:
                increment += 10;
            case R.id.buttonMinusOne:
                increment = increment > 0 ? increment - 1 : 0;
                break;
            case R.id.buttonClear:
                increment = 0;
                break;


        }

        button.setText("Add\n" + increment);

    }


}
