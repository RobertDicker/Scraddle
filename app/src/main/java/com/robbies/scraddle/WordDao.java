package com.robbies.scraddle;

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

    @Query("SELECT * FROM word_table ORDER BY primeValue ASC")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT word, primeValue FROM word_table" +
            " WHERE CAST(:anagramLetterPrimeValue AS bigint) % CAST(primeValue AS bigint) == 0 AND LENGTH(word) BETWEEN :minimumLength AND :maxLength" +
            " ORDER BY LENGTH(word) DESC")
    LiveData<List<Word>> getMatchingPrimeWords(String anagramLetterPrimeValue, int minimumLength, int maxLength);

    @Query("SELECT * from word_table LIMIT 1")
    Word[] getAnyWord();
}
