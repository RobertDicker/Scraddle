package com.robbies.scraddle.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public abstract class BaseDao<T> {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(T entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(T... entity);

    @Update
    abstract void update(T entity);

    @Delete
    abstract void delete(T entity);


}