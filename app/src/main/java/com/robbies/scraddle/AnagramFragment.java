package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.Utilities.CheckWordIsAnagram;
import com.robbies.scraddle.Utilities.PrimeValue;
import com.robbies.scraddle.WordComparators.LengthComparator;
import com.robbies.scraddle.WordComparators.LexicographicComparator;
import com.robbies.scraddle.WordComparators.ScrabbleValueComparator;
import com.robbies.scraddle.WordData.Word;
import com.robbies.scraddle.WordData.WordViewModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramFragment extends Fragment implements View.OnClickListener {

    // private OnFragmentInteractionListener mListener;
    private static final int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    private final int[] anagramFragmentButtons = {R.id.anagramChangeSortButton, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.anagramSortDirectionImageView};
    private EditText letters;
    private TextView numberOfWords;
    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;
    private List<Word> matchingWords;
    private List<Word> mAllWords;
    private Comparator<Word> defaultSortOrder;
    private List<Comparator<Word>> sortMethods;

    private ProgressBar indeterminateProgressBar;
    private boolean reverse = false;

    public AnagramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        matchingWords = new ArrayList<>();
        sortMethods = new ArrayList<>();
        sortMethods.addAll(Arrays.asList(
                new ScrabbleValueComparator(),
                new LexicographicComparator(),
                new LengthComparator()
        ));
        defaultSortOrder = sortMethods.get(0);
        adapter = new WordListAdapter(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anagram_checker, container, false);

        mWordViewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                mAllWords = words;

            }
        });

        // ===================DISPLAY =========================

        letters = view.findViewById(R.id.editTextLettersToSolve);
        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);
        numberOfWords = view.findViewById(R.id.anagramTextViewNumberOfWords);

        RecyclerView recyclerView = view.findViewById(R.id.rVWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        //Focus Keyboard and bring up the soft keyboard
        letters.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }


        //Register onclick listeners
        for (int button : anagramFragmentButtons) {
            view.findViewById(button).setOnClickListener(this);
        }

        return view;
    }


    //================== Button Handlers =======================

    private void reverse(View view) {

        reverse = !reverse;
        sortWords();
        view.setRotation(reverse ? 180 : 0);
        updateUI();
    }

    private void changeSortOrder(View view) {

        Collections.rotate(sortMethods, 1);
        defaultSortOrder = sortMethods.get(0);

        Button b = view.findViewById(view.getId());
        String sortButtonText = defaultSortOrder instanceof ScrabbleValueComparator ? "Scrabble Value" : defaultSortOrder instanceof LexicographicComparator ? "A - Z" : "Word Length";
        b.setText(sortButtonText);

        sortWords();
        updateUI();
    }

    private void solveAnagram(int buttonWordLength) {


        int maxWordLength = buttonWordLength == 0 | buttonWordLength == 8 ? R.string.maxAnagramLetters : buttonWordLength;
        numberOfWords.setText("");

        final String anagramToSolve = letters.getText().toString().toLowerCase();
        final String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);

        //Words can be searched as numbers
        if (anagramPrimeValue.length() < 19) {
            mWordViewModel.getAnagramsOf(anagramPrimeValue, buttonWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
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
            if (buttonWordLength == 0) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mAllWords == null) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        matchingWords = getAnagrams(mAllWords, anagramPrimeValue);
                        sortWords();
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();

                            }
                        });

                    }
                }).start();

            } else {
                //   startTimer = System.nanoTime();
                mWordViewModel.getAllWords(buttonWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
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

    //=====================  METHODS =================

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


    }


    private List<Word> getAnagrams(List<Word> words, String anagramPrimeValue) {
        List<Word> allMatchingWords = new ArrayList<>();
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);

        allMatchingWords = Collections.synchronizedList(allMatchingWords);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        synchronized (allMatchingWords) {


            for (Word word : words) {

                Callable worker = new CheckWordIsAnagram(word, anagramPrimeValueBigInt, allMatchingWords);
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

    private void hideKeyboard() {
        //Hide Keyboard

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }

    }

    @Override
    public void onClick(View view) {
        showHideIndeterminateProgressBar();
        switch (view.getId()) {

            case (R.id.anagramSortDirectionImageView):
                reverse(view);
                break;
            case (R.id.anagramChangeSortButton):
                changeSortOrder(view);
                break;
            case (R.id.button_allWords):
                solveAnagram(0);
                break;
            case (R.id.buttonTwoWords):
                solveAnagram(2);
                break;
            case (R.id.buttonThree):
                solveAnagram(3);
                break;
            case (R.id.buttonFour):
                solveAnagram(4);
                break;
            case (R.id.buttonFive):
                solveAnagram(5);
                break;

            case (R.id.buttonSix):
                solveAnagram(6);
                break;
            case (R.id.buttonSeven):
                solveAnagram(7);
                break;
            case (R.id.buttonEight):
                solveAnagram(8);
                break;
        }
        if (view.getId() != R.id.anagramChangeSortButton || view.getId() != R.id.anagramSortDirectionImageView) {
            hideKeyboard();
        }

    }

}
