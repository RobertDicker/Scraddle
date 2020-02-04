package com.robbies.scraddle.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ScoringViewModel extends AndroidViewModel {

    private final GameRepository gameRepository;
    private LiveData<List<Player>> mAllPlayers;
    private long mCurrentMatchId = -1;

    public ScoringViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

    //GETTERS

    public LiveData<List<GameDetail>> getPlayersDetails(long matchId) {
        return gameRepository.getGameDetails(matchId);
    }

    public LiveData<List<Player>> getAllPlayers() {

        if (mAllPlayers == null) {
            this.mAllPlayers = gameRepository.getAllPlayers();
        }
        return mAllPlayers;
    }

    public long getCurrentMatchId() {
        return mCurrentMatchId;
    }


    //INSERTS

    public void setCurrentMatchId(long mCurrentMatchId) {
        this.mCurrentMatchId = mCurrentMatchId;
    }

    public long insertMatch(Match match) {
        this.mCurrentMatchId = gameRepository.insertMatch(match);
        return mCurrentMatchId;
    }

    public void savePlayer(Player player) {
        gameRepository.insertPlayer(player);

    }

    public void saveScore(Score score) {
        gameRepository.insertScore(score);

    }

    //DELETES

    //Deletes matching scores and match from data
    public void deleteMatch(long matchId) {
        gameRepository.deleteMatch(matchId);
    }

    public void deletePlayer(int playerId) {
        gameRepository.deletePlayer(playerId);
    }

    public void deleteAll() {
        gameRepository.deleteAll();
    }

}
