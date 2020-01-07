package com.robbies.scraddle;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String word;

    @NonNull
    @ColumnInfo(name = "primeValue")
    private int primeValue;


    public Word(@NonNull String word) {
        this.word = word;
        this.primeValue = PrimeValue.calculatePrimeValue(word);
    }

    public String getWord() {
        return this.word;
    }

    public int getPrimeValue() {
        return this.primeValue;
    }


    public void setPrimeValue(int primeValue) {
        this.primeValue = PrimeValue.calculatePrimeValue(word);
    }
}
