package com.robbies.scraddle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoringActivity extends AppCompatActivity implements PlayerListAdapter.OnPlayerListener {

    private static final int REVERT_PLAYER = -1;
    private static final int SAVE_PLAYER = 0;
    private static final int FINAL_SAVE = 1;
    private static final int REVERT_DATA = -2;
    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<GameDetail> playerDetails;
    private Match mCurrentMatch;
    private ScoringViewModel scoringViewModel;
    private GameDetail previousPlayer;
    private GameDetail currentPlayer;
    private ArrayList<GameDetail> playersDetailsBeforeMatch;
    private TextView tvCurrentPlayerTurn;
    private Button addToPlayerButton;
    private int incrementScore;
    private RecyclerView mRecyclerViewPlayers;
    private PlayerListAdapter playerAdapter;
    private long matchId;
    private int leaderHighScore;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_scoring);


        //ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Data
        matchId = getIntent().getLongExtra("lastMatchId", -1);
        sharedPreferences = getApplicationContext().getSharedPreferences("Match", 0);
        sharedPreferences.edit().putLong("matchId", matchId).apply();

        Log.d(TAG, "================MATCH ID =======>" + matchId);

        scoringViewModel = ViewModelProviders.of(this).get(ScoringViewModel.class);


        scoringViewModel.getPlayersDetails(matchId).observe(this, new Observer<List<GameDetail>>() {
            @Override
            public void onChanged(List<GameDetail> gameDetails) {
                playerDetails = (ArrayList) gameDetails;
                playerAdapter.setAllPlayers(playerDetails);
                Log.d(TAG, "================Player OBSERVER =======>\n playerDetails: " + playerDetails);

                //Make a copy of the
                if (playersDetailsBeforeMatch == null) {
                    playersDetailsBeforeMatch = (ArrayList) gameDetails;
                }


            }
        });

        scoringViewModel.getCurrentMatch().observe(this, new Observer<Match>() {
            @Override
            public void onChanged(Match match) {
                if (match != null) {
                    mCurrentMatch = match;
                    matchId = match.getMatchId();
                    scoringViewModel.getCurrentMatch().removeObserver(this);
                    Log.d(TAG, "================ Match OBSERVER =======>\n currentMatch: " + match);
                    Log.d(TAG, "================ MATCH =======>\n currentMatch: " + matchId);


                }
            }
        });

        //Display
        tvCurrentPlayerTurn = findViewById(R.id.textViewCurrentPlayerTurn);
        addToPlayerButton = findViewById(R.id.buttonAddToPlayer);

        mRecyclerViewPlayers = findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerListAdapter(playerDetails, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scoring_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case (R.id.menuButtonSave):
                returnToMain();
                break;
            case (R.id.menuButtonEndGame):
                endMatch();
                break;
            case (R.id.menuButtonDeleteMatch):
                deleteMatch();
                break;
            case (R.id.menuButtonUndo):
                undoLastMove();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void undoLastMove() {

        //Roll back turn and play order by 1
        //Roll back player
        saveData(REVERT_PLAYER);
        changePlayer(-1);
        currentPlayer = previousPlayer;
        //HIDE THE UNDO BUTTON
    }


    private void deleteMatch() {

        mRecyclerViewPlayers.setAdapter(null);
        saveData(REVERT_DATA);
        returnToMain();
    }


    public void onPlayerClick(View view) {

        TextView scoreTally = view.findViewById(R.id.tVScoreTally);
        ImageView bottomDivider = view.findViewById(R.id.scoreCardTallyDividerBottom);
        if (scoreTally.getVisibility() == View.GONE) {
            scoreTally.setVisibility(View.VISIBLE);
            bottomDivider.setVisibility(View.VISIBLE);

        } else {
            scoreTally.setVisibility(View.GONE);
            bottomDivider.setVisibility(View.GONE);
        }
    }


    public void addToPlayer(View view) {
        Log.d("===PLAYERS IN MATCH", playerDetails.toString() + "");

        currentPlayer = playerDetails.get(0);
        previousPlayer = new GameDetail(currentPlayer);


        //First Player is always 0 in the list.
        if (incrementScore > 0) {
            currentPlayer.addScore(incrementScore);

            //Check for score records
            if (currentPlayer.getPersonalBest() < incrementScore) {

                currentPlayer.setPersonalBest(incrementScore);

            }

            //Check current winners score
            int currentPlayersScore = currentPlayer.getTotalScore();
            leaderHighScore = currentPlayersScore > leaderHighScore ? currentPlayersScore : leaderHighScore;

        }

        // Next Player clear button text and shuffle list
        String tempButtonText = "SKIP";

        addToPlayerButton.setText(tempButtonText);
        incrementScore = 0;
        changePlayer(-1);
        saveData(SAVE_PLAYER);

        Log.d("PLAYERS IN MATCH AFTER", currentPlayer.toString() + "");


    }


    private void changePlayer(int rotateDistance) {

        playerDetails.get(0).setPlayersTurnOrder(playerDetails.get(0).getPlayersTurnOrder() + playerDetails.size());
        Collections.rotate(playerDetails, rotateDistance);

        //Set the title to show who's turn it is
        String turnString = playerDetails.get(0).getName() + "'s Turn";


        tvCurrentPlayerTurn.setText(turnString);

    }

    public void endMatch() {


        List<Integer> winners = determineWinner();
        String winnerText = "";
        for (int i = 0; i < playerDetails.size(); i++) {

            //Win
            if (winners.contains(playerDetails.get(i).getPlayerId()) & winners.size() == 1) {
                playerDetails.get(i).incrementWin();
                playerDetails.get(i).setResult(3);
                winnerText = "The Winner is " + playerDetails.get(i).getName() + "\nScore: " + playerDetails.get(i).getTotalScore();
            }
            //Draw
            else if (winners.contains(playerDetails.get(i).getPlayerId()) & winners.size() > 1) {
                playerDetails.get(i).incrementDraw();
                playerDetails.get(i).setResult(1);
                winnerText = "Its a Tie \nScore: " + playerDetails.get(i).getTotalScore();

            }
            //loss
            else {
                playerDetails.get(i).incrementLoss();
                playerDetails.get(i).setResult(-1);
            }

            if (playerDetails.get(i).getTotalScore() > playerDetails.get(i).getPlayersHighestMatchScore()) {
                playerDetails.get(i).setPlayersHighestMatchScore(playerDetails.get(i).getTotalScore());
            }

            saveData(FINAL_SAVE);

        }

        showEndMatchDialog(winnerText);


    }

    private void showEndMatchDialog(String endMatchText) {


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Winner")
                .setMessage(endMatchText).setIcon(R.drawable.ic_face_black_24dp)
                .setNeutralButton("To Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        returnToMain();
                    }
                })
                .create();
        dialog.show();


    }

    private void saveData(int method) {

        int i = 0;
        switch (method) {

            case (SAVE_PLAYER):
                scoringViewModel.savePlayer(new Player(currentPlayer));
                scoringViewModel.saveScore(new Score(currentPlayer));
                Log.d(TAG, "============SAVGING PLAYER ONLY===========");
                break;

            case (FINAL_SAVE):

                //Save once more to update their wins/loss status and results
                for (GameDetail player : playerDetails) {
                    scoringViewModel.saveScore(new Score(player));
                    scoringViewModel.savePlayer(new Player(player));
                }

                //Close the last match so it cannot be continued.
                sharedPreferences.edit().putLong("matchId", -1).apply();
                break;

            case (REVERT_PLAYER):
                scoringViewModel.savePlayer(new Player(previousPlayer));
                scoringViewModel.saveScore(new Score(previousPlayer));
                break;

            case (REVERT_DATA):

                sharedPreferences.edit().putLong("matchId", -1).apply();
                Log.d(TAG, "============REVERTING ALL =========== MatchID: " + -1);

                //Revert the player to their details before the match started
                for (GameDetail player : playersDetailsBeforeMatch) {
                    scoringViewModel.savePlayer(new Player(player));
                }

                //Delete the score and match rows from data
                scoringViewModel.deleteMatch(mCurrentMatch.getMatchId());
                Log.d(TAG, "============DELETING ALL =========== MatchID: " + matchId);
                break;

        }
    }


    private void returnToMain() {

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private List<Integer> determineWinner() {

        List winners = new ArrayList<>();

        for (GameDetail player : playerDetails) {
            if (player.getTotalScore() == leaderHighScore) {
                winners.add(player.getPlayerId());
            }

        }
        return winners;
    }


    public void increment(View view) {


        switch (view.getId()) {

            case R.id.buttonAddOne:
                incrementScore++;
                break;
            case R.id.buttonAddFive:
                incrementScore += 5;
                break;
            case R.id.buttonAddTen:
                incrementScore += 10;
                break;
            case R.id.buttonMinusOne:
                incrementScore = incrementScore > 0 ? incrementScore - 1 : 0;
                break;
            case R.id.buttonClear:
                incrementScore = 0;
                break;


        }

        addToPlayerButton.setText("Add\n" + incrementScore);

    }


}