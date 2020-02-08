package com.robbies.scraddle;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.robbies.scraddle.Utilities.FullScreenMode;

public class AnagramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FullScreenMode.hideToolBr(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Set The Navigation Bar to transparent===============================
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            // ===============================
        }
        setContentView(R.layout.activity_anagram);



        if (savedInstanceState == null) {
            // Get the FragmentManager and start a transaction.
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();

            // Add the SimpleFragment.
            fragmentTransaction.add(R.id.content,
                    new AnagramFragment(), "mainfrag").commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
            if (imm != null) {
                imm.showSoftInput(tv, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
