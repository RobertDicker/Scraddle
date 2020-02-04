package com.robbies.scraddle.WordComparators;

import com.robbies.scraddle.WordData.Word;

import java.util.Comparator;

public class LexicographicComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return a.getWord().compareToIgnoreCase(b.getWord());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
