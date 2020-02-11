package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.robbies.scraddle.Utilities.Timer;
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
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramFragment extends Fragment implements View.OnClickListener {

    // private OnFragmentInteractionListener mListener;
    private static final String WORD = "yourLetters";
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int[] anagramFragmentButtons = {R.id.anagramChangeSortButton, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.anagramSortDirectionImageView};
    private String letters;
    private LinearLayout noWordsLinearLayout;
    private TextView numberOfWords;
    private ProgressBar indeterminateProgressBar;

    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;
    private List<Word> constrainedMatchingWords;
    private List<Word> mAllDictionaryWords;
    private List<Word> mCachedAllMatchingWords;

    private Comparator<Word> defaultSortOrder;
    private List<Comparator<Word>> sortMethods;

    private boolean reverse = false;
    private FragmentListener mListener;


    public static AnagramFragment newInstance(String word) {
        AnagramFragment fragment = new AnagramFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WORD, word);
        Log.d("NEW INSTANCE", "Word = " + word);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            letters = Objects.requireNonNull(getArguments().getString(WORD)).replaceAll("\\s+", "");
            Log.d("Saved INSTANCE", "Word = " + letters);
        }

        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

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
                mAllDictionaryWords = words;
                // solver = AnagramSolver.getInstance(mAllDictionaryWords);

            }
        });


        // ===================DISPLAY =========================

        //Set the chosen letters to screen and set a click listener so they can go back to change
        TextView yourLetters = view.findViewById(R.id.editTextLettersToSolve);
        yourLetters.setText(letters);
        yourLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changePage(0);
            }
        });


        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);
        numberOfWords = view.findViewById(R.id.anagramTextViewNumberOfWords);
        noWordsLinearLayout = view.findViewById(R.id.linearLayoutNoWords);

        RecyclerView recyclerView = view.findViewById(R.id.rVWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //Register onclick listeners
        for (int button : anagramFragmentButtons) {
            view.findViewById(button).setOnClickListener(this);
        }

        //All Words


        onClick(yourLetters);
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
        constrainedMatchingWords = new ArrayList<>();
        Log.d("BUTTON WORD LENGTH = ", "WordLength = " + buttonWordLength);
        int maxWordLength = buttonWordLength == 0 | buttonWordLength == 1 | buttonWordLength == 8 ? R.string.maxAnagramLetters : buttonWordLength;
        numberOfWords.setText("");

        final String anagramToSolve = letters;
        final String anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);
        Log.d("ANAGRAM PRIME VALUE = ", " = " + anagramPrimeValue.length());


        //Words have already been cached so look through them
        if (buttonWordLength != 0 & mCachedAllMatchingWords != null) {
            Log.d("CACHED IN LOOP", "Cached size: " + mCachedAllMatchingWords.size());
            if (buttonWordLength == 1) {
                constrainedMatchingWords.addAll(mCachedAllMatchingWords);
            } else {
                for (Word word : mCachedAllMatchingWords) {
                    int wordLength = word.getWord().length();
                    if (wordLength >= buttonWordLength & wordLength <= maxWordLength) {
                        constrainedMatchingWords.add(word);

                    }
                }
            }
            sortWords();
            updateUI();
        }

        //Words can be searched as numbers (Quicker)
        else if (anagramPrimeValue.length() < 19) {
            mWordViewModel.getAnagramsOf(anagramPrimeValue, buttonWordLength, maxWordLength).observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable final List<Word> words) {
                    mCachedAllMatchingWords = new ArrayList<>();
                    constrainedMatchingWords = words;
                    if (constrainedMatchingWords != null) {
                        sortWords();
                        mCachedAllMatchingWords.addAll(constrainedMatchingWords);
                    }
                    updateUI();
                }
            });

            //Words are too large to be searched for in Room so Get all the words and iterate over them to determine value in default sort order first time
        } else {
            mCachedAllMatchingWords = new ArrayList<>();
            Log.d("Word Search", "apparently completed, words total:" + constrainedMatchingWords.size());

            final Timer timer = new Timer();
            timer.startTimer();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mAllDictionaryWords == null) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    constrainedMatchingWords = getAnagrams(mAllDictionaryWords, anagramPrimeValue);

                    sortWords();
                    mCachedAllMatchingWords.addAll(constrainedMatchingWords);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateUI();

                        }
                    });
                    Log.d("=====TIMER===", "time = " + timer.stopTimer());
                }
            }).start();
        }
    }

    //=====================  METHODS =================

    private void sortWords() {

        if (reverse) {
            Collections.sort(constrainedMatchingWords, defaultSortOrder);
            Collections.reverse(constrainedMatchingWords);
        } else {
            Collections.sort(constrainedMatchingWords, defaultSortOrder);
        }
    }

    private void updateUI() {
        adapter.setWords(constrainedMatchingWords);
        Log.d("WORDS ====", "NUMBER OF WORDS = " + constrainedMatchingWords.size());
        numberOfWords.setText(String.format("Words: %s", constrainedMatchingWords.size()));

        //Turn off
        showHideIndeterminateProgressBar();

        //If there are no current words show this
        int visibilityNoWordsImage = constrainedMatchingWords.size() < 1 ? View.VISIBLE : View.GONE;
        noWordsLinearLayout.setVisibility(visibilityNoWordsImage);
    }


    private List<Word> getAnagrams(List<Word> words, String anagramPrimeValue) {
        List<Word> allMatchingWords = new ArrayList<>();
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);

        allMatchingWords = Collections.synchronizedList(allMatchingWords);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        synchronized (allMatchingWords) {


            for (Word word : words) {

                Runnable worker = new CheckWordIsAnagram(word, anagramPrimeValueBigInt, allMatchingWords);
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
        Log.d("PROGRESS BAR ", "VISIBILITY = " + visibility);
        indeterminateProgressBar.setVisibility(visibility);
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
                solveAnagram(1);
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
            default:
                solveAnagram(0);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
