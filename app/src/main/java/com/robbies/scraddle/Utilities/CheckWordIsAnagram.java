package com.robbies.scraddle.Utilities;

import com.robbies.scraddle.WordData.Word;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class CheckWordIsAnagram implements Runnable {

    private Word word;
    private BigInteger anagramPrimeValue;
    private Map<Integer, List<Word>> allMatchingWords;

    public CheckWordIsAnagram(Word word, BigInteger anagramPrimeValue, Map<Integer, List<Word>> allMatchingWords) {
        this.word = word;
        this.anagramPrimeValue = anagramPrimeValue;
        this.allMatchingWords = allMatchingWords;
    }


    @Override
    public void run() {

        if (word != null) {
            BigInteger wordValue = new BigInteger(word.getPrimeValue());

            BigInteger result = anagramPrimeValue.mod(wordValue);
            int wordLength = word.getWord().length() < 8 ? word.getWord().length() : 8;
            if (result.equals(BigInteger.ZERO)) {

                if (allMatchingWords.containsKey(wordLength)) {
                    allMatchingWords.get(wordLength).add(word);
                }
            }
        }
    }
}
