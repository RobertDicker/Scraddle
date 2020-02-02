package com.robbies.scraddle;

import android.util.Log;

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


  /*  @Override
    public void run() {

        BigInteger wordValue = new BigInteger(word.getPrimeValue());

        BigInteger result = anagramPrimeValue.mod(wordValue);

        if (result.equals(BigInteger.ZERO)) {
            if(word == null){
                Log.d("broke", "here");
            }
            if(word != null){
            allMatchingWords.add(word);}
            Log.d("", "word: " + word);
        }
    }*/

    @Override
    public Boolean call() throws Exception {

        BigInteger wordValue = new BigInteger(word.getPrimeValue());

        BigInteger result = anagramPrimeValue.mod(wordValue);

        if (result.equals(BigInteger.ZERO)) {
            if(word == null){
                Log.d("broke", "here");
            }
            if(word != null){
                allMatchingWords.add(word);}

        }

        return null;
    }
}
