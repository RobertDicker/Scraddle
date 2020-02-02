package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activities that contain this fragment must implement the
 * {@link LeaderboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LeaderboardFragment extends Fragment {

    private List<Player> allPlayers;
    private OnFragmentInteractionListener mListener;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        final LeaderBoardAdapter playerDetailAdapter;
        RecyclerView recyclerView;
        allPlayers = new ArrayList<>();

        ScoringViewModel scoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);


        recyclerView = view.findViewById(R.id.rVLeaderBoard);
        playerDetailAdapter = new LeaderBoardAdapter();
        recyclerView.setAdapter(playerDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoringViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                allPlayers = players;
                playerDetailAdapter.setPlayers(allPlayers);

                Log.d("==players===", allPlayers.toString());

            }
        });


        return view;
    }


    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View view);
    }


}
