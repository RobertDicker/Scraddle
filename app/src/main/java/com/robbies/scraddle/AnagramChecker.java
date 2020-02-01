package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnagramChecker extends AppCompatActivity {

    private EditText letters;
    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;
    private List<Word> mAllWords;
    private List<Word> mMatchingWords;
    private BigInteger anagramPrimeValueBigInt;
    private ProgressBar progressBar;
    private ProgressBar indeterminateProgressBar;
    private int progressCounter;
    private int progressPercentage;
    private TextView numberOfWords;
    private Handler handler = new Handler();
    private Runnable updateProgress;
    private Comparator sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_checker);
        mMatchingWords = new ArrayList<Word>();
        //Set The Navigation Bar to transparent===============================
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // ===============================


        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        letters = findViewById(R.id.editTextLettersToSolve);
        progressBar = findViewById(R.id.anagramProgressBar);
        indeterminateProgressBar = findViewById(R.id.progressBarIndeterminate);
        numberOfWords = findViewById(R.id.anagramTextViewNumberOfWords);
        sortOrder = new scrabbleValueComparator();

        updateProgress = new Runnable() {
            public void run() {
                progressBar.setProgress(0);

                while (progressPercentage < 100) {

                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {

                        public void run() {

                            progressBar.setProgress(progressPercentage);

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };


        RecyclerView recyclerView = findViewById(R.id.rVWords);
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                mAllWords = words;
                Log.d("===========>", "Words loaded");
            }
        });

        letters.requestFocus();


    }


    public void solveAnagram(View view) {

        numberOfWords.setText("");
        showHideProgressBars();
        new Thread(updateProgress).start();
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


        String anagramToSolve = letters.getText().toString().toLowerCase();
        String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);
        anagramPrimeValueBigInt = new BigInteger(PrimeValue.calculatePrimeValue(anagramToSolve));

        if (anagramPrimeValue.length() < 19) {
            mWordViewModel.getAnagramsOf(anagramPrimeValue, minWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable final List<Word> words) {


                    mMatchingWords = words;
                    if (mMatchingWords != null) {
                        progressPercentage = 100;
                        Collections.sort(mMatchingWords, sortOrder);
                        adapter.setWords(mMatchingWords);

                        showHideProgressBars();
                        numberOfWords.setText(String.format("Words: %s", mMatchingWords.size()));
                    }
                }
            });
        } else {

            mMatchingWords.clear();

            if (view.getId() == R.id.button_allWords) {
                mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {

                    @Override
                    public void onChanged(List<Word> words) {
                        checkWord(words, sortOrder);
                    }
                });


            } else {

                mWordViewModel.getAllWords(minWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {

                    @Override
                    public void onChanged(List<Word> words) {
                        checkWord(words, sortOrder);
                    }
                });
            }

        }


    }

    private void showHideProgressBars() {
        int visibility = indeterminateProgressBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        indeterminateProgressBar.setVisibility(visibility);
    }

    void checkWord(final List<Word> words, final Comparator sortOrder) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                progressPercentage = 0;
                progressCounter = 0;
                BigInteger wordValue;
                BigInteger result;
                BigInteger zero = new BigInteger("0");
                final int wordCount = words.size();

                for (Word word : words) {

                    String wordd = word.getPrimeValue();
                    wordValue = new BigInteger(word.getPrimeValue());
                    result = anagramPrimeValueBigInt.mod(wordValue);
                    progressPercentage = ((int) ((progressCounter++ / (float) wordCount) * 100));


                    if (result.equals((zero))) {
                        mMatchingWords.add(word);
                        Log.d("------->", String.format("word: %s, primeValue: %s, result: %s", word.getWord(), wordd, result));

                    }


                }

                Collections.sort(mMatchingWords, sortOrder);
                handler.post(new Runnable(

                ) {
                    @Override
                    public void run() {
                        adapter.setWords(mMatchingWords);
                        showHideProgressBars();
                        numberOfWords.setText(String.format("Words: %s", mMatchingWords.size()));
                    }
                });

            }
        }).start();


    }
}

class LexicographicComparator implements Comparator<Word> {
    @Override
    public int compare(Word a, Word b) {
        return a.getWord().compareToIgnoreCase(b.getWord());
    }
}

class lengthComparator implements Comparator<Word> {
    @Override
    public int compare(Word b, Word a) {
        return Integer.compare(a.getWord().length(), b.getWord().length());
    }
}

class scrabbleValueComparator implements Comparator<Word> {
    @Override
    public int compare(Word b, Word a) {
        return Integer.compare(Integer.parseInt(a.getScrabbleValue()), Integer.parseInt(b.getScrabbleValue()));
    }
}
