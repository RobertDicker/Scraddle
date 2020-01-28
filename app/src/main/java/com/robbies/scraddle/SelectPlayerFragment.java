package com.robbies.scraddle;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SelectPlayerFragment extends Fragment implements SelectPlayerAdapter.OnPlayerListener, SelectPlayerAdapter.OnStartDragListener, View.OnClickListener {


    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerViewAllPlayers;
    private ItemTouchHelper mItemTouchHelper;
    private SelectPlayerAdapter mAllPlayerAdapter;
    private ArrayList<Player> mCachedAllPlayers;
    private ArrayList<Player> mSelectedPlayers;
    private EditText editText;
    private Button addPlayerButton;
    private SharedPreferences sharedPreferences;
    private FragmentSwitcher fragmentSwitcher;
    private ScoringViewModel scoringViewModel;


    public SelectPlayerFragment(FragmentSwitcher fragmentSwitcher) {
        this.fragmentSwitcher = fragmentSwitcher;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_players, container, false);
        sharedPreferences = getContext().getSharedPreferences("Match", 0);

        //Enable control of the toolbar within the fragment and not main activity
        setHasOptionsMenu(true);

        scoringViewModel = ViewModelProviders.of(getActivity()).get(ScoringViewModel.class);

        //addPlayerButton = view.findViewById(R.id.buttonAddPlayers);

        mSelectedPlayers = new ArrayList<>();

        scoringViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mCachedAllPlayers = (ArrayList) players;

                //mAllPlayerAdapter.setPlayers(mCachedAllPlayers);
                if (players.size() > 0) {
                    //addPlayerButton.setVisibility(View.VISIBLE);

                    //Add 1-2 players
                    if (mSelectedPlayers.isEmpty()) {
                        for (int i = 0; i < mCachedAllPlayers.size() && i < 2; i++) {
                            mSelectedPlayers.add(mCachedAllPlayers.get(i));
                        }
                    }

                    mAllPlayerAdapter.setPlayers(mSelectedPlayers);
                }
            }
        });

        view.findViewById(R.id.buttonStartScoring).setOnClickListener(this);

        mRecyclerViewAllPlayers = view.findViewById(R.id.recyclerviewPlayers);
        mAllPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this, this);
        mRecyclerViewAllPlayers.setAdapter(mAllPlayerAdapter);
        mRecyclerViewAllPlayers.setLayoutManager(new LinearLayoutManager(getContext()));

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

        mItemTouchHelper.attachToRecyclerView(mRecyclerViewAllPlayers);

        return view;

    }


    @Override
    public void onPlayerClick(int position) {
    }


    public void startScoring() {

        if (mSelectedPlayers.size() > 0) {

            long matchId = scoringViewModel.getCurrentMatchId();
            int order = 0;
            for (Player player : mSelectedPlayers) {
                scoringViewModel.saveScore(new Score(player.getPlayerId(), matchId, order++));
            }

            sharedPreferences.edit().putLong("matchId", matchId).apply();

            fragmentSwitcher.switchFragment(new ScoringFragment());

        }
    }

    public void addPlayers() {

        final List<Integer> selectedItems = new ArrayList<>();
        String[] items = new String[mCachedAllPlayers.size()];
        boolean[] checkedItems = new boolean[mCachedAllPlayers.size()];
        int i = 0;
        for (Player player : mCachedAllPlayers) {
            items[i] = player.getName();
            if (mSelectedPlayers.contains(player)) {
                checkedItems[i] = true;
                selectedItems.add(i);
            }
            checkedItems[i] = mSelectedPlayers.contains(player);
            i++;
        }


        AlertDialog dialog = new AlertDialog.Builder(getContext()).
                setTitle("Select Your Players").setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {

                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    selectedItems.add(index);
                } else if (selectedItems.contains(index)) {
                    // Else, if the item is already in the array, remove it
                    selectedItems.remove(Integer.valueOf(index));
                }

            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int buttonId) {
                        mSelectedPlayers.clear();
                        for (Integer i : selectedItems) {
                            mSelectedPlayers.add(mCachedAllPlayers.get(i));
                        }
                        mAllPlayerAdapter.setPlayers(mSelectedPlayers);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();


    }

    private void createPlayerPopup() {


        editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setSingleLine(true);

        editText.setHint("Enter Player Name");
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Create New Player")
                .setMessage("Enter new players name below")
                .setView(editText)

                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String editTextInput = editText.getText().toString().trim();


                        Player player = new Player(editTextInput);
                        //First two created players will automatically be added to every game

                        scoringViewModel.savePlayer(player);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case (R.id.menuSelectPlayers):
                addPlayers();
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

        if (view.getId() == R.id.buttonStartScoring) {
            startScoring();
        }

    }

    public interface FragmentSwitcher {
        void switchFragment(Fragment fragment);
    }

}


