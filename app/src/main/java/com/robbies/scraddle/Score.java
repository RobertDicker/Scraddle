package com.robbies.scraddle;

import android.text.TextUtils;

import java.util.ArrayList;

// This Class is for each of the players scores
class Score {
    private ArrayList<Integer> mAllScore = new ArrayList<>();


    public Score() {
    }

    public void addToScore(String score) {

        if (TextUtils.isEmpty(score)) {
            mAllScore.add(Integer.parseInt(score.trim())); // or break, continue, throw
        }
    }

    public void addToScore(int score) {
        mAllScore.add(score);

    }

    public int getTotalScore() {
        int sum = 0;
        for (Integer number : mAllScore) {
            sum += number;
        }
        return sum;
    }

}