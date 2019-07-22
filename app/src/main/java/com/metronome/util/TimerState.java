package com.metronome.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static com.metronome.util.ContextManager.getContext;

public class TimerState {
    private Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    LightSound lightControl = new LightSound();
    private TimerTask task;
    private Timer timer;
    int second = 1;
    int molecule = 1;
    boolean sound, vibration, light;

    public void start(int molecule, double timeValue, boolean sound, boolean vibration, boolean light) {
        this.molecule = molecule;
        this.sound = sound;
        this.vibration = vibration;
        this.light = light;
        task = timerTask();
        timer = new java.util.Timer();
        timer.schedule(task, 1, (int) (timeValue * 1000));
    }

    public void stop() {
        task.cancel();
        task.scheduledExecutionTime();
        timer.purge();
        vibrator.cancel();
    }

    public TimerTask timerTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (second == 1 || (double) (second % molecule) == 0) {
                    Log.e("timer log " + second, "+");
                    if(sound) { }
                    if(vibration) { vibrator.vibrate(300); }
                    if(light) { lightControl.streakLight( getContext()); }
                    second++;
                } else {
                    Log.e("timer log " + second, "-");
                    if(sound) { }
                    if(vibration) { vibrator.vibrate(100); }
                    if(light) { lightControl.light( getContext()); }
                    second++;
                }
            }
        };
        return task;
    }
}