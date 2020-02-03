package com.robbies.scraddle.WordComparators;

import android.util.Log;

import com.robbies.scraddle.Word;

import java.util.Comparator;

public class LengthComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return Integer.compare(a.getWord().length(), b.getWord().length());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LengthComparator", "probable Null value word - check list");
        }
        return 0;
    }
}
