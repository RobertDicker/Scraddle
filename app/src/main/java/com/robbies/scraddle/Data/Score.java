package com.robbies.scraddle.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "score_table", primaryKeys = {"matchId", "playerId"}, foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = Match.class,
                parentColumns = "matchId", childColumns = "matchId"), @ForeignKey(onDelete = CASCADE, entity = Player.class, parentColumns = "playerId", childColumns = "playerId")},
        indices = {
                @Index("matchId"),
                @Index("playerId")
        })
public class Score {


    private long matchId;


    private int playerId;

    @ColumnInfo(defaultValue = "", name = "score")
    private String score;

    //This is the order
    private int playersTurnOrder;

    private int totalScore;

    private int maxScore;

    @ColumnInfo(defaultValue = "404", name = "result")
    private int result;

    public Score(int playerId, long matchId, int playersTurnOrder) {
        this.score = "";
        this.matchId = matchId;
        this.playerId = playerId;
        this.maxScore = 0;
        this.totalScore = 0;
        this.playersTurnOrder = playersTurnOrder;

    }

    public Score(GameDetail player) {
        this.matchId = player.getMatchId();
        this.playerId = player.getPlayerId();
        this.score = player.getScore();
        this.playersTurnOrder = player.getPlayersTurnOrder();
        this.totalScore = player.getTotalScore();
        this.maxScore = player.getMaxScore();
        this.result = player.getResult();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    int getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    int getMaxScore() {
        return this.maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getLastScore() {
        return score.isEmpty() ? "0" : score.substring(score.length() - 1);
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }


    int getPlayersTurnOrder() {
        return playersTurnOrder;
    }

    public void setPlayersTurnOrder(int playersTurnOrder) {
        this.playersTurnOrder = playersTurnOrder;
    }

    //Win 1, draw 0, loss -1
    int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Score{" +
                "matchId=" + matchId +
                ", playerId=" + playerId +
                ", score='" + score + '\'' +
                ", playersTurnOrder=" + playersTurnOrder +
                ", totalScore=" + totalScore +
                ", maxScore=" + maxScore +
                ", result=" + result +
                '}';
    }
}
