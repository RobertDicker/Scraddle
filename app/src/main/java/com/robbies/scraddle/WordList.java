package com.robbies.scraddle;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordList {

    private static WordList INSTANCE = null;
    private ArrayList<String> allWords;
    private Context context;

    private WordList(Context context) {
        this.allWords = new ArrayList<>();
        this.context = context;

    }

    public static WordList getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new WordList(context);
        }
        return (INSTANCE);
    }

    private void readAllWordsFromFile(String filename, Context context) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            Log.d("WordsList.class", "Reading Words from file");
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                allWords.add(line.trim().toLowerCase());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<String> getAllWords() {
        readAllWordsFromFile("allWords.txt", context);

        return this.allWords;
    }

}
