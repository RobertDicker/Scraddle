package com.robbies.scraddle;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {


    private String mName;
    private int mPersonalBest;
    private String mPlayerID;
    private boolean mPlayersTurn;


    public Player(String name) {
        mPersonalBest = 0;
        mPlayerID = UUID.randomUUID().toString();
        mPlayersTurn = false;
        this.mName = name;


    }

    public String getName() {
        return mName;
    }



    public String getPlayerID() {
        return this.mPlayerID;
    }


    public int getPersonalBest() {
        return mPersonalBest;

    }

    public void setPersonalBest(int score) {
        this.mPersonalBest = score;
    }

    public void setPlayerTurn(Boolean turn) {
        this.mPlayersTurn = turn;
    }

    public boolean getPlayerStatus() {
        return this.mPlayersTurn;
    }

}
