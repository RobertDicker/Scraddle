package com.robbies.scraddle.WordComparators;

import com.robbies.scraddle.WordData.Word;

import java.util.Comparator;

public class LengthComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        if (a == null) {
            if (b == null) {
                return 0;
            }
            return -1;
        } else if (b == null) {
            return 1;

        }


        return Integer.compare(a.getWord().length(), b.getWord().length());

    }
}
