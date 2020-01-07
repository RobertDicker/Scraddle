package com.robbies.scraddle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnagramChecker extends AppCompatActivity {

    //  private TreeMap<Integer, Word> cachedWordList;
//    private EditText letters;
    private WordViewModel mWordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram_checker);
        //      cachedWordList = new TreeMap<>();
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.rVWords);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });
    }

 /*   private void addWordsToMap(ArrayList<String> words) {

        for (String word : words) {

            Word newWord = new Word(word);
            cachedWordList.put(newWord.getPrimeValue(), newWord);

        }

    }*/



/*
    public void solveAnagram(View view) {
        String anagramToSolve = letters.getText().toString().toLowerCase();

        int anagramsPrimeValue = PrimeValue.calculatePrimeValue(anagramToSolve);

        ArrayList<String> words = new ArrayList<>();


       for(int number : cachedWordList.keySet()){
            if(anagramsPrimeValue % number == 0){
                words.add(cachedWordList.get(number).getWord());
            }
            if(number > anagramsPrimeValue){break;}
       }

       Log.d("--THE WORDS---", words.toString());




    }*/
}
