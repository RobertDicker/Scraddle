package com.robbies.scraddle;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameRepository {

    private GameDao gameDao;
    private LiveData<List<Player>> allPlayers;
    private LiveData<List<Match>> allMatches;


    public GameRepository(Application application) {

        GameRoomDatabase db = GameRoomDatabase.getDatabase(application);
        gameDao = db.gameDao();
        this.allPlayers = gameDao.getAllPlayers();
        this.allMatches = gameDao.getAllMatches();
    }

    LiveData<List<Player>> getAllPlayers() {
        return this.allPlayers;
    }

    LiveData<List<Match>> getAllMatches() {
        return this.allMatches;
    }

    LiveData<List<Player>> getPlayersFromMatch(long matchId) {
        return gameDao.getPlayersFromMatch(matchId);
    }

    LiveData<List<Score>> getScoresFromMatch(long matchId) {
        return gameDao.getScoresFromMatch(matchId);
    }

    LiveData<Match> getLastMatch() {
        return gameDao.getLastMatch();

    }


    LiveData<List<GameDetail>> getGameDetails(long matchId) {
        return gameDao.getGameDetails(matchId);
    }


    //INSERTS

    void insertPlayer(Player player) {
        new insertPlayerAsyncTask(gameDao).execute(player);
    }

    long insertMatch(Match match) {

        long matchId = 0;
        try {
            matchId = new insertMatchAsyncTask(gameDao).execute(match).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matchId;

    }

    void insertScore(Score score) {
        new insertScoreAsyncTask(gameDao).execute(score);
    }

    public LiveData<Player> getPlayer(int playerId) {
        return gameDao.getPlayer(playerId);
    }

    public LiveData<Integer> getResultCountForPlayer(int result, int playerId) {
        return gameDao.getResultCountForPlayer(result, playerId);
    }

    public void deleteMatch(long matchId) {
        new deleteMatchAsyncTask(gameDao).execute(matchId);
    }

    public Player getPlayerFromId(int id) {
        return gameDao.getPlayerFromId(id);
    }


    private static class insertPlayerAsyncTask extends AsyncTask<Player, Void, Void> {

        private GameDao mAsyncTaskDao;


        insertPlayerAsyncTask(GameDao gameDao) {

            this.mAsyncTaskDao = gameDao;
        }


        @Override
        protected Void doInBackground(final Player... players) {


            mAsyncTaskDao.insert(players[0]);


            return null;

        }
    }


    private static class insertMatchAsyncTask extends AsyncTask<Match, Void, Long> {

        private GameDao mAsyncTaskDao;


        insertMatchAsyncTask(GameDao gameDao) {

            this.mAsyncTaskDao = gameDao;
        }


        @Override
        protected Long doInBackground(final Match... matches) {
            long matchId = mAsyncTaskDao.insert(matches[0]);
            return matchId;


        }


    }


    private static class insertScoreAsyncTask extends AsyncTask<Score, Void, Void> {

        private GameDao mAsyncTaskDao;


        insertScoreAsyncTask(GameDao gameDao) {

            this.mAsyncTaskDao = gameDao;
        }


        @Override
        protected Void doInBackground(final Score... scores) {


            mAsyncTaskDao.insert(scores[0]);

            return null;

        }
    }


    private class deleteMatchAsyncTask extends AsyncTask<Long, Void, Void> {

        private GameDao mAsyncTaskDao;

        public deleteMatchAsyncTask(GameDao gameDao) {
            this.mAsyncTaskDao = gameDao;
        }


        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.delete(longs[0]);
            return null;
        }
    }
}
