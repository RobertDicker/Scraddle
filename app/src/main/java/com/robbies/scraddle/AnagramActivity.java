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

public class AnagramActivity extends AppCompatActivity implements FragmentListener {

    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    String word = "wrong";
    WordViewModel wordViewModel;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        FullScreenMode.hideToolBr(this);
        mode = getIntent().getIntExtra("solveType",0);

        // The Fragments to load (Title, Fragment)
        final Map<String, Fragment> fragmentList = new LinkedHashMap<>();
        fragmentList.put("Word", WordEnterWordFragment.newInstance(mode));

        if(mode == 1){
           fragmentList.put("Solve", CrosswordFragment.newInstance("Demo"));
        }else{
        fragmentList.put("Solve", AnagramFragment.newInstance("TryAgain"));}

        setContentView(R.layout.activity_tabbed_word_solve);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragmentList);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(mode == 1){
                    sectionsPagerAdapter.updatePageValue("Solve", CrosswordFragment.newInstance(word));
                }else{
                sectionsPagerAdapter.updatePageValue("Solve", AnagramFragment.newInstance(word));}

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
        this.word = word;

    }

    @Override
    public void changePage(int position) {

        viewPager.setCurrentItem(position, true);


    }


}


