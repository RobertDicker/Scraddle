package com.robbies.scraddle;

import java.util.ArrayList;
import java.util.TreeMap;

public class GameData {

    private static final GameData gameDataHolder = new GameData();
    private TreeMap<String, Player> mAllPlayers;
    private ArrayList<Match> mAllMatches;

    private GameData() {

        mAllPlayers = new TreeMap<>();

        Player player1 = new Player("Robbie");
        Player player2 = new Player("Katherine");
        Player player3 = new Player("Steve");

        mAllPlayers.put(player1.getPlayerID(), player1);
        mAllPlayers.put(player2.getPlayerID(), player2);
        mAllPlayers.put(player3.getPlayerID(), player3);

    }

    public static GameData getInstance() {
        return gameDataHolder;
    }

    public TreeMap<String, Player> getAllPlayers() {
        return mAllPlayers;
    }


    public ArrayList<Match> getAllMatches() {
        return mAllMatches;
    }

    public void addMatchtoHistory(Match match) {
        mAllMatches.add(match);
    }


}
