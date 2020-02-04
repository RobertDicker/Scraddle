package com.robbies.scraddle.WordData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.robbies.scraddle.Utilities.PrimeValue;

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


    public Word(@NonNull String word, @NonNull String primeValue, @NonNull String scrabbleValue) {

        this.word = word;
        this.primeValue = primeValue;
        this.scrabbleValue = scrabbleValue;
    }

    @NonNull
    public String getWord() {
        return this.word;
    }

    @NonNull
    public String getPrimeValue() {
        return this.primeValue;
    }

    void setPrimeValue(String primeValue) {
        this.primeValue = PrimeValue.calculatePrimeValue(word) + "";
    }

    public long getPrimeValueLong() {
        return Long.parseLong(this.primeValue);
    }

    @NonNull
    public String getScrabbleValue() {
        return scrabbleValue;
    }

    public void setScrabbleValue(@NonNull String scrabbleValue) {
        this.scrabbleValue = scrabbleValue;
    }
}
