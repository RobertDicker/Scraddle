package com.robbies.scraddle.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "player_record_table", primaryKeys = {"playerId"}, foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = Player.class, parentColumns = "playerId", childColumns = "playerId")},
        indices = {
                @Index("playerId")
        })
public class PlayerRecord {

    @ColumnInfo(name = "personalBest", defaultValue = "0")
    private int personalBest;

    @ColumnInfo(name = "highestMatchScore", defaultValue = "0")
    private int playersHighestMatchScore;

    @ColumnInfo(name = "playerId")
    private long playerId;

    @ColumnInfo(name = "wins", defaultValue = "0")
    private int wins;

    @ColumnInfo(name = "loss", defaultValue = "0")
    private int loss;

    @ColumnInfo(name = "draw", defaultValue = "0")
    private int draw;


    public PlayerRecord(long playerId) {
        this.playerId = playerId;
        this.personalBest = 0;
        this.playersHighestMatchScore = 0;
        this.wins = 0;
        this.loss = 0;
        this.draw = 0;
    }

    @Ignore
    public PlayerRecord(long playerId, int personalBest, int playersHighestMatchScore, int wins, int loss, int draw) {
        this.playerId = playerId;
        this.personalBest = personalBest;
        this.playersHighestMatchScore = playersHighestMatchScore;
        this.wins = wins;
        this.draw = draw;
        this.loss = loss;
    }

    public PlayerRecord(GameDetail gameDetail) {
        this.playerId = gameDetail.getPlayerId();
        this.personalBest = gameDetail.getPersonalBest();
        this.playersHighestMatchScore = gameDetail.getPlayersHighestMatchScore();
        this.wins = gameDetail.getWins();
        this.draw = gameDetail.getDraw();
        this.loss = gameDetail.getLoss();

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

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
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

    private int getRank() {
        return (this.wins * 3 + this.draw + (this.loss * -1));
    }

    @Override
    public String toString() {
        return "PlayerRecord{" +
                "playerId=" + playerId +
                '}';
    }
}
