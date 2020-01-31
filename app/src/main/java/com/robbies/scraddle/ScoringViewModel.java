package com.robbies.scraddle;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ScoringViewModel extends AndroidViewModel {

    private GameRepository gameRepository;
    private LiveData<List<Player>> mAllPlayers;
    private long mCurrentMatchId;

    public ScoringViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

    //GETTERS

    LiveData<List<GameDetail>> getPlayersDetails(long matchId) {
        return gameRepository.getGameDetails(matchId);
    }

    LiveData<List<Player>> getAllPlayers() {

        if (mAllPlayers == null) {
            this.mAllPlayers = gameRepository.getAllPlayers();
        }
        return mAllPlayers;
    }

    long getCurrentMatchId() {
        if (mCurrentMatchId == -1) {
            mCurrentMatchId = insertMatch(new Match());
        }
        return mCurrentMatchId;
    }


    //INSERTS

    void setCurrentMatchId(long mCurrentMatchId) {
        this.mCurrentMatchId = mCurrentMatchId;
    }

    private long insertMatch(Match match) {
        this.mCurrentMatchId = gameRepository.insertMatch(match);
        return mCurrentMatchId;
    }

    void savePlayer(Player player) {
        gameRepository.insertPlayer(player);
        Log.d("==SAVING===>", "saving" + player.toString());
    }

    void saveScore(Score score) {
        gameRepository.insertScore(score);
        Log.d("==SAVING===>", "saving" + score.toString());
    }

    //DELETES

    //Deletes matching scores and match from data
    void deleteMatch(long matchId) {
        gameRepository.deleteMatch(matchId);
    }

    void deletePlayer(int playerId) {
        gameRepository.deletePlayer(playerId);
    }

    void deleteAll() {
        gameRepository.deleteAll();
    }

}
