package com.robbies.scraddle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements MainMenuFragment.FragmentSwitcher, LeaderboardFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;

    public static void setDayNightTheme(SharedPreferences prefs) {
        int currentTheme = prefs.getBoolean(Settings.KEY_PREF_NIGHT_MODE, false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(currentTheme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  SharedPreferences sP = getSharedPreferences("Match", 0);
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);

        setDayNightTheme(sP);

        setContentView(R.layout.activity_main_menu);

        // Instantiate the fragment.
        MainMenuFragment mainMenuFragment = new MainMenuFragment(this);
        // Get the FragmentManager and start a transaction.
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.content,
                mainMenuFragment).commit();
    }

    @Override
    public void switchFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStackImmediate();
        else super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(View view) {

    }


}
