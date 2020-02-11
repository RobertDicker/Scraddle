package com.robbies.scraddle;

import android.util.Log;

import com.robbies.scraddle.Utilities.CheckWordIsAnagram;
import com.robbies.scraddle.Utilities.PrimeValue;
import com.robbies.scraddle.Utilities.Timer;
import com.robbies.scraddle.WordData.Word;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramSolver {

    private static AnagramSolver INSTANCE;
    private List<Word> mAllWords;
    private List<Word> mMatchingWordCache;
    private static final int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    private String cachedAnagramToSolve;
    private boolean status;

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
    }

    public List<Word> getAllAnagrams(String anagramToSolve){
        status = false;
        cachedAnagramToSolve = anagramToSolve;
        mMatchingWordCache = new ArrayList<>();
        List<Word> allMatchingWords = new ArrayList<>();
        final String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);
        Log.d("CORES=======", NUMBER_OF_CORES + "");
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);
        allMatchingWords = Collections.synchronizedList(allMatchingWords);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        synchronized (allMatchingWords) {
            Timer t = new Timer();
            t.startTimer();
            for (Word word : mAllWords) {

                Runnable worker = new CheckWordIsAnagram(word, anagramPrimeValueBigInt, allMatchingWords);
                pool.submit(worker);
            }
            pool.shutdown();
            status = pool.isTerminated();
            // Wait until all threads have completed

            try{
                pool.awaitTermination(20, TimeUnit.SECONDS);
            }
             catch (InterruptedException e) {
                e.printStackTrace();
            }

                /*while(!pool.isTerminated()){
                    try {
                        Log.d("======", "completed "+ pool.getCompletedTaskCount() + " of " + pool.getTaskCount());
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

            mMatchingWordCache = allMatchingWords;
            Log.d("============", t.stopTimer() + "");
            Log.d("Word Search" , "ACTUALLY completed, words total:" + allMatchingWords.size());
            return allMatchingWords;
        }
    }

    public List<Word> getMatchingWord(){

        if(status){return mMatchingWordCache;}
        return null;
    }

    public List<Word> getAnagramsBetween(int minSize, int maxSize){
        if(mMatchingWordCache == null){
            getAllAnagrams(cachedAnagramToSolve);
        }
        if(minSize == 1){return mMatchingWordCache;}

        List<Word> matchingWords = new ArrayList<>();
        for(Word word : mMatchingWordCache){
            int wordLength = word.getWord().length();
            if(wordLength >= minSize & wordLength <= maxSize){
                matchingWords.add(word);
            }
        }
      return matchingWords;
    }

}


