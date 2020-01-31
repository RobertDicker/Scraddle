package com.robbies.scraddle;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

class GameRepository {

    private GameDao gameDao;
    private LiveData<List<Player>> allPlayers;


    GameRepository(Application application) {

        GameRoomDatabase db = GameRoomDatabase.getDatabase(application);
        gameDao = db.gameDao();
        this.allPlayers = gameDao.getAllPlayers();

    }

    LiveData<List<Player>> getAllPlayers() {
        return this.allPlayers;
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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return matchId;
    }

    void insertScore(Score score) {
        new insertScoreAsyncTask(gameDao).execute(score);
    }

    void deleteMatch(long matchId) {
        new deleteMatchAsyncTask(gameDao).execute(matchId);
    }

    void deletePlayer(int playerId) {
        new deletePlayerAsyncTask(gameDao).execute(playerId);
    }

    void deleteAll() {
        new deleteAll(gameDao).execute();
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
            return mAsyncTaskDao.insert(matches[0]);
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


    private static class deleteMatchAsyncTask extends AsyncTask<Long, Void, Void> {

        private GameDao mAsyncTaskDao;

        deleteMatchAsyncTask(GameDao gameDao) {
            this.mAsyncTaskDao = gameDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.delete(longs[0]);
            return null;
        }
    }


    private static class deletePlayerAsyncTask extends AsyncTask<Integer, Void, Void> {

        private GameDao mAsyncTaskDao;

        deletePlayerAsyncTask(GameDao gameDao) {
            this.mAsyncTaskDao = gameDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncTaskDao.deletePlayer(integers[0]);
            return null;
        }
    }

    private static class deleteAll extends AsyncTask<Void, Void, Void> {

        private GameDao mAsyncTaskDao;

        deleteAll(GameDao gameDao) {
            this.mAsyncTaskDao = gameDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllPlayers();
            mAsyncTaskDao.deleteAllMatches();
            return null;
        }
    }


}
