package com.metronome;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MetronomeFragment extends Fragment {
    TextView textview;
    TimerTask task;
    Timer timer;
    Button start, stop;
    int i = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metronome, null);
        textview = (TextView)view.findViewById(R.id.clickOutput);
        start = (Button)view.findViewById(R.id.start);
        stop = (Button)view.findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = timerTask();
                timer = new Timer();
                timer.schedule(task, 1, 1000);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel();
                task.scheduledExecutionTime();
                timer.purge();
            }
        });
        return view;
    }

    public TimerTask timerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                textview.setText(String.valueOf(i++));
            }
        };
        return task;
    }
}