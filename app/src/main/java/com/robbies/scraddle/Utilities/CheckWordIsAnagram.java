package com.robbies.scraddle.Utilities;

import com.robbies.scraddle.WordData.Word;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

public class CheckWordIsAnagram implements Callable<Boolean> {

    private Word word;
    private BigInteger anagramPrimeValue;
    private List<Word> allMatchingWords;

    public CheckWordIsAnagram(Word word, BigInteger anagramPrimeValue, List<Word> allMatchingWords) {
        this.word = word;
        this.anagramPrimeValue = anagramPrimeValue;
        this.allMatchingWords = allMatchingWords;
    }


    @Override
    public Boolean call() {

        BigInteger wordValue = new BigInteger(word.getPrimeValue());

        BigInteger result = anagramPrimeValue.mod(wordValue);

        if (result.equals(BigInteger.ZERO)) {
            if (word == null) {

            }
            if (word != null) {
                allMatchingWords.add(word);
            }

        }

        return null;
    }
}
