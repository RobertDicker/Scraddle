package com.robbies.scraddle;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.robbies.scraddle.Data.ScoringViewModel;

public class ScoringActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG_STRING = "ScoringActivity";
    private long matchId;


    Fragment matchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scoring);

        matchId = getIntent().getLongExtra("lastMatchId", -1);

        if(savedInstanceState == null){

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
