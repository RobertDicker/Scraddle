package com.robbies.scraddle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.robbies.scraddle.WordComparators.LoadingFragment;

import java.util.List;

public class AnagramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram);

        // Get the FragmentManager and start a transaction.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.content,
                new LoadingFragment()).commit();

        WordViewModel mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {

                if(words !=null){

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new AnagramFragment())
                            .commit();
                }

                }


        });

    }
}
