package com.robbies.scraddle.Utilities;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.robbies.scraddle.R;

public class ThemeChanger {


    public static int getThemeFromPreferences(Context context) {

        String themeColour = PreferenceManager.getDefaultSharedPreferences(context).getString("theme", "");
        int theme;
        switch (themeColour) {
            case ("Red"):
                theme = R.style.Theme_Red;
                break;
            case ("Purple"):
                theme = R.style.Theme_Purple;
                break;
            case ("Pink"):
                theme = R.style.Theme_Pink;
                break;
            case ("Orange"):
                theme = R.style.Theme_Orange;
                break;
            case ("Green"):
                theme = R.style.Theme_Green;
                break;
            default:
                theme = R.style.Theme_Blue;
        }

        return theme;
    }

}
