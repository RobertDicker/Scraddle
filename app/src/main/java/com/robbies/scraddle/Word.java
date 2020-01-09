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

    @ColumnInfo(name = "word")
    private String word;

    @NonNull
    @ColumnInfo(name = "primeValue")
    private String primeValue;


    public Word(@NonNull String word) {
        Log.d("creating word", word);
        this.word = word;
        this.primeValue = PrimeValue.calculatePrimeValue(word) + "";
    }

    public String getWord() {
        return this.word;
    }

    public String getPrimeValue() {
        return this.primeValue;
    }

    public void setPrimeValue(String primeValue) {
        this.primeValue = PrimeValue.calculatePrimeValue(word) + "";
    }

    public long getPrimeValueLong() {
        return Long.parseLong(this.primeValue);
    }
}
