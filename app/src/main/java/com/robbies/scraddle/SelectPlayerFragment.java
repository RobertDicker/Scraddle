package com.robbies.scraddle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private final FragmentSwitcher mFragmentSwitcher;
    private ItemTouchHelper mItemTouchHelper;
    private SelectPlayerAdapter mAllPlayerAdapter;
    private ArrayList<Player> mCachedAllPlayers;
    private ArrayList<Player> mSelectedPlayers;
    private SharedPreferences mSharedPreferences;
    private ScoringViewModel mScoringViewModel;

    SelectPlayerFragment(FragmentSwitcher fragmentSwitcher) {
        this.mFragmentSwitcher = fragmentSwitcher;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScoringViewModel = new ViewModelProvider(requireActivity()).get(ScoringViewModel.class);
        mAllPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this, this);

        mSelectedPlayers = new ArrayList<>();

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
//                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_players, container, false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        //Enable control of the toolbar within the fragment and not main activity
        setHasOptionsMenu(true);


        mScoringViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {


                mCachedAllPlayers = (ArrayList<Player>) players;
                if (!mCachedAllPlayers.isEmpty()) {
                    //Add the most recently modified player
                    mSelectedPlayers.add(mCachedAllPlayers.get(0));
                    // Make sure there is at least two players
                    if (mSelectedPlayers.size() < 2) {
                        mSelectedPlayers.add(mCachedAllPlayers.get(1));

                    }
                }


                mAllPlayerAdapter.setPlayers(mSelectedPlayers);


            }
        });

        view.findViewById(R.id.buttonStartScoring).setOnClickListener(this);

        RecyclerView recyclerViewAllPlayers = view.findViewById(R.id.recyclerviewPlayers);

        recyclerViewAllPlayers.setAdapter(mAllPlayerAdapter);
        recyclerViewAllPlayers.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemTouchHelper.attachToRecyclerView(recyclerViewAllPlayers);

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
            mFragmentSwitcher.switchFragment(new ScoringFragment());
        } else {
            Snackbar.make(requireView(), "Try Selecting Players, it's going to be lonely otherwise", Snackbar.LENGTH_SHORT)
                    .show();
        }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case (R.id.menuSelectPlayers):
                selectPlayers();
                break;
            case (R.id.menuCreatePlayer):
                createPlayerPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonStartScoring) startScoring();
    }

}


