package com.robbies.scraddle;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
                new AnagramFragment()).commit();

        WordViewModel mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
       /* mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {

                if (words != null) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new AnagramFragment())
                            .commit();
                }

            }


        });*/

    }

    @Override
    public void onBackPressed() {
        EditText tv = findViewById(R.id.editTextLettersToSolve);

        if (tv.getText().toString().isEmpty()) {
            super.onBackPressed();
        } else {
            tv.setText("");
            tv.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(tv, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
