package com.robbies.scraddle.WordComparators;

import com.robbies.scraddle.WordData.Word;

import java.util.Comparator;

public class ScrabbleValueComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {

        try {
            return Integer.compare(Integer.parseInt(b.getScrabbleValue()), Integer.parseInt(a.getScrabbleValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
