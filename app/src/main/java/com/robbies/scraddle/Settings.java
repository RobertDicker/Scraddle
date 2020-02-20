package com.robbies.scraddle;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.robbies.scraddle.Data.Player;
import com.robbies.scraddle.Data.ScoringViewModel;
import com.robbies.scraddle.Utilities.TidyStringFormatterHelper;

import java.util.List;

public class Settings extends PreferenceFragmentCompat implements SelectPlayerDialog.SelectPlayerDialogOnClickListener {

    static final String KEY_PREF_NIGHT_MODE = "switchNightMode";
    private ScoringViewModel mScoringViewModel;
    private List<Player> mAllPlayers;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
        mScoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);

        mScoringViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mAllPlayers = players;
                boolean visibility = players.size() > 0;

                Preference deletePlayerPreference = findPreference("deletePlayer");
                if (deletePlayerPreference != null) {
                    deletePlayerPreference.setVisible(visibility);
                }
            }
        });


        Preference deleteAllPlayersButton = findPreference("deleteAll");
        if (deleteAllPlayersButton != null) {
            deleteAllPlayersButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Are you sure you wish to Delete all user Data")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mScoringViewModel.deleteAll();
                                    sharedPreferences.edit().clear().apply();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(getContext(), "All User Data Cleared", Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("Oops No", null)
                            .create();

                    builder.show();


                    return true;
                }
            });
        }

        Preference createPlayerButton = findPreference(getString(R.string.createPlayer));
        if (createPlayerButton != null) {
            createPlayerButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogFragment popup = new CreatePlayerDialog();
                    popup.show(requireActivity().getSupportFragmentManager(), "createPlayer");
                    return true;
                }
            });
        }


        Preference deletePlayersButton = findPreference("deletePlayer");
        if (deletePlayersButton != null) {
            deletePlayersButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    popupDeleteMultiSelectMenu();
                    return true;
                }
            });
        }
    }

    private void popupDeleteMultiSelectMenu() {
        String[] namesOfPlayers = new String[mAllPlayers.size()];
        boolean[] checkedItems = new boolean[mAllPlayers.size()];
        for (int i = 0; i < mAllPlayers.size(); i++) {
            namesOfPlayers[i] = mAllPlayers.get(i).getName();
            checkedItems[i] = false;
        }

        DialogFragment dialogFragment = SelectPlayerDialog.getInstance("Delete Selected Players", namesOfPlayers, checkedItems, this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "selectPlayer");

    }

    @Override
    public void doPositiveClick(final boolean[] checkedItems) {
        String playersToDelete = "";
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                playersToDelete = TidyStringFormatterHelper.addToItemStringWithCommasAnd(playersToDelete, mAllPlayers.get(i).getName());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(String.format("Are you sure you wish to Delete %s?", playersToDelete))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = 0; i < checkedItems.length; i++) {

                            if (checkedItems[i]) {
                                mScoringViewModel.deletePlayer(mAllPlayers.get(i).getPlayerId());
                            }
                        }
                        sharedPreferences.edit().putLong("matchId", -1).apply();
                    }
                })
                .setNegativeButton("Oops, No", null)
                .create();
        builder.show();
    }


}
