package com.robbies.scraddle.WordData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "word_definition_table",
        primaryKeys = {"Word"}, foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = Word.class,
                parentColumns = "Word", childColumns = "Word")},
        indices = {
                @Index("Word")
        })
public class WordAndDefinition {


    @NonNull
    @ColumnInfo(name = "Word")
    private String word;

    @ColumnInfo(name = "Definition", defaultValue = "N/A")
    private String definition;


    public WordAndDefinition(@NonNull String word, String definition) {

        this.word = word;
        this.definition = definition;

    }

    @NonNull
    public String getWord() {
        return this.word;
    }


    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
