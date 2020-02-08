package com.robbies.scraddle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.Data.PlayersAndRecords;
import com.robbies.scraddle.Data.ScoringViewModel;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private List<PlayersAndRecords> allPlayers;
    private LeaderBoardAdapter playerDetailAdapter;
    private ScoringViewModel scoringViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerDetailAdapter = new LeaderBoardAdapter();
        allPlayers = new ArrayList<>();
        scoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rVLeaderBoard);

        recyclerView.setAdapter(playerDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoringViewModel.getAllPlayersAndRecords().observe(getViewLifecycleOwner(), new Observer<List<PlayersAndRecords>>() {
            @Override
            public void onChanged(List<PlayersAndRecords> players) {
                allPlayers = players;
                playerDetailAdapter.setPlayers(allPlayers);
            }
        });

        return view;
    }
}
