package com.robbies.scraddle.WordComparators;

import android.util.Log;

import com.robbies.scraddle.Word;

import java.util.Comparator;

public class LexicographicComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return a.getWord().compareToIgnoreCase(b.getWord());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LexicographicComparator", "probable Null value word - check list");
        }
        return 0;
    }
}
