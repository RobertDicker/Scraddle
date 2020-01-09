package com.robbies.scraddle;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {

        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);

        this.mWordDao = db.wordDao();
        this.mAllWords = mWordDao.getAllWords();

    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }


    void insertWord(Word word) {

        new insertWordAsyncTask(mWordDao).execute(word);

    }

    LiveData<List<Word>> getMatchingPrimeWords(String anagramPrimeValue, int minimumLength, int maxLength) {
        return mWordDao.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength);
    }


    private static class insertWordAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;


        insertWordAsyncTask(WordDao wordDao) {

            this.mAsyncTaskDao = wordDao;
        }


        @Override
        protected Void doInBackground(final Word... words) {

            mAsyncTaskDao.insert(words[0]);
            return null;

        }
    }

}
