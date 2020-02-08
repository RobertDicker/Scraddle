package com.robbies.scraddle.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public abstract class BaseDao<T> {

    /**
     * @param entity
     * @return long
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(T entity);

    /**
     * Insert a list of objects into the database
     *
     * @param entity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(T... entity);

    /**
     * Update the object
     *
     * @param entity
     */
    @Update
    abstract void update(T entity);

    /**
     * delete the object
     *
     * @param entity
     */
    @Delete
    abstract void delete(T entity);


}