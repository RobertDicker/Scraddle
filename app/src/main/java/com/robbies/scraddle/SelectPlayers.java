package com.robbies.scraddle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayers extends AppCompatActivity implements SelectPlayerAdapter.OnPlayerListener {


    private RecyclerView mRecyclerView;
    private SelectPlayerAdapter mSelectPlayerAdapter;

    private ArrayList<Player> mCachedAllPlayers;
    //private LinkedHashMap<Integer, Player> mSelectedPlayers;
    private List<Integer> mSelectedPlayerIds;
    private EditText editText;
    private GameViewModel gameViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSelectedPlayerIds = new ArrayList<>();


        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        gameViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mCachedAllPlayers = (ArrayList) players;

                mSelectPlayerAdapter.setPlayers(mCachedAllPlayers);
                Log.d("================", mCachedAllPlayers.toString());

            }
        });


        mRecyclerView = findViewById(R.id.recyclerviewPlayers);
        mSelectPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this);
        mRecyclerView.setAdapter(mSelectPlayerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPlayerPopup();

            }
        });


    }

    @Override
    public void onPlayerClick(int position) {

        View v = mRecyclerView.getLayoutManager().findViewByPosition(position);

        if (v != null) {
            int playerId = mCachedAllPlayers.get(position).getPlayerId();
            Log.d("--------------", playerId + "");

            if (mSelectedPlayerIds.contains(playerId)) {
                mSelectedPlayerIds.remove(playerId);

                v.findViewById(R.id.tVSelectPlayerTurnOrder).setVisibility(View.INVISIBLE);


            } else {
                mSelectedPlayerIds.add(playerId);
                v.findViewById(R.id.imageSelect).setVisibility(View.VISIBLE);
                TextView tv = v.findViewById(R.id.tVSelectPlayerTurnOrder);


            }
        }

    }

    public void startScoring(View view) {


        if (mSelectedPlayerIds.size() > 0) {

            long matchId;
            matchId = gameViewModel.insertMatch(new Match());
            int order = 0;
            for (int playerId : mSelectedPlayerIds) {
                gameViewModel.insertScore(new Score(playerId, matchId, order++));
            }


            Intent intent = new Intent(this, ScoringActivity.class);

       /*     while (matchId == -1) {
                try {
                    wait(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

            intent.putExtra("lastMatchId", matchId);

            startActivity(intent);

        }
    }

    private void createPlayerPopup() {


        editText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create New Player")
                .setMessage("Enter new players name below")
                .setView(editText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        editText.setHint("Enter Player Name");
                        String editTextInput = editText.getText().toString();
                        Log.d("onclick", "editext value is: " + editTextInput);

                        Player player = new Player(editTextInput);

                        gameViewModel.insertPlayer(player);

                        mSelectPlayerAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

}


