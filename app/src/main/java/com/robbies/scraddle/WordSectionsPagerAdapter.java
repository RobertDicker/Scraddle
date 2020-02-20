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
public class WordSectionsPagerAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private List<Fragment> pages;
    private List<String> pagesTitles;


    public WordSectionsPagerAdapter(Context context, FragmentManager fm, Map<String, Fragment> pages) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;

        this.pages = new ArrayList<>();
        this.pages.addAll(pages.values());

        pagesTitles = new ArrayList<>();
        pagesTitles.addAll(pages.keySet());
    }

    @Override
    public Fragment getItem(@NonNull int position) {
        return pages.get(position);
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