package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
abstract class MatchDao extends BaseDao<Match> {

    @Query("SELECT * FROM match_table")
    abstract LiveData<List<Match>> getAllMatches();

    @Query("SELECT * FROM match_table WHERE matchId = :matchId")
    abstract LiveData<Match> getMatchScores(long matchId);

    @Query("SELECT * FROM match_table ORDER BY matchId DESC LIMIT 1")
    abstract LiveData<Match> getLastMatch();

    @Query("DELETE FROM match_table")
    public abstract void deleteAllMatches();

    @Query("DELETE FROM match_table where matchId = :matchId")
    abstract void delete(long matchId);
}
