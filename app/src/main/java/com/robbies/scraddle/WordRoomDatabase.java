package com.robbies.scraddle;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
                    new PopulateDbAsync(INSTANCE).execute();
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

    //Populate DB in background
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mWordDao;


        PopulateDbAsync(WordRoomDatabase db) {
            mWordDao = db.wordDao();

        }

        @Override
        protected Void doInBackground(final Void... params) {


            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
/*            mWordDao.deleteAll();


            for (int i = 0; i <= mWords.size() - 1; i++) {
                Word word = new Word(mWords.get(i));
                Log.d("----WORD---", word.getWord());
                mWordDao.insert(word);
            }*/

            // If we have no words, then create the initial list of words
            if (mWordDao.getAnyWord().length < 1) {
                Log.d("WordRoomDatabase", "------APPARENTLY THE DATABASE IS EMPTY, FIX IT!");
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
