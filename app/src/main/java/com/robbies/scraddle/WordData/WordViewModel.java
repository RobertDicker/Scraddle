package com.robbies.scraddle.WordData;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WordViewModel extends AndroidViewModel {

    private final List<Word> mAllAnagramList;
    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;
    private MutableLiveData<String> mLetters;
    private MediatorLiveData<List<Word>> mUIObservedAnagrams;
    private MutableLiveData<Map<Integer, List<Word>>> mAllRetrievedAnagrams;
    private MutableLiveData<List<Word>> selectedAnagrams;


    public WordViewModel(final Application application) {
        super(application);
        mAllAnagramList = new ArrayList<>();
        mRepository = WordRepository.getInstance(application);
        mAllWords = mRepository.getAllWords();
        mUIObservedAnagrams = new MediatorLiveData<>();
        mAllRetrievedAnagrams = new MutableLiveData<>();
        selectedAnagrams = new MutableLiveData<>();
        mLetters = new MutableLiveData<>();
        mLetters.setValue("");


        mUIObservedAnagrams.addSource(selectedAnagrams, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                mUIObservedAnagrams.setValue(words);
            }
        });


        mUIObservedAnagrams.addSource(mAllRetrievedAnagrams, new Observer<Map<Integer, List<Word>>>() {
            @Override
            public void onChanged(Map<Integer, List<Word>> wordIntegerMap) {

                List<Word> allWords = new ArrayList<>();
                for (List<Word> list : wordIntegerMap.values()) {
                    allWords.addAll(list);
                }
                mUIObservedAnagrams.setValue(allWords);
            }
        });
    }

    public void setAllAnagramsCache(Map<Integer, List<Word>> allAnagrams) {

        for (List<Word> lists : allAnagrams.values()) {

            mAllAnagramList.addAll(lists);
        }


        mAllRetrievedAnagrams.setValue(allAnagrams);
    }

    public LiveData<List<Word>> getAnagrams() {
        return mUIObservedAnagrams;
    }

    public LiveData<List<Word>> getAllWords() {
        return this.mAllWords;
    }

    public void getAnagramsOf(String anagramPrimeValue, int minimumLength, int maxLength) {

        mUIObservedAnagrams.addSource(mRepository.getMatchingPrimeWords(anagramPrimeValue, minimumLength, maxLength), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                Map<Integer, List<Word>> allMatchingWords = new ConcurrentHashMap<>();
                for (int i = 2; i < 9; i++) {
                    allMatchingWords.put(i, new ArrayList<Word>());
                }
                for (Word word : words) {
                    mAllAnagramList.add(word);
                    int wordLength = word.getWord().length() < 8 ? word.getWord().length() : 8;
                    if (allMatchingWords.containsKey(wordLength)) {

                        allMatchingWords.get(wordLength).add(word);
                    }

                }
                mAllRetrievedAnagrams.setValue(allMatchingWords);




            }


        });

    }

    public void insertWord(Word insertWord) {
        mRepository.insertWord(insertWord);
    }

    public void setSelectedAnagrams(int length) {

        if (length < 2) {
            selectedAnagrams.setValue(mAllAnagramList);
        } else {

            //Selected Length
            if (mAllRetrievedAnagrams.getValue() != null) {
                selectedAnagrams.setValue(mAllRetrievedAnagrams.getValue().get(length));
            }
        }


    }

    public LiveData<String> getDefinition(String word) {
        return mRepository.getDefinition(word);
    }

    public LiveData<List<WordAndDefinition>> getMatchingWordsForCrossword() {
        return mRepository.getMatchingCrosswordWords(mLetters.getValue());

    }

    public void setWord(String letters) {
        mLetters.setValue(letters.replace(" ", "_").toLowerCase());
    }

    public LiveData<String> getLetters() {
        return mLetters;
    }

}
