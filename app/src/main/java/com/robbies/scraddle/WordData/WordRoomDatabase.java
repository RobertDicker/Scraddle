package com.robbies.scraddle.WordData;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    private static WordRoomDatabase INSTANCE;
    //private static ArrayList<String> mWordList;
    private static WordRoomDatabase.Callback sWordRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    //     new PopulateDbAsync(INSTANCE).execute();
                }
            };

    static WordRoomDatabase getDatabase(final Context context) {


        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    // mWordList = WordList.getInstance(context).getAllWords();

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_table").createFromAsset("words.db")
                            //TODO add migration strategy to replace destructive migration
                            .fallbackToDestructiveMigration()
                            .addCallback(sWordRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract WordDao wordDao();


}
