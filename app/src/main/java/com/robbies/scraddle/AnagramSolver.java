package com.robbies.scraddle;

import android.util.Log;

import com.robbies.scraddle.Utilities.CheckWordIsAnagram;
import com.robbies.scraddle.WordData.Word;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramSolver {

    private static final int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
/*
    private static AnagramSolver INSTANCE;
    private List<Word> mAllWords;
    private List<Word> mMatchingWordCache;

    private String cachedAnagramToSolve;
    private boolean status;
*/
/*
    public AnagramSolver(List<Word> allWords) {
        mAllWords = allWords;
        this.status = false;
    }

    // static method to create instance of Singleton class
    public static AnagramSolver getInstance(List<Word> allWords )
    {
        if (INSTANCE == null)
            INSTANCE = new AnagramSolver(allWords);
        return INSTANCE;
    }*/

    public static List<Word> getAnagramsBetween(int minSize, int maxSize, List<Word> allWords) {

        if (minSize < 2) {
            return allWords;
        }

        List<Word> matchingWords = new ArrayList<>();
        for (Word word : allWords) {
            int wordLength = word.getWord().length();
            if (wordLength >= minSize & wordLength <= maxSize) {
                matchingWords.add(word);
            }
        }
        return matchingWords;
    }

    private Map<Integer, List<Word>> createAnagramMap(List<Word> allWords, String anagramPrimeValue) {

        Map<Integer, List<Word>> allMatchingWords = new ConcurrentHashMap<>();
        for (int i = 2; i < 9; i++) {
            allMatchingWords.put(i, new ArrayList<Word>());
        }

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        /*        synchronized (allMatchingWords) {*/


        for (Word word : allWords) {

            Runnable worker = new CheckWordIsAnagram(word, anagramPrimeValueBigInt, allMatchingWords);
            pool.submit(worker);
        }

        pool.shutdown();
        // Wait until all threads have completed
        try {
            pool.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allMatchingWords;
    }

}


