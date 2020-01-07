package com.robbies.scraddle;

import android.content.Context;

import java.util.ArrayList;

public class GameDataAdapter {

    private Context context;
    private GameData gameData;

    public GameDataAdapter(Context context) {
        this.context = context;
        this.gameData = GameData.getInstance(context);

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

    public void updatePlayerData(Player player) {
        gameData.updatePlayer(player);
    }

    public void persistGameData() {
        gameData.persistMatchData(context);
        gameData.persistPlayerData(context);

    }


}
