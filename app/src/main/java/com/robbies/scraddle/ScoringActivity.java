package com.robbies.scraddle;

import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class ScoringActivity extends AppCompatActivity implements SelectPlayerFragment.FragmentSwitcher {

    private long matchId;
    private EditText editText;
    private ScoringViewModel scoringViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoring);
        scoringViewModel = ViewModelProviders.of(this).get(ScoringViewModel.class);

        matchId = getIntent().getIntExtra("lastMatchId", -1);

        Fragment matchFragment = matchId == -1 ?
                new SelectPlayerFragment(this)
                : new ScoringFragment();

        // Get the FragmentManager and start a transaction.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment,
                matchFragment).addToBackStack(null).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                .replace(R.id.fragment, fragment).addToBackStack(null)
                .commit();

    }







}
