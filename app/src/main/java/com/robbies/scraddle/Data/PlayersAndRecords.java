package com.robbies.scraddle.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class PlayersAndRecords implements Comparable<PlayersAndRecords> {


    @NonNull
    @ColumnInfo(name = "name", defaultValue = "Unknown Player")
    private String name;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playerId")
    private int playerId;

    @ColumnInfo(name = "wins", defaultValue = "0")
    private int wins;

    @ColumnInfo(name = "loss", defaultValue = "0")
    private int loss;

    @ColumnInfo(name = "draw", defaultValue = "0")
    private int draw;

    @ColumnInfo(name = "personalBest", defaultValue = "0")
    private int personalBest;

    @ColumnInfo(name = "highestMatchScore", defaultValue = "0")
    private int playersHighestMatchScore;

    public PlayersAndRecords(@NonNull String name, int playerId, int wins, int loss, int draw, int personalBest, int playersHighestMatchScore) {
        this.name = name;
        this.playerId = playerId;
        this.wins = wins;
        this.loss = loss;
        this.draw = draw;
        this.personalBest = personalBest;
        this.playersHighestMatchScore = playersHighestMatchScore;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
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

    public int getRankScore() {
        return (this.wins * 3 + this.draw + (this.loss * -1));
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

    @Override
    public int compareTo(PlayersAndRecords player) {
        int thisPlayerRank = this.getRankScore();
        int otherPlayerRank = player.getRankScore();
        return Integer.compare(thisPlayerRank, otherPlayerRank);
    }

    @Override
    public String toString() {
        return "PlayersAndRecords{" +
                "name='" + name + '\'' +
                ", playerId=" + playerId +
                ", wins=" + wins +
                ", loss=" + loss +
                ", draw=" + draw +
                ", personalBest=" + personalBest +
                ", playersHighestMatchScore=" + playersHighestMatchScore +
                '}';
    }
}
