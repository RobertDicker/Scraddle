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

    @Query("SELECT p.name, r.personalBest, r.highestMatchScore, p.playerId, r.wins, r.loss, r.draw, s.matchId, s.score, s.playersTurnOrder, s.totalScore, s.maxScore, s.result FROM player_table p LEFT JOIN score_table s ON p.playerId = s.playerId  LEFT JOIN player_record_table r ON s.playerId = r.playerId WHERE :matchId = s.matchId ORDER BY s.playersTurnOrder")
    LiveData<List<GameDetail>> getGameDetails(long matchId);


    @Query("SELECT p.name, p.playerId, r.wins, r.loss, r.draw, r.personalBest, r.highestMatchScore FROM player_table p JOIN player_record_table r ON p.playerId == r.playerId")
    LiveData<List<PlayersAndRecords>> getAllPlayersAndRecords();


}

