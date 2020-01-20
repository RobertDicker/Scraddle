package com.robbies.scraddle;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {


    //Player Access
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player player);

    @Query("DELETE FROM player_table")
    void deleteAllPlayers();

    @Query("SELECT * FROM player_table WHERE playerId == :playerId")
    LiveData<Player> getPlayer(int playerId);

    @Query("SELECT * FROM player_table ORDER BY lastPlayed DESC")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT player_table.* FROM player_table INNER JOIN score_table ON player_table.playerId == score_table.playerId WHERE score_table.matchId == :matchId")
    LiveData<List<Player>> getPlayersFromMatch(long matchId);


    //Match Access

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Match match);

    //Get Last Match
    @Query("SELECT * FROM match_table ORDER BY matchId DESC LIMIT 1")
    LiveData<Match> getLastMatch();


    //Get All Matches
    @Query("SELECT * FROM match_table ORDER BY datePlayed DESC")
    LiveData<List<Match>> getAllMatches();


    //Score Access

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Score score);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Score score);

    @Query("SELECT * FROM score_table ORDER BY matchId")
    LiveData<List<Score>> getAllScores();

    @Query("SELECT * FROM score_table WHERE :matchId == matchId ORDER BY playersTurnOrder")
    LiveData<List<Score>> getScoresFromMatch(long matchId);

    @Query("SELECT * FROM score_table WHERE :playerId == playerId AND matchID == :matchId")
    LiveData<Score> getPlayerScore(int playerId, int matchId);

    @Query("SELECT COUNT(*) FROM score_table WHERE :playerId == playerId AND :result == result")
    LiveData<Integer> getResultCountForPlayer(int result, int playerId);

    @Query("DELETE FROM match_table WHERE matchId == :matchId")
    void delete(long matchId);

    @Query("SELECT * FROM player_table WHERE :id == playerId")
    Player getPlayerFromId(int id);


    @Query("SELECT * FROM player_table p JOIN score_table s ON p.playerId == s.playerId WHERE :matchId == s.matchId ORDER BY s.playersTurnOrder")
    LiveData<List<GameDetail>> getGameDetails(long matchId);
}
