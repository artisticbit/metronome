package com.metronome;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MetronomeFragment extends Fragment {
    RecyclerView recyclerView;
    TextView textview, bpmView;
    TimerTask task;
    Timer timer;
    Button start, stop;
    int i = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metronome, null);
        SeekBar seekBar  = view.findViewById(R.id.BPMSeekBar);
        textview = view.findViewById(R.id.clickOutput);
        bpmView = view.findViewById(R.id.BPMText);
        start = view.findViewById(R.id.start);
        stop = view.findViewById(R.id.stop);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //처음에 발생하는 이벤트
            public void onStopTrackingTouch(SeekBar seekBar) {}
            //탭하는 순간 발생하는 이벤트
            public void onStartTrackingTouch(SeekBar seekBar) {}
            //드래그를 멈추면 발생하는 이벤트
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bpmView.setText(String.valueOf(progress));
            }
        });

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