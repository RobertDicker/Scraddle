package com.robbies.scraddle.Utilities;

import android.util.Log;

public class Timer {

    long timeRemainingInSeconds;
    TimerUpdateListener mListener;
    private long startTime;
    private long endTime;

    public Timer(TimerUpdateListener listener) {
        this.mListener = listener;
    }


    public static Timer newTimer(TimerUpdateListener listener) {
        return new Timer(listener);
    }

    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    public void startCountdown(final int limit) {
        startTimer();
        timeRemainingInSeconds = (limit - ((endTime - startTime)) / 1_000_000_000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeRemainingInSeconds > 0) {
                    Log.d("Timer ", "" + timeRemainingInSeconds);
                    try {
                        wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mListener.updateTime(Long.toString(timeRemainingInSeconds = (limit - ((endTime - startTime)) / 1_000_000_000)));
                }
                stopTimer();
                mListener.notifyTimeUp();
            }
        }).start();
    }

    public String stopTimer() {

        endTime = System.nanoTime();
        double algoTimerInSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
        return algoTimerInSeconds + "";
    }


    public interface TimerUpdateListener {
        void updateTime(String time);

        void notifyTimeUp();
    }


}
