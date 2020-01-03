package com.robbies.scraddle;

import java.util.ArrayList;

public class GameDataAdapter {

    private GameData gameData = GameData.getInstance();


    public GameDataAdapter() {
    }

    public ArrayList<Player> getAllPlayers() {
        return new ArrayList<>(gameData.getAllPlayers().values());
    }

    public ArrayList<Match> getAllMatches() {
        return gameData.getAllMatches();
    }

    public Player getIndividualPlayer(String playerId) {

        return gameData.getAllPlayers().get(playerId);
    }

    public void addMatch(Match match) {
        gameData.addMatchtoHistory(match);
    }


}
