package com.robbies.scraddle;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;
    private static WordRepository INSTANCE = null;

    WordRepository(Application application) {

        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        Log.d("WORD REPOSITORY", "CREATED A NEW WORD REPO");
        this.mWordDao = db.wordDao();
        this.mAllWords = mWordDao.getAllWords();

    }

    public static WordRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new WordRepository(application);
        }
        return INSTANCE;

    }

    LiveData<List<Word>> getAllWords() {

        Log.d("WORD REPOSITORY", "CREATED A NEW WORD ALLWOIRDS");
        return mAllWords;
    }


    void insertWord(Word word) {

        new insertWordAsyncTask(mWordDao).execute(word);

    }

    LiveData<List<Word>> getMatchingPrimeWords(String anagramPrimeValue, int minimumLength, int maxLength) {

        return mWordDao.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength);
    }

    public LiveData<List<Word>> getAllWords(int minWordLength, int maxWordLength) {
        return mWordDao.getAllWords(minWordLength, maxWordLength);
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
