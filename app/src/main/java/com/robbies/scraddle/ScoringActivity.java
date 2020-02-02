package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class ScoringActivity extends AppCompatActivity implements FragmentSwitcher {


    private long matchId;
    private ScoringViewModel scoringViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoring);
        scoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);

        matchId = getIntent().getLongExtra("lastMatchId", -1);
        scoringViewModel.setCurrentMatchId(matchId);

        Fragment matchFragment = matchId == -1 ?
                new SelectPlayerFragment(this)
                : new ScoringFragment();

        // Get the FragmentManager and start a transaction.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment,
                matchFragment).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_players, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (matchId != -1) {
            menu.clear();
            getMenuInflater().inflate(R.menu.scoring_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void switchFragment(Fragment fragment) {
        matchId = scoringViewModel.getCurrentMatchId();
        invalidateOptionsMenu();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
