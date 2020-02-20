package com.robbies.scraddle;

import android.os.Bundle;
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

public class WordCrosswordFragment extends Fragment implements WordListAdapter.OnWordClickListener {

    private String letters;
    private LinearLayout noWordsLinearLayout;
    private TextView numberOfWords;
    private ProgressBar indeterminateProgressBar;

    private WordViewModel mWordViewModel;
    private WordCrosswordListAdapter adapter;


    static WordCrosswordFragment newInstance() {
        return new WordCrosswordFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ========================= DATA =====================

        mWordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

        adapter = new WordCrosswordListAdapter(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anagram_checker, container, false);

        view.findViewById(R.id.includeFilter).setVisibility(View.GONE);
        indeterminateProgressBar = view.findViewById(R.id.progressBarIndeterminate);

        //================== Get Matching Words =====================================

        mWordViewModel.getLetters().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                letters = s;


                mWordViewModel.getMatchingWordsForCrossword().observe(getViewLifecycleOwner(), new Observer<List<WordAndDefinition>>() {
                    @Override
                    public void onChanged(List<WordAndDefinition> words) {
                        //Get and store a cache of matching letters;
                        updateUI(words);
                    }
                });
            }
        });

        // ===================DISPLAY ==========================================

        //Set the chosen letters to screen and set a click listener so they can go back to change
        TextView yourLetters = view.findViewById(R.id.editTextLettersToSolve);
        yourLetters.setText(letters);

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
        indeterminateProgressBar.setVisibility(View.GONE);

        //If there are no current words show this
        int visibilityNoWordsImage = allMatchingWords.size() < 1 ? View.VISIBLE : View.GONE;
        noWordsLinearLayout.setVisibility(visibilityNoWordsImage);
        adapter.setWords(allMatchingWords);
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
