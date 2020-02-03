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

import com.robbies.scraddle.Utilities.PrimeValue;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 */
public class AnagramFragment extends Fragment implements View.OnClickListener {

    // private OnFragmentInteractionListener mListener;
    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
    private final int[] anagramFragmentButtons = {R.id.anagramChangeSortButton, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.anagramSortDirectionImageView};
    private EditText letters;
    private TextView numberOfWords;
    private WordViewModel mWordViewModel;
    private WordListAdapter adapter;
    private List<Word> matchingWords;

    private Comparator defaultSortOrder;
    private List<Comparator> sortMethods;

    private ProgressBar indeterminateProgressBar;
    private boolean reverse = false;


    public AnagramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anagram_checker, container, false);

        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(getActivity()).get(WordViewModel.class);
        matchingWords = new ArrayList<>();

        // ===================DISPLAY =========================

        letters = view.findViewById(R.id.editTextLettersToSolve);
        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);
        numberOfWords = view.findViewById(R.id.anagramTextViewNumberOfWords);

        RecyclerView recyclerView = view.findViewById(R.id.rVWords);
        adapter = new WordListAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        letters.requestFocus();

        //================== SORTING ========================

        sortMethods = new ArrayList<>();
        sortMethods.addAll(Arrays.asList(
                new ScrabbleValueComparator(),
                new LexicographicComparator(),
                new LengthComparator()
        ));
        defaultSortOrder = sortMethods.get(0);

        //Register onclick listeners
        for (int button : anagramFragmentButtons) {

            view.findViewById(button).setOnClickListener(this);

        }

        return view;
    }


    //================== Button Handlers =======================

    public void reverse(View view) {

        reverse = !reverse;
        sortWords();
        view.setRotation(reverse ? 180 : 0);
        updateUI();
    }

    public void changeSortOrder(View view) {

        Collections.rotate(sortMethods, 1);
        defaultSortOrder = sortMethods.get(0);

        Button b = view.findViewById(view.getId());
        String sortButtonText = defaultSortOrder instanceof ScrabbleValueComparator ? "Scrabble Value" : defaultSortOrder instanceof LexicographicComparator ? "A - Z" : "Word Length";
        b.setText(sortButtonText);

        sortWords();
        updateUI();
    }

    public void solveAnagram(int buttonValue) {

        int minWordLength = buttonValue;
        int maxWordLength = buttonValue == 0 | buttonValue == 8 ? R.string.maxAnagramLetters : buttonValue;
        numberOfWords.setText("");

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
            if (buttonValue == 0) {

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

/*    private void timer(){



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

        //Hide Keyboard
        if (view.getId() != R.id.anagramChangeSortButton || view.getId() != R.id.anagramSortDirectionImageView) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            }
        }




/*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
    }
}
