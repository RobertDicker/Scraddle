package com.robbies.scraddle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Word")
    private String word;

    @NonNull
    @ColumnInfo(name = "PrimeValue")
    private String primeValue;

    @NonNull
    @ColumnInfo(name = "ScrabbleValue")
    private String scrabbleValue;


    public Word(@NonNull String word, String primeValue, String scrabbleValue) {
       // Log.d("creating word", word);
        this.word = word;
        this.primeValue = primeValue;
        this.scrabbleValue = scrabbleValue;
    }

    @NonNull
    public String getWord() {
        return this.word;
    }

    @NonNull
    String getPrimeValue() {
        return this.primeValue;
    }

    void setPrimeValue(String primeValue) {
        this.primeValue = PrimeValue.calculatePrimeValue(word) + "";
    }

    public long getPrimeValueLong() {
        return Long.parseLong(this.primeValue);
    }

    public String getScrabbleValue() {
        return scrabbleValue;
    }

    public void setScrabbleValue(String scrabbleValue) {
        this.scrabbleValue = scrabbleValue;
    }
}
