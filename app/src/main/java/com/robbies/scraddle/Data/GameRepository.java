package com.robbies.scraddle.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameRepository {

    private final GameDao gameDao;
    private final PlayerDao playerDao;
    private final MatchDao matchDao;
    private final ScoreDao scoreDao;
    private final PlayerRecordDao playerRecordDao;

    private LiveData<List<Player>> allPlayers;


    GameRepository(Application application) {

        GameRoomDatabase db = GameRoomDatabase.getDatabase(application);
        gameDao = db.gameDao();
        playerDao = db.playerDao();
        scoreDao = db.scoreDao();
        matchDao = db.matchDao();
        playerRecordDao = db.playerRecordDao();

        this.allPlayers = playerDao.getAllPlayers();

    }

    LiveData<List<Player>> getAllPlayers() {
        return this.allPlayers;
    }

    LiveData<Match> getLastMatch() {
        return matchDao.getLastMatch();
    }

    LiveData<List<GameDetail>> getGameDetails(long matchId) {
        return gameDao.getGameDetails(matchId);
    }
    LiveData<List<PlayersAndRecords>> getAllPlayersAndRecords() {
      return gameDao.getAllPlayersAndRecords();
    }


    //INSERTS

    long insertPlayer(Player player)
    {
        long playerId = -1;

        try {
           playerId = new insertPlayerAsyncTask(playerDao).execute(player).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return playerId;
    }

    void insertPlayerRecord(PlayerRecord playerRecord) {
        new insertPlayerRecordAsyncTask(playerRecordDao).execute(playerRecord);
    }

    long insertMatch(Match match) {

        long matchId = 0;
        try {
            matchId = new insertMatchAsyncTask(matchDao).execute(match).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return matchId;
    }

    void insertScore(Score score) {
        new insertScoreAsyncTask(scoreDao).execute(score);
    }

    void deleteMatch(long matchId) {
        new deleteMatchAsyncTask(matchDao).execute(matchId);
    }

    void deletePlayer(long playerId) {
        new deletePlayerAsyncTask(playerDao).execute(playerId);
    }

    void deleteAll() {
        new deleteAll(matchDao, playerDao).execute();
    }




    private static class insertPlayerAsyncTask extends AsyncTask<Player, Void, Long> {

        private PlayerDao mAsyncTaskDao;

        insertPlayerAsyncTask(PlayerDao player) {
            this.mAsyncTaskDao = player;
        }

        @Override
        protected Long doInBackground(final Player... players) {

            return mAsyncTaskDao.insert(players[0]);

        }
    }


    private static class insertPlayerRecordAsyncTask extends AsyncTask<PlayerRecord, Void, Void> {

        private PlayerRecordDao mAsyncTaskDao;

        insertPlayerRecordAsyncTask(PlayerRecordDao playerRecordDao) {
            this.mAsyncTaskDao = playerRecordDao;
        }

        @Override
        protected Void doInBackground(final PlayerRecord... playersRecord) {
            mAsyncTaskDao.insert(playersRecord[0]);
            return null;
        }
    }



    private static class insertMatchAsyncTask extends AsyncTask<Match, Void, Long> {

        private MatchDao mAsyncTaskDao;

        insertMatchAsyncTask(MatchDao matchDao) {
            this.mAsyncTaskDao = matchDao;
        }

        @Override
        protected Long doInBackground(final Match... matches) {
            return mAsyncTaskDao.insert(matches[0]);
        }
    }


    private static class insertScoreAsyncTask extends AsyncTask<Score, Void, Void> {

        private ScoreDao mAsyncTaskDao;

        insertScoreAsyncTask(ScoreDao scoreDao) {
            this.mAsyncTaskDao = scoreDao;
        }

        @Override
        protected Void doInBackground(final Score... scores) {
            mAsyncTaskDao.insert(scores[0]);
            return null;
        }
    }


    private static class deleteMatchAsyncTask extends AsyncTask<Long, Void, Void> {

        private MatchDao mAsyncTaskDao;

        deleteMatchAsyncTask(MatchDao matchDao) {
            this.mAsyncTaskDao = matchDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            mAsyncTaskDao.delete(longs[0]);
            return null;
        }
    }


    private static class deletePlayerAsyncTask extends AsyncTask<Long, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        deletePlayerAsyncTask(PlayerDao playerDao) {
            this.mAsyncTaskDao = playerDao;
        }

        @Override
        protected Void doInBackground(Long... playerIds) {
            mAsyncTaskDao.deletePlayer(playerIds[0]);
            return null;
        }
    }

    private static class deleteAll extends AsyncTask<Void, Void, Void> {

        private MatchDao matchAsyncTaskDao;
        private PlayerDao playerAsyncTaskDao;

        deleteAll(MatchDao matchDao, PlayerDao playerDao) {

            this.matchAsyncTaskDao = matchDao;
            this.playerAsyncTaskDao = playerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            playerAsyncTaskDao.deleteAllPlayers();
            matchAsyncTaskDao.deleteAllMatches();
            return null;
        }
    }



}
