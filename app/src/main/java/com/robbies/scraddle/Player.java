package com.robbies.scraddle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "player_table")
public class Player implements Comparable {


    @NonNull
    @ColumnInfo(name = "name", defaultValue = "Unknown Player")
    private String name;

    @ColumnInfo(name = "personalBest", defaultValue = "0")
    private int personalBest;

    @ColumnInfo(name = "highestMatchScore", defaultValue = "0")
    private int playersHighestMatchScore;


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playerId")
    @NonNull
    private int playerId;

    @ColumnInfo(name = "wins", defaultValue = "0")
    private int wins;

    @ColumnInfo(name = "loss", defaultValue = "0")
    private int loss;

    @ColumnInfo(name = "draw", defaultValue = "0")
    private int draw;


    @NonNull
    @ColumnInfo(name = "lastPlayed")
    private String lastPlayed;


    public Player(String name) {
        personalBest = 0;
        playersHighestMatchScore = 0;
        wins = 0;
        loss = 0;
        draw = 0;
        lastPlayed = Calendar.getInstance().getTime().toString();
        this.name = name;

    }

    public Player(GameDetail gameDetail) {
        this.name = gameDetail.getName();
        this.personalBest = gameDetail.getPersonalBest();
        this.playersHighestMatchScore = gameDetail.getPlayersHighestMatchScore();
        this.playerId = gameDetail.getPlayerId();
        this.wins = gameDetail.getWins();
        this.loss = gameDetail.getLoss();
        this.draw = gameDetail.getDraw();
        lastPlayed = Calendar.getInstance().getTime().toString();
    }


    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(int personalBest) {
        this.personalBest = personalBest;
    }

    public int getPlayersHighestMatchScore() {
        return playersHighestMatchScore;
    }

    public void setPlayersHighestMatchScore(int playersHighestMatchScore) {
        this.playersHighestMatchScore = playersHighestMatchScore;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }


    @NonNull
    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(@NonNull String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void incrementWin() {
        this.wins++;

    }

    public void incrementDraw() {
        this.draw++;

    }

    public void incrementLoss() {
        this.loss++;

    }

    public int getRank() {
        return (this.wins * 3 + this.draw + (this.loss * -1));
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", personalBest=" + personalBest +
                ", playersHighestMatchScore=" + playersHighestMatchScore +
                ", playerId=" + playerId +
                ", wins=" + wins +
                ", loss=" + loss +
                ", draw=" + draw +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean result = false;

        if (this.getPlayerId() == ((Player) obj).getPlayerId()) {
            result = true;
        }
        return result;


    }

    @Override
    public int compareTo(Object o) {
        int i = 0;

        if (this.getRank() > ((Player) o).getRank()) {
            i = 1;
        } else if (this.getRank() < ((Player) o).getRank()) {
            i = -1;
        }

        return i;
    }
}