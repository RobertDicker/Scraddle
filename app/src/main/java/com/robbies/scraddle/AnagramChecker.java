package com.robbies.scraddle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnagramChecker extends AppCompatActivity {

    private EditText letters;
    private WordViewModel mWordViewModel;

    private String anagramPrimeValue = "";

    private List<Word> anagramWords;
    private WordListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_checker);
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        Log.d("===========>", mWordViewModel.getAllWords().toString());
        letters = findViewById(R.id.editTextLettersToSolve);


        RecyclerView recyclerView = findViewById(R.id.rVWords);
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    public void solveAnagram(View view) {
        int[] buttons = {0, R.id.button_allWords, R.id.buttonTwoWords, R.id.buttonThree, R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight};
        int minWordLength = 0;
        int maxWordLength = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (view.getId() == buttons[i]) {
                minWordLength = i;
                maxWordLength = i == 1 || i == 8 ? 15 : i;
                break;
            }
        }


        anagramWords = new ArrayList<>();
        String anagramToSolve = letters.getText().toString().toLowerCase();
        anagramPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);

        mWordViewModel.getAnagramsOf(anagramPrimeValue, minWordLength, maxWordLength).observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {

                adapter.setWords(words);
            }
        });


    }
}
