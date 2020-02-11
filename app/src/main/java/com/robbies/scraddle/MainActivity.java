package com.robbies.scraddle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.robbies.scraddle.Utilities.FullScreenMode;

public class MainActivity extends AppCompatActivity {

    private final static String FRAGMENT_TAG_STRING = "MainMenu";
    private MainMenuFragment mainMenuFragment;

    private static void setDayNightTheme(SharedPreferences prefs) {
        int currentTheme = prefs.getBoolean(Settings.KEY_PREF_NIGHT_MODE, false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(currentTheme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenMode.hideToolBr(this);
        //Set The Navigation Bar to transparent===============================
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ===============================

        setContentView(R.layout.activity_scoring);


        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);
        setDayNightTheme(sP);
        setContentView(R.layout.activity_main_menu);


        if (savedInstanceState == null) {
            mainMenuFragment = new MainMenuFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content,
                    mainMenuFragment, FRAGMENT_TAG_STRING).commit();
        } else {
            mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_STRING);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }


}
