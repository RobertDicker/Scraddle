package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {


    //Player Access
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player player);

    @Query("SELECT * FROM player_table ORDER BY lastPlayed DESC")
    LiveData<List<Player>> getAllPlayers();


    //Delete
    @Query("DELETE FROM match_table")
    void deleteAllMatches();

    @Query("DELETE FROM player_table")
    void deleteAllPlayers();

    @Query("DELETE FROM match_table WHERE matchId == :matchId")
    void delete(long matchId);

    @Query("DELETE FROM player_table WHERE playerId == :playerId")
    void deletePlayer(int playerId);

    //Match Access

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Match match);

    //Get Last Match
    @Query("SELECT * FROM match_table ORDER BY matchId DESC LIMIT 1")
    LiveData<Match> getLastMatch();

    //Score Access

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Score score);

    //Game Detail (Score and Player)

    @Query("SELECT p.name, p.personalBest, highestMatchScore, p.playerId, wins, loss, draw, matchId, score, playersTurnOrder, totalScore, maxScore, result FROM player_table p JOIN score_table s ON p.playerId == s.playerId WHERE :matchId == s.matchId ORDER BY s.playersTurnOrder")
    LiveData<List<GameDetail>> getGameDetails(long matchId);


}
