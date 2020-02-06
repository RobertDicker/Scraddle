package com.robbies.scraddle.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class PlayerDao extends BaseDao<Player> {

    @Query("SELECT * FROM player_table ORDER BY lastPlayed DESC")
    abstract LiveData<List<Player>> getAllPlayers();

    @Query("DELETE FROM player_table")
    abstract void deleteAllPlayers();

    @Query("DELETE FROM player_table WHERE playerId = :playerId")
    abstract void deletePlayer(long playerId);


}
