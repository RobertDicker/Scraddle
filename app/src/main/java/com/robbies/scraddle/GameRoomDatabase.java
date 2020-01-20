package com.robbies.scraddle;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class, Match.class, Score.class}, version = 1)

public abstract class GameRoomDatabase extends RoomDatabase {


    private static GameRoomDatabase INSTANCE;


    public static GameRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (GameRoomDatabase.class) {
                if (INSTANCE == null) {


                    INSTANCE = Room.databaseBuilder(context, GameRoomDatabase.class, "game_table")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }


        }

        return INSTANCE;
    }

    public abstract GameDao gameDao();


}
