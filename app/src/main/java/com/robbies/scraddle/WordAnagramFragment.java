package com.robbies.scraddle;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WordAnagramFragment extends Fragment implements View.OnClickListener, WordListAdapter.OnWordClickListener {

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int[] anagramFragmentButtons = {R.id.anagramChangeSortButton, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.anagramSortDirectionImageView};
    private String letters;
    private LinearLayout noWordsLinearLayout;
    private TextView numberOfWords;
    private ProgressBar indeterminateProgressBar;
    private TextView yourLetters;

    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;
    private List<Word> allMatchingWordsList;

    private Comparator<Word> defaultSortOrder;
    private List<Comparator<Word>> sortMethods;

    private boolean reverse = false;
    private FragmentListener mListener;


    static WordAnagramFragment newInstance() {
        return new WordAnagramFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

        sortMethods = new ArrayList<>();
        sortMethods.addAll(Arrays.asList(
                new ScrabbleValueComparator(),
                new LexicographicComparator(),
                new LengthComparator()
        ));
        defaultSortOrder = sortMethods.get(0);
        adapter = new WordListAdapter(requireContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anagram_checker, container, false);


        // ===================DISPLAY ==========================================
        // View items
        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);
        numberOfWords = view.findViewById(R.id.anagramTextViewNumberOfWords);
        noWordsLinearLayout = view.findViewById(R.id.linearLayoutNoWords);

        //Set the chosen letters to screen and set a click listener so they can go back to change
        yourLetters = view.findViewById(R.id.editTextLettersToSolve);

        yourLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changePage(0);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.rVWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //===================Register listeners===================
        for (int button : anagramFragmentButtons) {
            view.findViewById(button).setOnClickListener(this);
        }


        setLetterListener();
        setAnagramChangeListener();


        return view;
    }


    //================== Button Handlers =======================

    private void reverse(View view) {
        showIndeterminateProgressBar(true);
        reverse = !reverse;
        sortWords();
        view.setRotation(reverse ? 180 : 0);
        updateUI();
    }

    private void changeSortOrder(View view) {
        showIndeterminateProgressBar(true);
        Collections.rotate(sortMethods, 1);
        defaultSortOrder = sortMethods.get(0);

        Button b = view.findViewById(view.getId());
        String sortButtonText = defaultSortOrder instanceof ScrabbleValueComparator ? "Scrabble Value" : defaultSortOrder instanceof LexicographicComparator ? "A - Z" : "Word Length";
        b.setText(sortButtonText);

        sortWords();
        updateUI();
    }

    //=====================  METHODS =================

    private void setAnagramChangeListener() {

        //================Ui to update Listener===========================================

        mWordViewModel.getAnagrams().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {

                if (words != null) {
                    allMatchingWordsList = words;
                    if (!words.isEmpty()) {
                        sortWords();
                    }
                }
                if (letters.length() > 1) {
                    updateUI();
                }
            }
        });
    }

    private void setLetterListener() {

        //================== Get The Letters and All Words =====================================
        mWordViewModel.getLetters().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String savedLetters) {

                letters = savedLetters;
                yourLetters.setText(letters);
                final String primeValueOfLetters = PrimeValue.calculatePrimeValue(letters);

                if (primeValueOfLetters.length() < 19) {

                    mWordViewModel.getAnagramsOf(primeValueOfLetters, 0, letters.length());

                } else {

                    mWordViewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                        @Override
                        public void onChanged(List<Word> words) {
                            if (!words.isEmpty()) {
                                mWordViewModel.setAllAnagramsCache(createAnagramMap(words, primeValueOfLetters));
                            }
                        }
                    });

                }
            }
        });

    }

    private void sortWords() {

        if (reverse) {
            Collections.sort(allMatchingWordsList, Collections.reverseOrder(defaultSortOrder));
        } else {
            Collections.sort(allMatchingWordsList, defaultSortOrder);
        }
    }


    private void updateUI() {
        adapter.setWords(allMatchingWordsList);
        numberOfWords.setText(String.format("Words: %s", allMatchingWordsList.size()));

        //Turn off
        showIndeterminateProgressBar(false);

        //If there are no current words show this
        int visibilityOfNoWordsImage = allMatchingWordsList.size() < 1 ? View.VISIBLE : View.GONE;
        noWordsLinearLayout.setVisibility(visibilityOfNoWordsImage);
    }


    private Map<Integer, List<Word>> createAnagramMap(List<Word> words, String anagramPrimeValue) {

        Map<Integer, List<Word>> allMatchingWords = new ConcurrentHashMap<>();
        for (int i = 2; i < 9; i++) {
            allMatchingWords.put(i, new ArrayList<Word>());
        }

        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CORES);

        BigInteger anagramPrimeValueBigInt = new BigInteger(anagramPrimeValue);

        /*        synchronized (allMatchingWords) {*/


        for (Word word : words) {

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

    private void showIndeterminateProgressBar(Boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        indeterminateProgressBar.setVisibility(visibility);
    }


    @Override
    public void onClick(View view) {

        showIndeterminateProgressBar(true);

        switch (view.getId()) {

            case (R.id.anagramSortDirectionImageView):
                reverse(view);
                break;
            case (R.id.anagramChangeSortButton):
                changeSortOrder(view);
                break;
            default:

                for (int i = 1; i < anagramFragmentButtons.length; i++) {
                    if (anagramFragmentButtons[i] == view.getId()) {
                        mWordViewModel.setSelectedAnagrams(i);
                        break;
                    }
                }
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
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

    @Override
    public void onWordClick(String word) {

        mWordViewModel.getDefinition(word).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT)
                        .show();

            }
        });
    }
}
