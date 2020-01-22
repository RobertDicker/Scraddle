package com.robbies.scraddle;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ScoringViewModel extends AndroidViewModel {

    private GameRepository gameRepository;
    private LiveData<Match> mCurrentMatch;

    public ScoringViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
        mCurrentMatch = gameRepository.getLastMatch();
    }


    LiveData<List<GameDetail>> getPlayersDetails(long matchId) {
        return gameRepository.getGameDetails(matchId);
    }

    void savePlayer(Player player) {
        gameRepository.insertPlayer(player);
        Log.d("==SAVING===>", "saving" + player.toString());
    }

    void saveScore(Score score) {
        gameRepository.insertScore(score);
        Log.d("==SAVING===>", "saving" + score.toString());
    }

    //Deletes matching scores and match from data
    void deleteMatch(long matchId) {
        gameRepository.deleteMatch(matchId);
    }

    LiveData<Match> getCurrentMatch() {
        return mCurrentMatch;
    }

}
