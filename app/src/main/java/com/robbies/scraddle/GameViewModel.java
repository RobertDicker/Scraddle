package com.robbies.scraddle;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class GameViewModel extends AndroidViewModel {

    private GameRepository mGameRepository;
    private LiveData<List<Player>> mAllPlayers;
    private LiveData<List<Match>> mAllMatches;
    private List<Player> mCurrentMatchPlayer;
    private List<Score> currentScores;
    private MediatorLiveData mediatorLiveData = new MediatorLiveData<>();


    public GameViewModel(@NonNull Application application) {
        super(application);
        mGameRepository = new GameRepository(application);

        mAllPlayers = mGameRepository.getAllPlayers();
        mAllMatches = mGameRepository.getAllMatches();


    }

    public LiveData<List<Match>> getAllMatches() {
        return mAllMatches;
    }

    public LiveData<List<Player>> getAllPlayers() {
        Log.d("---", "getting players");
        return mAllPlayers;
    }


    public LiveData<Player> getPlayer(int playerId) {
        return mGameRepository.getPlayer(playerId);
    }

    public LiveData<Match> getLastMatch() {
        return mGameRepository.getLastMatch();
    }


    public LiveData<List<Score>> getScoresFromMatch(long matchId) {
        return mGameRepository.getScoresFromMatch(matchId);
    }

    public LiveData<List<Player>> getPlayersFromMatch(long matchId) {
        return mGameRepository.getPlayersFromMatch(matchId);
    }

    public LiveData<Integer> getResultCountForPlayer(int result, int playerId) {

        return mGameRepository.getResultCountForPlayer(result, playerId);
    }

    public Player getPlayerFromId(int id) {
        return mGameRepository.getPlayerFromId(id);
    }


    //INSERTS

    public void insertPlayer(Player player) {
        mGameRepository.insertPlayer(player);
    }

    public long insertMatch(Match match) {
        return mGameRepository.insertMatch(match);
    }

    public void insertScore(Score score) {
        mGameRepository.insertScore(score);
    }


    public void deleteMatch(long matchId) {
        mGameRepository.deleteMatch(matchId);
    }


}


