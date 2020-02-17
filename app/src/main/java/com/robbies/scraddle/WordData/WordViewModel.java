package com.robbies.scraddle.WordData;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;
    private MediatorLiveData<Map<Word, Integer>> mUIObservedAnagrams;
    private MutableLiveData<Map<Word, Integer>> mAllRetrievedAnagrams;
    private MutableLiveData<Map<Word, Integer>> selectedAnagrams;


    public WordViewModel(final Application application) {
        super(application);
        mRepository = WordRepository.getInstance(application);
        mAllWords = mRepository.getAllWords();
        mUIObservedAnagrams = new MediatorLiveData<>();
        mAllRetrievedAnagrams = new MutableLiveData<>();
        selectedAnagrams = new MutableLiveData<>();


        mUIObservedAnagrams.addSource(selectedAnagrams, new Observer<Map<Word, Integer>>() {
            @Override
            public void onChanged(Map<Word, Integer> wordIntegerMap) {
                mUIObservedAnagrams.setValue(wordIntegerMap);
            }
        });


        mUIObservedAnagrams.addSource(mAllRetrievedAnagrams, new Observer<Map<Word, Integer>>() {
            @Override
            public void onChanged(Map<Word, Integer> wordIntegerMap) {
                mUIObservedAnagrams.setValue(wordIntegerMap);
            }
        });
    }

    public void setAllAnagramsCache(Map<Word, Integer> allAnagrams) {
        mAllRetrievedAnagrams.setValue(allAnagrams);
    }

    public LiveData<Map<Word, Integer>> getAnagrams() {
        return mUIObservedAnagrams;
    }

    public LiveData<List<Word>> getAllWords() {
        return this.mAllWords;
    }

    public void getAnagramsOf(String anagramPrimeValue, int minimumLength, int maxLength) {
        Log.d("Started", "==ENTERED GETANAGRAMOF looking for words");

        mUIObservedAnagrams.addSource(mRepository.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {

                Log.d("Started", "==Searching for words and creating map completed");
                Map<Word, Integer> newMap = new ConcurrentHashMap<>();
                for (Word word : words) {
                    newMap.put(word, word.getWord().length());
                }
                Log.d("DONE", "==Searching for words and creating map completed");
                mAllRetrievedAnagrams.setValue(newMap);

            }


        });

    }

    public void insertWord(Word word) {
        mRepository.insertWord(word);
    }

    public void setSelectedAnagrams(Map<Word, Integer> selection) {
        selectedAnagrams.setValue(selection);

    }

    public LiveData<String> getDefinition(String word) {
        return mRepository.getDefinition(word);
    }

    public LiveData<List<WordAndDefinition>> getMatchingWordsForCrossword(String letters) {
        return mRepository.getMatchingCrosswordWords(letters);

    }


}
