package com.robbies.scraddle.Utilities;

import com.robbies.scraddle.WordData.Word;

import java.math.BigInteger;
import java.util.Map;

public class CheckWordIsAnagram implements Runnable {

    private Word word;
    private BigInteger anagramPrimeValue;
    private Map<Word, Integer> allMatchingWords;

    public CheckWordIsAnagram(Word word, BigInteger anagramPrimeValue, Map<Word, Integer> allMatchingWords) {
        this.word = word;
        this.anagramPrimeValue = anagramPrimeValue;
        this.allMatchingWords = allMatchingWords;
    }


    @Override
    public void run() {

        if (word != null) {
            BigInteger wordValue = new BigInteger(word.getPrimeValue());

            BigInteger result = anagramPrimeValue.mod(wordValue);

            if (result.equals(BigInteger.ZERO)) {

                allMatchingWords.put(word, word.getWord().length());
            }
        }
    }
}
