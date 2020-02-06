package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface GameDao {

/*

    //Player Access
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Player player);

    @Query("SELECT * FROM player_table ORDER BY lastPlayed DESC")
    LiveData<List<Player>> getAllPlayers();

    //Player Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlayerRecord playerRecord);

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

    @Transaction



/*
 @Query("SELECT * FROM player_record_table")
    LiveData<List<PlayerRecord>> getPlayerRecords();
 */



@Query("SELECT p.name, r.personalBest, r.highestMatchScore, p.playerId, r.wins, r.loss, r.draw, s.matchId, s.score, s.playersTurnOrder, s.totalScore, s.maxScore, s.result FROM player_table p LEFT JOIN score_table s ON p.playerId = s.playerId  LEFT JOIN player_record_table r ON s.playerId = r.playerId WHERE :matchId = s.matchId ORDER BY s.playersTurnOrder")
LiveData<List<GameDetail>> getGameDetails(long matchId);
}

