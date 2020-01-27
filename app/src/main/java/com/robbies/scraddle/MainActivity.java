package com.robbies.scraddle;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements MainMenuFragment.FragmentSwitcher, LeaderboardFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Instantiate the fragment.
        MainMenuFragment mainMenuFragment = new MainMenuFragment(this);
        // Get the FragmentManager and start a transaction.
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.content,
                mainMenuFragment).commit();

   /*
            //Set The Navigation Bar to transparent===============================
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
           // ===============================

*/

    }


    @Override
    public void switchFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStackImmediate();
        else super.onBackPressed();
    }



    @Override
    public void onFragmentInteraction(View view) {
        Toast.makeText(this, view.getId() + "", Toast.LENGTH_SHORT);
    }
}
