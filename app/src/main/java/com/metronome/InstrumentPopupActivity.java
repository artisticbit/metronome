package com.metronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.metronome.util.ScaleConverter;

public class InstrumentPopupActivity extends AppCompatActivity {

    View guitarLayout;
    LinearLayout instrumentListLayout;
    View[] instrumentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_popup);

        //현재 악기 정보추출
        Intent intent = getIntent();
        int currentInstrument = intent.getIntExtra("currentInstrum",0);

        instrumentListLayout = findViewById(R.id.instrumentList);
        int instrumentCount = instrumentListLayout.getChildCount();
        instrumentList = new View[instrumentCount];
        InstBtnClickListener instBtnClickListener = new InstBtnClickListener();
        for(int i=0; i<instrumentCount; i++){
            instrumentList[i]= instrumentListLayout.getChildAt(i);
            instrumentList[i].setOnClickListener(instBtnClickListener);
        }
        //테스트
        for(int i=0; i< instrumentCount; i++){
            Log.d("test", "instrumentName: "+ instrumentList[i].getId());
            //Log.d("test", " : "+ instrumentList[i].);
        }

    }

    class InstBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.guitarLayout :
                    break;
            }
        }
    }

}
