package com.robbies.scraddle;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;


/**
 * A simple {@link Fragment} subclass.
 */
public final class MainMenuFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private final int[] registeredMenuButtons = {R.id.buttonAnagramSolver, R.id.buttonStartNewGame, R.id.buttonLeaderBoard, R.id.buttonContinue, R.id.buttonSettings, R.id.buttonCrossword};
    private long lastMatch = -1;
    private SharedPreferences sP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sP = PreferenceManager.getDefaultSharedPreferences(requireContext());
        lastMatch = sP.getLong("matchId", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        setRetainInstance(true);

        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
        setDayNightMode();

        //Alter Continue button if there are no games to continue
        if (lastMatch == -1) {
            view.findViewById(R.id.buttonContinue).setVisibility(View.GONE);
        }

        for (int button : registeredMenuButtons) {
            view.findViewById(button).setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        sP.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {


            case R.id.buttonAnagramSolver:
                intent = new Intent(getContext(), WordSolveActivity.class);
                intent.putExtra("solveType", 0);
                startActivity(intent);
                break;
            case R.id.buttonStartNewGame:
                startActivity(new Intent(getContext(), ScoringActivity.class));
                break;
            case R.id.buttonLeaderBoard:
                requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, new LeaderboardFragment()).addToBackStack(null).commit();
                break;
            case R.id.buttonContinue:
                intent = new Intent(getContext(), ScoringActivity.class);
                intent.putExtra("lastMatchId", lastMatch);
                startActivity(intent);
                break;
            case R.id.buttonSettings:
                requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, new Settings()).addToBackStack(null).commit();
                sP.registerOnSharedPreferenceChangeListener(this);
                break;
            case R.id.buttonCrossword:
                intent = new Intent(getContext(), WordSolveActivity.class);
                intent.putExtra("solveType", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        lastMatch = sP.getLong("matchId", -1);

        //Alter Continue button if there are no games to continue
        if (lastMatch == -1) {
            if (getView() != null) {
                getView().findViewById(R.id.buttonContinue).setVisibility(View.GONE);
            }
        }

        if (s.equals(Settings.KEY_PREF_NIGHT_MODE)) {
            setDayNightMode();
        }
    }

    private void setDayNightMode() {
        int currentTheme = sP.getBoolean(Settings.KEY_PREF_NIGHT_MODE, false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(currentTheme);
    }

}
