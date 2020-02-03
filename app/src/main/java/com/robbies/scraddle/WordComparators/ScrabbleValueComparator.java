package com.robbies.scraddle.WordComparators;

import android.util.Log;

import com.robbies.scraddle.Word;

import java.util.Comparator;

public class ScrabbleValueComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return Integer.compare(Integer.parseInt(b.getScrabbleValue()), Integer.parseInt(a.getScrabbleValue()));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ScrabbleValueComparator", "probable Null value word - check list");
        }
        return 0;
    }
}
