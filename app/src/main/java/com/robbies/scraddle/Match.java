package com.robbies.scraddle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

class Match implements Serializable {

    private String mMatchId;
    private TreeMap<String, Score> mPlayersAndScores = new TreeMap<>();


    //Precondition: must have at least one player
    //Postcondition a Player and a Score set is created
    public Match(ArrayList<String> players) {

        if (players.size() > 0) {
            for (String playerId : players) {
                mPlayersAndScores.put(playerId, new Score());
            }
        }
        mMatchId = UUID.randomUUID().toString();
    }

    public Match(String matchId, TreeMap<String, Score> oldMatch) {
        this.mMatchId = matchId;
        this.mPlayersAndScores = oldMatch;
    }

    public Map<String, Score> getmPlayersAndScores() {
        return mPlayersAndScores;
    }

    public Score getPlayerScore(String playerId) {
        return mPlayersAndScores.get(playerId);
    }

    public void addScoreToPlayer(String playerId, int score) {
        mPlayersAndScores.get(playerId).addToScore(score);
    }

    public ArrayList<String> getPlayerIds() {
        ArrayList<String> playerIds = new ArrayList<>();
        playerIds.addAll(mPlayersAndScores.keySet());
        return playerIds;
    }
}




