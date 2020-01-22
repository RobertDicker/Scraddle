package com.robbies.scraddle;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;
    private LiveData<List<Word>> mMatchingWords;

    public WordViewModel(Application application) {
        super(application);

        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();


    }


    LiveData<List<Word>> getAllWords() {
        return this.mAllWords;
    }

    LiveData<List<Word>> getAnagramsOf(String anagramPrimeValue, int minimumLength, int maxLength) {
        mMatchingWords = mRepository.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength);
        return this.mMatchingWords;
    }

    void insertWord(Word word) {
        mRepository.insertWord(word);
    }


}
