package com.robbies.scraddle;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.Data.GameDetail;
import com.robbies.scraddle.Data.Player;
import com.robbies.scraddle.Data.Score;
import com.robbies.scraddle.Data.ScoringViewModel;
import com.robbies.scraddle.Utilities.TidyStringFormatterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoringFragment extends Fragment implements PlayerListAdapter.OnPlayerListener, View.OnClickListener {

    //CONSTANTS
    private static final int UNDO_PLAYER = -1;
    private static final int SAVE_PLAYER = 0;
    private static final int FINAL_SAVE = 1;
    private static final int REVERT_DATA = -2;
    private final int[] onClickRegisteredButtons = {R.id.buttonAddOne, R.id.buttonAddFive, R.id.buttonAddTen, R.id.buttonMinusOne, R.id.buttonClear, R.id.buttonAddToPlayer};

    //DATA
    private SharedPreferences sharedPreferences;
    private ScoringViewModel scoringViewModel;
    private ArrayList<GameDetail> playerDetails;
    private ArrayList<GameDetail> backupPlayerDetails;

    //Buttons
    private GameDetail currentPlayer;
    private int incrementScore;
    private long matchId;

    //View
    private TextView tvCurrentPlayerTurn;
    private Button addToPlayerButton;
    private RecyclerView mRecyclerViewPlayers;
    private PlayerListAdapter playerAdapter;


    public ScoringFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        scoringViewModel = new ViewModelProvider(requireActivity()).get(ScoringViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_scoring, container, false);

        //Enable control of the toolbar within the fragment and not main activity
        setHasOptionsMenu(true);

        matchId = scoringViewModel.getCurrentMatchId();
        Log.d("matchid", "===============================" + matchId);

        scoringViewModel.getPlayersDetails(matchId).observe(getViewLifecycleOwner(), new Observer<List<GameDetail>>() {
            @Override
            public void onChanged(List<GameDetail> gameDetails) {

                playerDetails = (ArrayList<GameDetail>) gameDetails;

                playerAdapter.setAllPlayers(playerDetails);

                //Sets the initial starting values of each player in order to revert transaction on cancelled match.
                if (backupPlayerDetails == null) {
                    backupPlayerDetails = new ArrayList<>();
                    //Create backups NOT REFERENCES
                    for (GameDetail gameDetail : gameDetails) {
                        backupPlayerDetails.add(gameDetail);
                    }
                }
            }
        });


        //Display
        tvCurrentPlayerTurn = view.findViewById(R.id.textViewCurrentPlayerTurn);
        addToPlayerButton = view.findViewById(R.id.buttonAddToPlayer);

        mRecyclerViewPlayers = view.findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerListAdapter(playerDetails, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));

        for (int button : onClickRegisteredButtons) {
            view.findViewById(button).setOnClickListener(this);
        }

        return view;
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

        if (backupPlayerDetails.size() > playerDetails.size()) {
            //Roll back turn and play order by 1
            //Roll back player
            //Revert turn order
            changePlayer(1);
            saveData(UNDO_PLAYER);

        }
    }


    private void deleteMatch() {

        mRecyclerViewPlayers.setAdapter(null);
        saveData(REVERT_DATA);
        returnToMain();
    }


    public void onPlayerClick(View view) {
        TextView scoreTally = view.findViewById(R.id.tVScoreTally);
        ImageView topDivider = view.findViewById(R.id.scoreCardTallyDividerTop);

        if (!scoreTally.getText().toString().isEmpty()) {

            int visibility = scoreTally.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;

            scoreTally.setVisibility(visibility);
            topDivider.setVisibility(visibility);
        }
    }


    private void addToPlayer() {

        currentPlayer = playerDetails.get(0);
        //Players state before they take a move
        backupPlayerDetails.add(new GameDetail(currentPlayer));

        //First Player is always 0 in the list.
        if (incrementScore > 0) {
            currentPlayer.addScore(incrementScore);

            //Check for score records
            if (currentPlayer.getPersonalBest() < incrementScore) {
                currentPlayer.setPersonalBest(incrementScore);
            }
        }

        // Next Player clear button text and shuffle list
        incrementScore = 0;
        changePlayer(-1);
        saveData(SAVE_PLAYER);
    }


    private void changePlayer(int rotateDistance) {

        if (rotateDistance == -1) {
            playerDetails.get(0).setPlayersTurnOrder(playerDetails.get(0).getPlayersTurnOrder() + playerDetails.size());
        }

        Collections.rotate(playerDetails, rotateDistance);

        //Set the title to show who's turn it is

        tvCurrentPlayerTurn.setText(String.format("%s's Turn", playerDetails.get(0).getName()));
    }

    private void endMatch() {

        List<Integer> winners = determineWinner();
        String winnerText = "";
        String winner = "";

        for (int i = 0; i < playerDetails.size(); i++) {

            //Win
            if (winners.contains(playerDetails.get(i).getPlayerId()) & winners.size() == 1) {
                playerDetails.get(i).incrementWin();
                playerDetails.get(i).setResult(3);
                winnerText = String.format("The winner is %s, with a score of %s", playerDetails.get(i).getName(), playerDetails.get(i).getTotalScore());
            }
            //Draw
            else if (winners.contains(playerDetails.get(i).getPlayerId()) & winners.size() > 1) {
                playerDetails.get(i).incrementDraw();
                playerDetails.get(i).setResult(1);
                winner = TidyStringFormatterHelper.addToItemStringWithCommasAnd(winner, playerDetails.get(i).getName());
                winnerText = String.format("Its a Tie between %s with a total\nScore: %s ", winner, playerDetails.get(i).getTotalScore());
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

        if (getContext() != null) {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Winner")
                    .setMessage(endMatchText).setIcon(R.drawable.ic_face_black_24dp)
                    .setNeutralButton("To Main Menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            returnToMain();
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == (KeyEvent.KEYCODE_BACK)) {
                        returnToMain();
                    }
                    return true;
                }
            });

            dialog.show();
        }

    }

    private void saveData(int method) {

        switch (method) {

            case (SAVE_PLAYER):
                scoringViewModel.savePlayer(new Player(currentPlayer));
                scoringViewModel.saveScore(new Score(currentPlayer));
                break;

            case (FINAL_SAVE):
                //Save once more to update their wins/loss status and results
                for (GameDetail player : playerDetails) {
                    scoringViewModel.saveScore(new Score(player));
                    scoringViewModel.savePlayer(new Player(player));
                }

                backupPlayerDetails = null;
                //Close the last match so it cannot be continued.
                sharedPreferences.edit().putLong("matchId", -1).apply();
                break;

            case (UNDO_PLAYER):
                scoringViewModel.savePlayer(new Player(backupPlayerDetails.get(backupPlayerDetails.size() - 1)));
                scoringViewModel.saveScore(new Score(backupPlayerDetails.get(backupPlayerDetails.size() - 1)));

                //Remove their last backup
                backupPlayerDetails.remove(backupPlayerDetails.size() - 1);
                break;

            case (REVERT_DATA):

                sharedPreferences.edit().putLong("matchId", -1).apply();

                //Revert the player to their details before the match started
                for (int i = 0; i < playerDetails.size(); i++) {
                    scoringViewModel.savePlayer(new Player(backupPlayerDetails.get(i)));
                }

                //Delete the score and match rows from data it will cascade
                scoringViewModel.deleteMatch(matchId);

                break;
        }
    }


    private void returnToMain() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private List<Integer> determineWinner() {
        int leaderHighScore = 0;
        List<Integer> winners = new ArrayList<>();

        for (GameDetail player : playerDetails) {
            if (player.getTotalScore() > leaderHighScore) {
                leaderHighScore = player.getTotalScore();
                winners.clear();
                winners.add(player.getPlayerId());
            } else if (player.getTotalScore() == leaderHighScore) {
                winners.add(player.getPlayerId());
            }
        }
        return winners;
    }


    @Override
    public void onClick(View view) {

        if (currentPlayer == null) {
            tvCurrentPlayerTurn.setText(String.format("%s's Turn", playerDetails.get(0).getName()));

        }
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
            case R.id.buttonAddToPlayer:
                addToPlayer();
        }

        String addAmountString = incrementScore == 0 ? "Skip" : String.format("Add\n%s", incrementScore);
        addToPlayerButton.setText(addAmountString);

    }
}