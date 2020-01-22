package com.robbies.scraddle;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "match_table")
public class Match {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long matchId;

    @NonNull
    private String datePlayed;

    public Match() {
        datePlayed = Calendar.getInstance().getTime().toString();
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    @NonNull
    public String getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(@NonNull String datePlayed) {
        this.datePlayed = datePlayed;
    }


    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", datePlayed='" + datePlayed + '\'' +
                '}';
    }
}
