package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeaderboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LeaderboardFragment extends Fragment {

    private GameViewModel gameViewModel;
    private List<Player> allPlayers;
    private OnFragmentInteractionListener mListener;

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_leader_board, container, false);
        final PlayerDetailAdapter playerDetailAdapter;
        RecyclerView recyclerView;
        allPlayers = new ArrayList<>();

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);


        recyclerView = view.findViewById(R.id.rVLeaderBoard);
        playerDetailAdapter = new PlayerDetailAdapter();
        recyclerView.setAdapter(playerDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
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
    public void onAttach(Context context) {
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View view);
    }


}
