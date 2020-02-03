package com.robbies.scraddle.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class GameDetail {
    @NonNull
    @ColumnInfo(name = "name", defaultValue = "Unknown Player")
    private String name;

    @ColumnInfo(name = "personalBest", defaultValue = "0")
    private int personalBest;

    @ColumnInfo(name = "highestMatchScore", defaultValue = "0")
    private int playersHighestMatchScore;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playerId")
    private int playerId;

    @ColumnInfo(name = "wins", defaultValue = "0")
    private int wins;

    @ColumnInfo(name = "loss", defaultValue = "0")
    private int loss;

    @ColumnInfo(name = "draw", defaultValue = "0")
    private int draw;

    private long matchId;

    @ColumnInfo(defaultValue = "", name = "score")
    private String score;

    //This is the order
    private int playersTurnOrder;

    private int totalScore;

    private int maxScore;

    @ColumnInfo(defaultValue = "404", name = "result")
    private int result;

    GameDetail(@NonNull String name, int personalBest, int playersHighestMatchScore, int playerId, int wins, int loss, int draw, long matchId, String score, int playersTurnOrder, int totalScore, int maxScore, int result) {
        this.name = name;
        this.personalBest = personalBest;
        this.playersHighestMatchScore = playersHighestMatchScore;
        this.playerId = playerId;
        this.wins = wins;
        this.loss = loss;
        this.draw = draw;
        this.matchId = matchId;
        this.score = score;
        this.playersTurnOrder = playersTurnOrder;
        this.totalScore = totalScore;
        this.maxScore = maxScore;
        this.result = result;
    }

    public GameDetail(GameDetail gameDetail) {
        this(gameDetail.name, gameDetail.personalBest, gameDetail.playersHighestMatchScore, gameDetail.playerId, gameDetail.wins, gameDetail.loss, gameDetail.draw, gameDetail.matchId, gameDetail.score, gameDetail.playersTurnOrder, gameDetail.totalScore, gameDetail.getMaxScore(), gameDetail.result);
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

    int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getPlayersTurnOrder() {
        return playersTurnOrder;
    }

    public void setPlayersTurnOrder(int playersTurnOrder) {
        this.playersTurnOrder = playersTurnOrder;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getLastScore() {
        String lastScore = "0";
        if (!score.isEmpty()) {

            int lastIndex = score.lastIndexOf(" ");
            lastScore = score.substring(lastIndex);
        }
        return lastScore;
    }

    public void removeLastScore() {

        if (!score.isEmpty()) {
            String lastScore = getLastScore();
            totalScore -= Integer.parseInt(lastScore);
            score = score.substring(0, (score.lastIndexOf(lastScore) - 2));
        }
    }

    public void addScore(int score) {
        totalScore += score;
        maxScore = score > maxScore ? score : maxScore;
        this.score = this.score + ", " + score;
    }

    public int getRank() {
        return (this.wins * 3 + this.draw + (this.loss * -1));
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

    @NonNull
    @Override
    public String toString() {
        return "GameDetail{" +
                "name='" + name + '\'' +
                ", personalBest=" + personalBest +
                ", playersHighestMatchScore=" + playersHighestMatchScore +
                ", playerId=" + playerId +
                ", wins=" + wins +
                ", loss=" + loss +
                ", draw=" + draw +
                ", matchId=" + matchId +
                ", score='" + score + '\'' +
                ", playersTurnOrder=" + playersTurnOrder +
                ", totalScore=" + totalScore +
                ", maxScore=" + maxScore +
                ", result=" + result +
                '}';
    }


}
