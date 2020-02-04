package com.robbies.scraddle.WordComparators;

import com.robbies.scraddle.WordData.Word;

import java.util.Comparator;

public class LengthComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return Integer.compare(a.getWord().length(), b.getWord().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
