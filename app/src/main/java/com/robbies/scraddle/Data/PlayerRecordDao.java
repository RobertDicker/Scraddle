package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class PlayerRecordDao extends BaseDao<PlayerRecord> {

    @Query("SELECT * FROM player_record_table")
    abstract LiveData<List<PlayerRecord>> getAllPlayerRecords();


}
