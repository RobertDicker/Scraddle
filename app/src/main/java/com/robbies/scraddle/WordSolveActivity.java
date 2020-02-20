package com.robbies.scraddle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.robbies.scraddle.Utilities.FullScreenMode;
import com.robbies.scraddle.Utilities.ZoomOutPageTransformer;
import com.robbies.scraddle.WordData.WordViewModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class WordSolveActivity extends AppCompatActivity implements FragmentListener {

    private ViewPager viewPager;
    private WordViewModel wordViewModel;
    private String letters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        FullScreenMode.hideToolBr(this);
        int mode = getIntent().getIntExtra("solveType", 0);

        // The Fragments to load (Title, Fragment)
        final Map<String, Fragment> fragmentList = new LinkedHashMap<>();
        fragmentList.put("Word", WordEnterWordFragment.newInstance(mode));

        if (mode == 1) {
            fragmentList.put("Solve", WordCrosswordFragment.newInstance());
        } else {
            fragmentList.put("Solve", WordAnagramFragment.newInstance());
        }

        setContentView(R.layout.activity_tabbed_word_solve);
        WordSectionsPagerAdapter sectionsPagerAdapter = new WordSectionsPagerAdapter(this, getSupportFragmentManager(), fragmentList);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    wordViewModel.setWord(letters);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateWord(String word) {
        this.letters = word;

    }

    @Override
    public void changePage(int position) {
        if (letters.length() > 1) {
            viewPager.setCurrentItem(position, true);
        }


    }


}


