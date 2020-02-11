package com.robbies.scraddle.Utilities;

import android.app.Activity;

import android.view.View;



public class FullScreenMode {


    public static void hideToolBr(Activity activity) {

        View decorView = activity.getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }
}
