package com.robbies.scraddle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.robbies.scraddle.Data.ScoringViewModel;
import com.robbies.scraddle.Utilities.FullScreenMode;

public class ScoringActivity extends AppCompatActivity {

    Fragment matchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenMode.hideToolBr(this);

        setContentView(R.layout.activity_scoring);

        long matchId = getIntent().getLongExtra("lastMatchId", -1);

        if (savedInstanceState == null) {

            final ScoringViewModel scoringViewModel = new ViewModelProvider(this).get(ScoringViewModel.class);
            scoringViewModel.setCurrentMatchId(matchId);

            matchFragment = matchId == -1 ?
                    new SelectPlayerFragment()
                    : new ScoringFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.fragment,
                    matchFragment).commit();
        }


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

        if (getSupportFragmentManager().getFragments().get(0) instanceof ScoringFragment) {
            menu.clear();
            getMenuInflater().inflate(R.menu.scoring_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
