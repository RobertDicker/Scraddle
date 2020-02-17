package com.robbies.scraddle;

import com.robbies.scraddle.WordData.Word;

import java.util.ArrayList;
import java.util.List;

public class AnagramSolver {

/*
    private static AnagramSolver INSTANCE;
    private List<Word> mAllWords;
    private List<Word> mMatchingWordCache;
    private static final int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
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
/*
    public static List<Word> getAllAnagrams(String anagramToSolve, List<Word> mAllWords){


        List<Word> allMatchingWords = new ArrayList<>();
        final String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        allMatchingWords = Collections.synchronizedList(allMatchingWords);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);
        Log.d("Starting stuff", "===");
        synchronized (allMatchingWords) {
            Timer t = new Timer();
            t.startTimer();
            for (Word word : mAllWords) {

               *//* Runnable worker = new CheckWordIsAnagram(word, anagramPrimeValueBigInt, allMatchingWords);*//*
                pool.submit(worker);
            }
            pool.shutdown();

            try {
                pool.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return allMatchingWords;
        }
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

}


