package com.robbies.scraddle.WordData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * FROM word_table ORDER BY word DESC")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table" +
            " WHERE LENGTH(PrimeValue) < 19 AND :anagramLetterPrimeValue % PrimeValue == 0 AND LENGTH(Word) BETWEEN :minimumLength AND :maxLength" +
            " ORDER BY CAST(ScrabbleValue AS integer) DESC")
    LiveData<List<Word>> getMatchingPrimeWords(String anagramLetterPrimeValue, int minimumLength, int maxLength);

    @Query("SELECT * from word_table LIMIT 1")
    Word[] getAnyWord();

    @Query("SELECT * FROM word_table" +
            " WHERE LENGTH(Word) BETWEEN :minWordLength  AND :maxWordLength")
    LiveData<List<Word>> getAllWords(int minWordLength, int maxWordLength);

    @Query("SELECT Definition FROM word_definition_table WHERE Word = :word")
    LiveData<String> getDefinition(String word);

    @Query("SELECT * FROM word_definition_table WHERE Word LIKE :letters")
    LiveData<List<WordAndDefinition>> getMatchingCrosswordWords(String letters);
}
