package com.metronome.util;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerState {
    TimerTask task;
    Timer timer;
    int second = 1;
    int molecule = 1;

    public void start(int molecule, double timeValue) {
        this.molecule = molecule;
        task = timerTask();
        timer = new java.util.Timer();
        timer.schedule(task, 1, (int)(timeValue * 1000));
    }

    public void stop() {
        task.cancel();
        task.scheduledExecutionTime();
        timer.purge();
    }

    public TimerTask timerTask() {
         task = new TimerTask() {
            @Override
            public void run() {
                if (second == 1 || (double) (second % molecule) == 0) {
                    Log.e("timer log " + second, "+" );
                    second++;
                } else {
                    Log.e("timer log " + second, "-");
                    second++;
                }
            }
        };
        return task;
    }
}