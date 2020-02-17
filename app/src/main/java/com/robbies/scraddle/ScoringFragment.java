package com.robbies.scraddle;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.google.android.material.navigation.NavigationView;
import com.robbies.scraddle.Data.GameDetail;
import com.robbies.scraddle.Data.Player;
import com.robbies.scraddle.Data.PlayerRecord;
import com.robbies.scraddle.Data.Score;
import com.robbies.scraddle.Data.ScoringViewModel;
import com.robbies.scraddle.Utilities.TidyStringFormatterHelper;
import com.robbies.scraddle.Utilities.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ScoringFragment extends Fragment implements PlayerListAdapter.OnPlayerListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Timer.TimerUpdateListener {

    //CONSTANTS
    private static final int UNDO_PLAYER = -1;
    private static final int SAVE_PLAYER = 0;
    private static final int FINAL_SAVE = 1;
    private static final int REVERT_DATA = -2;
    private final int[] onClickRegisteredButtons = {R.id.buttonAddOne, R.id.buttonAddTwo, R.id.buttonAddThree, R.id.buttonAddFour, R.id.buttonAddFive, R.id.buttonAddSix, R.id.buttonAddSeven, R.id.buttonAddEight, R.id.buttonAddNine, R.id.buttonDeleteLast, R.id.buttonClear, R.id.buttonAddToPlayer};
    CountDownTimer countDownTimer;
    //DATA
    private SharedPreferences sharedPreferences;
    private ScoringViewModel scoringViewModel;
    private ArrayList<GameDetail> playerDetails;
    private ArrayList<GameDetail> backupPlayerDetails;
    private long timeRemaining;
    private boolean paused = true;
    private long countDownTime;


    //Buttons
    private GameDetail currentPlayer;
    private StringBuilder incrementScore;
    private long matchId;

    //View
    private TextView tvCurrentPlayerTurn;
    private Button addToPlayerButton;
    private RecyclerView mRecyclerViewPlayers;
    private PlayerListAdapter playerAdapter;
    private TextView timer;


    public static ScoringFragment newInstance(long matchId) {
        ScoringFragment fragment = new ScoringFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("matchId", matchId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        incrementScore = new StringBuilder();
        scoringViewModel = new ViewModelProvider(requireActivity()).get(ScoringViewModel.class);
        countDownTime = Long.parseLong(sharedPreferences.getString("timerLength", "6000"));


        if (getArguments() != null) {
            matchId = getArguments().getLong("matchId");
        }

        //Backup Turn data
        if (savedInstanceState != null) {
            backupPlayerDetails = savedInstanceState.getParcelableArrayList("backupPlayerTurnData");
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the backup turn data
        outState.putParcelableArrayList("backupPlayerTurnData", backupPlayerDetails);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.scoring_fragment_navigation_drawer_layout, container, false);

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
        timer = view.findViewById(R.id.textViewTimer);

        mRecyclerViewPlayers = view.findViewById(R.id.rVPlayers);
        playerAdapter = new PlayerListAdapter(playerDetails, this);
        mRecyclerViewPlayers.setAdapter(playerAdapter);

        //Handle screen Rotations
        if (requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        //TODO change this to seperate keyboard
        //Register the calculator buttons onclick methods
        for (int button : onClickRegisteredButtons) {
            view.findViewById(button).setOnClickListener(this);
        }

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paused = !paused;
                //Start the Game
                if (countDownTimer == null) {
                    if (currentPlayer == null) {
                        tvCurrentPlayerTurn.setText(String.format("%s's Turn", playerDetails.get(0).getName()));
                    }
                    startCountDown(countDownTime);
                }

                // Resume Timer
                else if (paused) {
                    startCountDown(timeRemaining);


                }
                // Pause the timer
                else {
                    countDownTimer.cancel();
                    tvCurrentPlayerTurn.setBackgroundColor(Color.parseColor("#FF9800"));
                    timer.setBackgroundColor(Color.parseColor("#FF9800"));
                    timer.setText(R.string.paused);
                }

            }
        });

        return view;
    }

    private void startCountDown(long time) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        tvCurrentPlayerTurn.setBackgroundColor(Color.parseColor("#00BCD4"));
        timer.setBackgroundColor(Color.parseColor("#00BCD4"));
        countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timer.setText(String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds));
                timeRemaining = millisUntilFinished;
            }

            public void onFinish() {
                timer.setText("0:00");
                timer.setBackgroundColor(Color.RED);
                tvCurrentPlayerTurn.setText("TIMES UP!");
                tvCurrentPlayerTurn.setBackgroundColor(Color.RED);

            }
        }.start();
    }


    private void checkWord() {
        CheckWord dialog = CheckWord.newInstance();
        dialog.show(requireActivity().getSupportFragmentManager(), "wordCheck");


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
        new AlertDialog.Builder(requireContext()).setTitle("Do you wish to delete this match?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData(REVERT_DATA);
                returnToMain();
            }
        }).setNegativeButton("Oops, No!", null).show();

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


    private void addToPlayer(int scoreToAdd) {

        currentPlayer = playerDetails.get(0);
        //Players state before they take a move
        backupPlayerDetails.add(new GameDetail(currentPlayer));

        //First Player is always 0 in the list.
        currentPlayer.addScore(scoreToAdd);

        //Check for score records
        if (currentPlayer.getPersonalBest() < scoreToAdd) {
            currentPlayer.setPersonalBest(scoreToAdd);
        }
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

        List<Integer> winnersIndex = getIndexOfWinners();
        String winnerText = "";
        String winner = "";

        for (int i = 0; i < playerDetails.size(); i++) {

            //Win
            if (winnersIndex.contains(i) & winnersIndex.size() == 1) {
                playerDetails.get(i).incrementWin();
                playerDetails.get(i).setResult(3);
                winnerText = String.format("The winner is %s, with a score of %s", playerDetails.get(i).getName(), playerDetails.get(i).getTotalScore());
            }
            //Draw
            else if (winnersIndex.contains(i) & winnersIndex.size() > 1) {
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
                scoringViewModel.savePlayerRecord(new PlayerRecord(currentPlayer));
                break;

            case (FINAL_SAVE):
                //Save once more to update their wins/loss status and results
                for (GameDetail player : playerDetails) {
                    scoringViewModel.saveScore(new Score(player));
                    scoringViewModel.savePlayer(new Player(player));
                    scoringViewModel.savePlayerRecord(new PlayerRecord(player));
                }

                backupPlayerDetails = null;
                //Close the last match so it cannot be continued.
                sharedPreferences.edit().putLong("matchId", -1).apply();
                break;

            case (UNDO_PLAYER):
                scoringViewModel.savePlayer(new Player(backupPlayerDetails.get(backupPlayerDetails.size() - 1)));
                scoringViewModel.saveScore(new Score(backupPlayerDetails.get(backupPlayerDetails.size() - 1)));
                scoringViewModel.savePlayerRecord(new PlayerRecord(backupPlayerDetails.get(backupPlayerDetails.size() - 1)));
                //Remove their last backup
                backupPlayerDetails.remove(backupPlayerDetails.size() - 1);
                break;

            case (REVERT_DATA):

                sharedPreferences.edit().putLong("matchId", -1).apply();

                //Revert the player to their details before the match started
                for (int i = 0; i < playerDetails.size(); i++) {
                    scoringViewModel.savePlayer(new Player(backupPlayerDetails.get(i)));
                    scoringViewModel.savePlayerRecord(new PlayerRecord(backupPlayerDetails.get(i)));
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


    private List<Integer> getIndexOfWinners() {
        int leaderHighScore = 0;
        List<Integer> winners = new ArrayList<>();

        for (GameDetail player : playerDetails) {

            // New Winner Scenario
            if (player.getTotalScore() > leaderHighScore) {
                leaderHighScore = player.getTotalScore();
                winners.clear();
                winners.add(playerDetails.indexOf(player));

                // Draw Scenario
            } else if (player.getTotalScore() == leaderHighScore) {
                winners.add(playerDetails.indexOf(player));
            }
        }
        return winners;
    }

    // Custom Keyboard
    @Override
    public void onClick(View view) {

        Button buttonClicked = (Button) view;

        switch (view.getId()) {

            case R.id.buttonClear:
                incrementScore.setLength(0);
                break;
            case R.id.buttonDeleteLast:
                if (incrementScore.length() > 0) {
                    incrementScore.deleteCharAt(incrementScore.length() - 1);
                }
                break;
            case R.id.buttonAddToPlayer:
                if (incrementScore.length() == 0) {
                    incrementScore.append("0");
                }

                addToPlayer(Integer.parseInt(incrementScore.toString()));
                // Next Player clear button text and shuffle list
                incrementScore.setLength(0);
                changePlayer(-1);
                saveData(SAVE_PLAYER);
                startCountDown(countDownTime);


                break;
            default:
                if (incrementScore.length() < 3) {
                    incrementScore.append(buttonClicked.getText().toString());
                }
        }

        String addAmountString;
        if (requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            addAmountString = incrementScore.length() == 0 ? "Skip" : String.format("Add\n%s", incrementScore);
        } else {
            addAmountString = incrementScore.length() == 0 ? "Skip" : String.format("Add %s", incrementScore);
        }
        addToPlayerButton.setText(addAmountString);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case (R.id.nav_home):
                returnToMain();
                break;
            case (R.id.nav_finish):
                endMatch();
                break;
            case (R.id.nav_checkWord):
                checkWord();
            case (R.id.nav_undo):
                undoLastMove();
                break;
            case (R.id.nav_delete_match):
                deleteMatch();
                break;
        }
        return false;
    }

    @Override
    public void updateTime(String time) {
        timer.setText(time);
    }

    @Override
    public void notifyTimeUp() {

    }
}