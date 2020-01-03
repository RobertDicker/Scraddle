package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SelectPlayers extends AppCompatActivity implements SelectPlayerAdapter.OnPlayerListener {


    private RecyclerView mRecyclerView;
    private SelectPlayerAdapter mSelectPlayerAdapter;
    private GameDataAdapter gameData;
    private ArrayList<Player> mCachedAllPlayers;
    private LinkedHashMap<String, Player> mSelectedPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameData = new GameDataAdapter();
        mCachedAllPlayers = gameData.getAllPlayers();
        mSelectedPlayers = new LinkedHashMap<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mRecyclerView = findViewById(R.id.recyclerviewPlayers);
        mSelectPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this);
        mRecyclerView.setAdapter(mSelectPlayerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onPlayerClick(int position) {
        View v = mRecyclerView.getLayoutManager().findViewByPosition(position);
        String playerId = mCachedAllPlayers.get(position).getPlayerID();
        Log.d("--------------", playerId);

        if (mSelectedPlayers.containsKey(playerId)) {
            mSelectedPlayers.remove(playerId);
            v.findViewById(R.id.imageSelect).setVisibility(View.INVISIBLE);


        } else {
            mSelectedPlayers.put(playerId, mCachedAllPlayers.get(position));
            v.findViewById(R.id.imageSelect).setVisibility(View.VISIBLE);

        }


    }

    public void startScoring(View view) {

        ArrayList<String> mSelectedPlayerIds = new ArrayList<String>(mSelectedPlayers.keySet());

        if (mSelectedPlayerIds.size() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putStringArrayListExtra("selectedPlayersIds", mSelectedPlayerIds);

            startActivity(intent);
        }
}
}
