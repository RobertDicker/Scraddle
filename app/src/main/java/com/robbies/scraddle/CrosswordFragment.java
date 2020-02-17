package com.robbies.scraddle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.robbies.scraddle.WordData.WordAndDefinition;
import com.robbies.scraddle.WordData.WordViewModel;

import java.util.List;
import java.util.Objects;

public class CrosswordFragment extends Fragment implements WordListAdapter.OnWordClickListener {

    // private OnFragmentInteractionListener mListener;
    private static final String WORD = "yourLetters";
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int[] anagramFragmentButtons = {R.id.anagramChangeSortButton, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.anagramSortDirectionImageView};
    private String letters;
    private LinearLayout noWordsLinearLayout;
    private TextView numberOfWords;
    private ProgressBar indeterminateProgressBar;

    private WordViewModel mWordViewModel;
    private CrosswordListAdapter adapter;

    private FragmentListener mListener;


    public static CrosswordFragment newInstance(String word) {
        CrosswordFragment fragment = new CrosswordFragment();
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

            letters = Objects.requireNonNull(getArguments().getString(WORD));
            Log.d("Saved INSTANCE", "Word = " + letters);
        }

        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

        adapter = new CrosswordListAdapter(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anagram_checker, container, false);

        view.findViewById(R.id.includeFilter).setVisibility(View.GONE);


        // Status Bar
        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);


        //================== Get Matching Words =====================================
        mWordViewModel.getMatchingWordsForCrossword(letters.replace(" ", "_")).observe(getViewLifecycleOwner(), new Observer<List<WordAndDefinition>>() {
            @Override
            public void onChanged(List<WordAndDefinition> words) {
                //Get and store a cache of matching letters;
                updateUI(words);

            }
        });

        // ===================DISPLAY ==========================================

        //Set the chosen letters to screen and set a click listener so they can go back to change
        TextView yourLetters = view.findViewById(R.id.editTextLettersToSolve);
        yourLetters.setText(letters);

        yourLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changePage(0);
            }
        });

        numberOfWords = view.findViewById(R.id.anagramTextViewNumberOfWords);
        noWordsLinearLayout = view.findViewById(R.id.linearLayoutNoWords);

        RecyclerView recyclerView = view.findViewById(R.id.rVWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }


    //=====================  METHODS =================

    private void updateUI(List<WordAndDefinition> allMatchingWords) {

        numberOfWords.setText(String.format("Words: %s", allMatchingWords.size()));
        showHideIndeterminateProgressBar();

        //If there are no current words show this
        int visibilityNoWordsImage = allMatchingWords.size() < 1 ? View.VISIBLE : View.GONE;
        noWordsLinearLayout.setVisibility(visibilityNoWordsImage);
        adapter.setWords(allMatchingWords);
    }

    private void showHideIndeterminateProgressBar() {
        int visibility = indeterminateProgressBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        indeterminateProgressBar.setVisibility(visibility);
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
