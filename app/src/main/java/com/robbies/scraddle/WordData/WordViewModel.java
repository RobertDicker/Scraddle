package com.robbies.scraddle.WordData;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(Application application) {
        super(application);

        mRepository = WordRepository.getInstance(application);
        mAllWords = mRepository.getAllWords();

    }


    public LiveData<List<Word>> getAllWords() {
        return this.mAllWords;
    }

    public LiveData<List<Word>> getAnagramsOf(String anagramPrimeValue, int minimumLength, int maxLength) {
        return mRepository.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength);

    }

    public void insertWord(Word word) {
        mRepository.insertWord(word);
    }


    public LiveData<List<Word>> getAllWords(int minWordLength, int maxWordLength) {

        return mRepository.getAllWords(minWordLength, maxWordLength);

    }
}
