package com.robbies.scraddle.Utilities;

public class Timer {

    private long startTime;
    private long endTime;

    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    public String stopTimer() {

        endTime = System.nanoTime();
        double algoTimerInSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
        return algoTimerInSeconds + "";
    }


}
