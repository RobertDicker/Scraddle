package com.robbies.scraddle;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {


    private final Context mContext;
    private String word;
    private List<Fragment> pages;
    private List<String> pagesTitles;


    public SectionsPagerAdapter(Context context, FragmentManager fm, Map<String, Fragment> pages) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        word = "";

        this.pages = new ArrayList<>();
        this.pages.addAll(pages.values());

        pagesTitles = new ArrayList<>();
        pagesTitles.addAll(pages.keySet());
    }


    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

/*    public void setWord(String word){
        this.word = word;

    }*/

    public void updatePageValue(String key, Fragment fragment) {
        int position = pagesTitles.indexOf(key);
        pages.set(position, fragment);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitles.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return pages.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;

    }
}