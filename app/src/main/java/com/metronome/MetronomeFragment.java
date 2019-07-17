package com.metronome;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.metronome.util.TempoBPM;

import java.util.Timer;
import java.util.TimerTask;

public class MetronomeFragment extends Fragment {
    private PopupWindow tempoPopup;
    NumberPicker picker_molecule, picker_denominator;
    TextView timeView, bpmText, tempoView, bpmView, valueText;
    TimerTask task;
    Timer timer;
    Button start, stop;
    double timeValue = 0.1666666666666667;
    int bpm = 10;
    int tempo = 2;
    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metronome, null);
        SeekBar seekBar = view.findViewById(R.id.BPMSeekBar);
        final TempoBPM tempoBPM = new TempoBPM();
        timeView = view.findViewById(R.id.clickOutput);
        valueText = view.findViewById(R.id.valueText);
        tempoView = view.findViewById(R.id.tempoView);
        bpmView = view.findViewById(R.id.bpmView);
        bpmText = view.findViewById(R.id.BPMText);
        start = view.findViewById(R.id.start);
        stop = view.findViewById(R.id.stop);

        //tempo button 이벤트
        Button popup = view.findViewById(R.id.tempBtn);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View tempView = getLayoutInflater().inflate(R.layout.tempo_popup, null);
                tempoPopup = new PopupWindow(tempView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tempoPopup.showAtLocation(tempView, Gravity.CENTER, 0, 0);
                picker_molecule = tempView.findViewById(R.id.picker_molecule);
                picker_denominator = tempView.findViewById(R.id.picker_denominator);

                //NumberPicker molecule / denominator 설정
                picker_molecule.setMinValue(0);
                picker_molecule.setMaxValue(11);
                picker_molecule.setDisplayedValues(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
                picker_molecule.setValue(0);
                picker_denominator.setMinValue(0);
                picker_denominator.setMaxValue(5);
                picker_denominator.setDisplayedValues(new String[]{"2", "4", "8", "2", "4", "8"});
                picker_denominator.setValue(0);

                //tempPopup 이벤트
                Button tempoPopupEvent = tempView.findViewById(R.id.CANCLE);
                tempoPopupEvent.setOnClickListener(new View.OnClickListener() {
                    // 취소 이벤트
                    @Override
                    public void onClick(View v) {
                        tempoPopup.dismiss();
                    }
                });
                Button ok = tempView.findViewById(R.id.OK);
                ok.setOnClickListener(new View.OnClickListener() {
                    // 확인 이벤트
                    @Override
                    public void onClick(View v) {
                        tempo = Integer.parseInt(picker_denominator.getDisplayedValues()[picker_denominator.getValue()]);
                        timeValue = tempoBPM.tempView(bpm, tempo);
                        tempoView.setText(picker_molecule.getDisplayedValues()[picker_molecule.getValue()] + " / " + tempo);
                        valueText.setText(String.valueOf(timeValue));
                        tempoPopup.dismiss();
                    }
                });
            }
        });

        //SeekBar 이벤트
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //처음에 발생하는 이벤트
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            //탭하는 순간 발생하는 이벤트
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //드래그를 멈추면 발생하는 이벤트
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bpm = progress+10;
                timeValue = tempoBPM.tempView(bpm, tempo);
                valueText.setText(String.valueOf(timeValue));
                bpmView.setText(tempoBPM.bpmView(bpm));
                bpmText.setText(String.valueOf(bpm));
            }
        });

        //Start 버튼 이벤트
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = timerTask();
                timer = new Timer();
                timer.schedule(task, 1, (int)(timeValue * 1000));
            }
        });

        //Stop 버튼 이벤트
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

//타이머 메소드
public TimerTask timerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeView.setText(String.valueOf(i++));
            }
        };
        return task;
    }
}