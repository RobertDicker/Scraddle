package com.robbies.scraddle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.robbies.scraddle.Data.Match;
import com.robbies.scraddle.Data.Player;
import com.robbies.scraddle.Data.Score;
import com.robbies.scraddle.Data.ScoringViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectPlayerFragment extends Fragment implements SelectPlayerAdapter.OnPlayerListener, SelectPlayerAdapter.OnStartDragListener, View.OnClickListener, SelectPlayerDialog.SelectPlayerDialogOnClickListener {


    private ItemTouchHelper mItemTouchHelper;
    private SelectPlayerAdapter mAllPlayerAdapter;
    private ArrayList<Player> mCachedAllPlayers;
    private ArrayList<Player> mSelectedPlayers;
    private SharedPreferences mSharedPreferences;
    private ScoringViewModel mScoringViewModel;
    private FragmentSwitcher mListener;


    static SelectPlayerFragment newInstance() {
        return new SelectPlayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScoringViewModel = new ViewModelProvider(requireActivity()).get(ScoringViewModel.class);
        mAllPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this, this);

        mSelectedPlayers = new ArrayList<>();

        if (savedInstanceState != null) {
            mSelectedPlayers = savedInstanceState.getParcelableArrayList("players");
            mAllPlayerAdapter.setPlayers(mSelectedPlayers);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());


        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(mSelectedPlayers, from, to);
                mAllPlayerAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int from = viewHolder.getAdapterPosition();
                mSelectedPlayers.remove(from);
                mAllPlayerAdapter.notifyItemRemoved(from);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_players, container, false);

        //Enable control of the toolbar within the fragment and not main activity
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            mScoringViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
                @Override
                public void onChanged(List<Player> players) {

                    mCachedAllPlayers = (ArrayList<Player>) players;

                    if (!mCachedAllPlayers.isEmpty()) {
                        // Add the most recently modified player
                        mSelectedPlayers.add(mCachedAllPlayers.get(0));

                        // Make sure there is at least two players by default
                        if (mCachedAllPlayers.size() >= 2 & mSelectedPlayers.size() < 2) {
                            mSelectedPlayers.add(mCachedAllPlayers.get(1));
                        }
                    }

                    mAllPlayerAdapter.setPlayers(mSelectedPlayers);
                }
            });
        } else {

            mScoringViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
                @Override
                public void onChanged(List<Player> players) {
                    mCachedAllPlayers = (ArrayList<Player>) players;

                    if (!mCachedAllPlayers.isEmpty()) {

                        if (!mSelectedPlayers.contains(mCachedAllPlayers.get(0)))
                            // Add the most recently modified player
                            mSelectedPlayers.add(mCachedAllPlayers.get(0));
                    }
                    mAllPlayerAdapter.setPlayers(mSelectedPlayers);
                }
            });
        }

        view.findViewById(R.id.buttonStartScoring).setOnClickListener(this);

        RecyclerView recyclerViewAllPlayers = view.findViewById(R.id.recyclerviewPlayers);

        recyclerViewAllPlayers.setAdapter(mAllPlayerAdapter);
        recyclerViewAllPlayers.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemTouchHelper.attachToRecyclerView(recyclerViewAllPlayers);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case (R.id.menuSelectPlayers):
                        selectPlayers();
                        break;
                    case (R.id.menuCreatePlayer):
                        createPlayerPopup();
                        break;
                }
                return false;
            }
        });


        return view;
    }


    @Override
    public void onPlayerClick(int position) {
        //reserved for future actions
    }


    private void startScoring() {

        long matchId = mScoringViewModel.insertMatch(new Match());

        if (mSelectedPlayers.size() > 0) {

            int order = 0;
            for (Player player : mSelectedPlayers) {

                mScoringViewModel.saveScore(new Score(player.getPlayerId(), matchId, order++));
            }

            mSharedPreferences.edit().putLong("matchId", matchId).apply();
            mListener.switchFragment(ScoringFragment.newInstance(matchId));
        } else {
            Snackbar.make(requireView(), "Try Selecting Players, it's going to be lonely otherwise", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("players", mSelectedPlayers);
    }

    private void selectPlayers() {

        int numberOfPlayers = mCachedAllPlayers.size();
        if (numberOfPlayers > 0) {
            String[] namesOfPlayers = new String[numberOfPlayers];
            boolean[] checkedItems = new boolean[numberOfPlayers];
            for (int i = 0; i < numberOfPlayers; i++) {
                namesOfPlayers[i] = mCachedAllPlayers.get(i).getName();
                checkedItems[i] = mSelectedPlayers.contains(mCachedAllPlayers.get(i));
            }

            DialogFragment dialogFragment = SelectPlayerDialog.getInstance("Select Players", namesOfPlayers, checkedItems, this);
            dialogFragment.show(requireActivity().getSupportFragmentManager(), "selectPlayer");

        } else {
            Snackbar.make(requireView(), "Try Create a Player First with the icon to the right", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public void doPositiveClick(boolean[] checkedItems) {
        mSelectedPlayers.clear();
        int playerIndex = 0;
        for (boolean item : checkedItems) {
            if (item) {
                mSelectedPlayers.add(mCachedAllPlayers.get(playerIndex));
            }
            playerIndex++;
        }
        mAllPlayerAdapter.setPlayers(mSelectedPlayers);
    }


    private void createPlayerPopup() {

        DialogFragment popup = new CreatePlayerDialog();
        popup.show(requireActivity().getSupportFragmentManager(), "createPlayer");
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonStartScoring) startScoring();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSwitcher) {
            mListener = (FragmentSwitcher) context;
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

}


