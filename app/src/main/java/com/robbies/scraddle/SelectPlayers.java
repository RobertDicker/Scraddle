package com.robbies.scraddle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectPlayers extends AppCompatActivity implements SelectPlayerAdapter.OnPlayerListener, SelectPlayerAdapter.OnStartDragListener {


    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerViewAllPlayers;
    private ItemTouchHelper mItemTouchHelper;
    private SelectPlayerAdapter mAllPlayerAdapter;
    private ArrayList<Player> mCachedAllPlayers;
    private ArrayList<Player> mSelectedPlayers;
    private EditText editText;
    private GameViewModel gameViewModel;
    private Button addPlayerButton;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        addPlayerButton = findViewById(R.id.buttonAddPlayers);

        mSelectedPlayers = new ArrayList<>();
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        gameViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mCachedAllPlayers = (ArrayList) players;

                //mAllPlayerAdapter.setPlayers(mCachedAllPlayers);
                if (players.size() > 0) {
                    addPlayerButton.setVisibility(View.VISIBLE);

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


        mRecyclerViewAllPlayers = findViewById(R.id.recyclerviewPlayers);
        mAllPlayerAdapter = new SelectPlayerAdapter(mCachedAllPlayers, this, this);
        mRecyclerViewAllPlayers.setAdapter(mAllPlayerAdapter);
        mRecyclerViewAllPlayers.setLayoutManager(new LinearLayoutManager(this));


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
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//

                createPlayerPopup();

            }
        });

    }

    //TODO probably dont need this or maybe turn into a select primary players
    @Override
    public void onPlayerClick(int position) {

    }


    public void startScoring(View view) {


        if (mSelectedPlayers.size() > 0) {

            long matchId;
            matchId = gameViewModel.insertMatch(new Match());
            int order = 0;
            for (Player player : mSelectedPlayers) {
                gameViewModel.insertScore(new Score(player.getPlayerId(), matchId, order++));
            }


            Intent intent = new Intent(this, ScoringActivity.class);

            intent.putExtra("lastMatchId", matchId);

            startActivity(intent);

        }
    }

    private void createPlayerPopup() {


        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setSingleLine(true);

        editText.setHint("Enter Player Name");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create New Player")
                .setMessage("Enter new players name below")
                .setView(editText)

                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String editTextInput = editText.getText().toString().trim();


                        Player player = new Player(editTextInput);
                        //First two created players will automatically be added to every game

                        gameViewModel.insertPlayer(player);
                        mAllPlayerAdapter.setPlayers(mSelectedPlayers);


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }


    public void addPlayers(View view) {

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


        AlertDialog dialog = new AlertDialog.Builder(this).
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


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

        mItemTouchHelper.startDrag(viewHolder);
    }
}


