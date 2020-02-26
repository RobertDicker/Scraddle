package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.robbies.scraddle.Data.ScoringViewModel;
import com.robbies.scraddle.Utilities.FullScreenMode;
import com.robbies.scraddle.Utilities.ThemeChanger;

public class ScoringActivity extends AppCompatActivity implements FragmentSwitcher, NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenMode.hideToolBr(this);

        setContentView(R.layout.activity_scoring);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        setTheme(ThemeChanger.getThemeFromPreferences(getApplicationContext()));

        long matchId = getIntent().getLongExtra("lastMatchId", -1);

        if (savedInstanceState == null) {

            final ScoringViewModel scoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);
            scoringViewModel.setCurrentMatchId(matchId);

            Fragment matchFragment = matchId == -1 ?
                    SelectPlayerFragment.newInstance() : ScoringFragment.newInstance(matchId);

            ft.add(R.id.fragment,
                    matchFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void switchFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.fragment, fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
