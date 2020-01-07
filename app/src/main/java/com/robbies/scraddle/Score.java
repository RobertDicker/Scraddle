package com.robbies.scraddle;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

// This Class is for each of the players scores
class Score implements Serializable {
    private ArrayList<Integer> mAllScore;


    public Score() {
        mAllScore = new ArrayList<>();

    }

    public void addToScore(String score) {

        if (TextUtils.isEmpty(score)) {
            mAllScore.add(Integer.parseInt(score.trim())); // or break, continue, throw
        }
    }

    public void addToScore(int score) {
        mAllScore.add(score);

    }

    public String getTotalScore() {

        int sum = 0;
        if (mAllScore.size() > 0) {
            for (Integer number : mAllScore) {
                sum += number;
            }
        }
        return Integer.toString(sum);
    }

    public String getLastScore() {
        String lastScore = mAllScore.size() > 0 ? mAllScore.get(mAllScore.size() - 1).toString() : Integer.toString(0);
        return lastScore;
    }

    public String getBestScoreSoFar() {
        int maxScore = mAllScore.size() > 0 ? Collections.max(mAllScore) : 0;
        return Integer.toString(maxScore);
    }

}