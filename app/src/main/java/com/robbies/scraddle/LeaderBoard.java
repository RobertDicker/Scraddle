package com.robbies.scraddle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity implements PlayerDetailAdapter.OnPlayerListener {


    private GameViewModel gameViewModel;
    private List<Player> allPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        final PlayerDetailAdapter playerDetailAdapter;
        RecyclerView recyclerView;
        allPlayers = new ArrayList<>();

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);


        recyclerView = findViewById(R.id.rVLeaderBoard);
        playerDetailAdapter = new PlayerDetailAdapter(this);
        recyclerView.setAdapter(playerDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                allPlayers = players;
                playerDetailAdapter.setPlayers(allPlayers);

                Log.d("==players===", allPlayers.toString());

            }
        });


    }


    @Override
    public void onPlayerClick(int position) {
        Log.d("======", "Clicked player at " + position);


    }
}
