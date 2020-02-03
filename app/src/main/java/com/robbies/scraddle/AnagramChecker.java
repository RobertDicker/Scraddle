package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.WordComparators.LengthComparator;
import com.robbies.scraddle.WordComparators.LexicographicComparator;
import com.robbies.scraddle.WordComparators.ScrabbleValueComparator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramChecker extends AppCompatActivity {

    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    long startTimer;
    private EditText letters;
    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;


    private ProgressBar indeterminateProgressBar;
    private List<Word> matchingWords;
    private TextView numberOfWords;
    private Button sortButton;


    private Comparator defaultSortOrder;
    private List<Comparator> sortMethods;

    private boolean reverse = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_checker);

        //Set The Navigation Bar to transparent===============================
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // ===============================

        // ========== DATA ==========
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        matchingWords = new ArrayList<>();

        // =========DISPLAY =====================

        letters = findViewById(R.id.editTextLettersToSolve);
        indeterminateProgressBar = findViewById(R.id.progressBarIndeterminate);
        numberOfWords = findViewById(R.id.anagramTextViewNumberOfWords);

        RecyclerView recyclerView = findViewById(R.id.rVWords);
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        letters.requestFocus();

        // =========SORTING ===========================

        sortMethods = new ArrayList<>();
        sortMethods.addAll(Arrays.asList(
                new ScrabbleValueComparator(),
                new LexicographicComparator(),
                new LengthComparator()
        ));
        defaultSortOrder = sortMethods.get(0);
        sortButton = findViewById(R.id.anagramChangeSortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHideIndeterminateProgressBar();

                //if reverse is now false, change sort method

                Collections.rotate(sortMethods, 1);
                defaultSortOrder = sortMethods.get(0);

                sortWords();
                updateUI();
            }
        });

    }


    public void solveAnagram(View view) {

        numberOfWords.setText("");
        showHideIndeterminateProgressBar();

        int[] buttons = {0, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight};
        int minWordLength = 0;
        int maxWordLength = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (view.getId() == buttons[i]) {
                minWordLength = i;
                maxWordLength = i == 1 || i == 8 ? 20 : i;
                break;
            }
        }

        //Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final String anagramToSolve = letters.getText().toString().toLowerCase();
        final String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);


        //Words can be searched as numbers
        if (anagramPrimeValue.length() < 19) {
            mWordViewModel.getAnagramsOf(anagramPrimeValue, minWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable final List<Word> words) {

                    matchingWords = words;
                    if (matchingWords != null) {
                        sortWords();
                        updateUI();
                    }
                }
            });

        } else {

            //Words are too large to be searched for in Room so Get all the words and iterate over them to determine value in default sort order
            //    mMatchingWords.clear();
            if (view.getId() == R.id.button_allWords) {
                startTimer = System.nanoTime();
                mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {

                    @Override
                    public void onChanged(List<Word> words) {

                        matchingWords = getAnagrams(words, anagramPrimeValue);
                        sortWords();
                        updateUI();
                    }
                });
            } else {
                //   startTimer = System.nanoTime();
                mWordViewModel.getAllWords(minWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {

                        matchingWords = getAnagrams(words, anagramPrimeValue);
                        sortWords();
                        updateUI();
                    }
                });
            }
        }
    }

    private void sortWords() {

        if (reverse) {
            Collections.sort(matchingWords, defaultSortOrder);
            Collections.reverse(matchingWords);
        } else {
            Collections.sort(matchingWords, defaultSortOrder);
        }
    }

    private void updateUI() {
        adapter.setWords(matchingWords);
        numberOfWords.setText(String.format("Words: %s", matchingWords.size()));
        showHideIndeterminateProgressBar();
        String sortButtonText = defaultSortOrder instanceof ScrabbleValueComparator ? "Scrabble Value" : defaultSortOrder instanceof LexicographicComparator ? "A - Z" : "Word Length";
        sortButton.setText(sortButtonText);
    }

/*    private void timer(){
        long startTime = System.nanoTime();
        long stopTime = System.nanoTime();
        double algoTimerInSeconds = (double) (stopTime - startTime) / 1_000_000_000.0;
        Log.d("==COMPLETE==", "ALL COMPLETED ================================================");
        Log.d("=====COMPLETED====", "Total Time: " + otherTimer + "Algo Time: " + algoTimerInSeconds);

    }*/

    private List<Word> getAnagrams(List<Word> words, String anagramPrimeValue) {
        List<Word> allMatchingWords = new ArrayList<>();
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);

        allMatchingWords = Collections.synchronizedList(allMatchingWords);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        synchronized (allMatchingWords) {
            Iterator<Word> wordIterator = words.iterator();
            while (wordIterator.hasNext()) {

                Callable worker = new CheckWordIsAnagram(wordIterator.next(), anagramPrimeValueBigInt, allMatchingWords);
                pool.submit(worker);
            }
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

    private void showHideIndeterminateProgressBar() {
        int visibility = indeterminateProgressBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        indeterminateProgressBar.setVisibility(visibility);
    }

    public void reverse(View view) {
        showHideIndeterminateProgressBar();
        reverse = !reverse;
        sortWords();
        view.setRotation(reverse ? 180 : 0);
        updateUI();


    }
}




