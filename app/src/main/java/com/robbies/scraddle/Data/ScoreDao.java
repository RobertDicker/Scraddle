package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;



@Dao
abstract class ScoreDao extends BaseDao<Score> {

    @Query("SELECT * FROM score_table")
    abstract LiveData<List<Score>> getAllScores();

    @Query("SELECT * FROM score_table WHERE matchId = :matchId")
    abstract LiveData<List<Score>> getMatchScores(long matchId);


}
