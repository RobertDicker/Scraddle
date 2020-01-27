package com.robbies.scraddle;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private final int[] registeredMenuButtons = {R.id.buttonAnagramSolver, R.id.buttonStartNewGame, R.id.buttonLeaderBoard, R.id.buttonContinue, R.id.buttonSettings};
    private long lastMatch = -1;
    private FragmentSwitcher fragmentSwitcher;

    public MainMenuFragment(FragmentSwitcher fragmentSwitcher) {
        this.fragmentSwitcher = fragmentSwitcher;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);


        SharedPreferences sP = getContext().getSharedPreferences("Match", 0);
        lastMatch = sP.getLong("matchId", -1);

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
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonAnagramSolver:
                startActivity(new Intent(getContext(), AnagramChecker.class));
                break;
            case R.id.buttonStartNewGame:
                startActivity(new Intent(getContext(), SelectPlayers.class));
                break;
            case R.id.buttonLeaderBoard:
                //  startActivity(new Intent(getContext(), LeaderBoard.class));
                fragmentSwitcher.switchFragment(new LeaderboardFragment());
                break;
            case R.id.buttonContinue:
                Intent intent = new Intent(getContext(), ScoringActivity.class);
                intent.putExtra("lastMatchId", lastMatch);
                startActivity(intent);
                break;
            case R.id.buttonSettings:
                //  fragmentSwitcher.switchFragment(new SettingsFragment());
                break;

        }


    }


    public interface FragmentSwitcher {
        void switchFragment(Fragment fragment);
    }
}
