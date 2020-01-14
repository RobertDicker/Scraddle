package com.robbies.scraddle;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ScoringViewModel extends AndroidViewModel {

    private GameRepository gameRepository;
    private MutableLiveData<List<Player>> mMatchPlayers;
    private MutableLiveData<List<Score>> mMatchScores;
    private Match mCurrentMatch;

    public ScoringViewModel(@NonNull Application application) {
        super(application);
        mCurrentMatch = gameRepository.getLastMatch().getValue();
        mMatchScores = gameRepository.getScoresFromMatch(mCurrentMatch.getMatchId());
        mMatchPlayers = gameRepository.getPlayersFromMatch(mCurrentMatch.getMatchId());
    }

    public MutableLiveData<List<Player>> getMatchPlayers() {
        return this.mMatchPlayers;
    }

    public MutableLiveData<List<Score>> getMatchScores() {
        return this.mMatchScores;
    }

}
