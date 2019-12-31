package com.robbies.scraddle;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String mName;
    private List<Integer> mAllScores;
    private int mHighScore = 0;
    private int mPersonalBest = 0;
    private boolean mPlayersTurn = false;


    public Player(String name) {
        mAllScores = new ArrayList<Integer>();
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void addScore(String scoreText) {
        int score = Integer.parseInt(scoreText);
        mAllScores.add(score);
        if (score > mHighScore) {
            mHighScore = score;
            if (mHighScore > mPersonalBest) {
                mPersonalBest = score;
            }
        }
    }

    public int getTotal() {
        int total = 0;
        if (mAllScores == null || mAllScores.isEmpty()) {
            return 0;
        }

        for (int score : mAllScores
        ) {
            total += score;

        }
        return total;

    }

    public String getHighScore() {
        return Integer.toString(mHighScore);
    }

    public String getPersonalBest() {
        return Integer.toString(mPersonalBest);

    }

    public String getLastScore() {
        String lastScore = "0";

        if (mAllScores.size() > 0) {
            lastScore = Integer.toString(mAllScores.get(mAllScores.size() - 1));
        }
        return lastScore;
    }

    public void setPlayerTurn(Boolean turn) {
        this.mPlayersTurn = turn;
    }

    public boolean getPlayerStatus() {
        return this.mPlayersTurn;
    }

}
