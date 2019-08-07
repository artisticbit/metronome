package com.metronome;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.metronome.util.AudioUtil;
import com.metronome.util.ContextManager;
import com.metronome.util.PermissionUtil;
import com.metronome.util.domain.AudioAnalysisResult;
import com.metronome.util.domain.ScaleConvertResult;
import com.metronome.viewer.TunerViewer;


/**
 * A simple {@link Fragment} subclass.
 */
public class TunerFragment extends Fragment {

    private View view;

    private  AudioUtil audioUtil;
    private PermissionUtil permissionUtil;

    private Handler handler;

    private TunerViewer tunerViewer;

    private BtnOnClickListener btnOnClickListener;

    private Button tunerStartBtn;
    private Button instrumentMenuBtn;

    public TunerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test", "TunerFragment onCreate!!");

        //핸들러 초기화
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0:
                        //tunerViewer.drawPitchView(msg);
                        break;
                    case 1:
                        tunerViewer.drawTunerResult((ScaleConvertResult) msg.obj);
                        break;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test", "TunerFragment onCreateView!!");

        //튜너 진입시 퍼시션 체크
        permissionUtil = new PermissionUtil(getActivity());
        permissionUtil.checkPermission(Manifest.permission.RECORD_AUDIO,PermissionUtil.PERMISSION_CODE_RECORD_AUDIO);

        view= inflater.inflate(R.layout.fragment_tuner,null);
        btnOnClickListener = new BtnOnClickListener();

        tunerStartBtn = view.findViewById(R.id.tunerStartBtn);
        tunerStartBtn.setOnClickListener(btnOnClickListener);

        instrumentMenuBtn = view.findViewById(R.id.instrumentMenu);
        instrumentMenuBtn.setOnClickListener(btnOnClickListener);

        tunerViewer = new TunerViewer(view);
        //오디오 유틸 초기화
        audioUtil = new AudioUtil(tunerViewer);
        audioUtil.setHandler(handler);

        return view;

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 100:

                    break;
            }
        }
    }

    //프래그먼트내 버튼클릭이벤트 정의
    class BtnOnClickListener implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tunerStartBtn :
                   if( permissionUtil.checkPermission(Manifest.permission.RECORD_AUDIO,PermissionUtil.PERMISSION_CODE_RECORD_AUDIO)){
                       Log.d("test","tunerStartBtnClick!");
                       if(audioUtil==null) {
                           audioUtil = new AudioUtil(tunerViewer);
                           audioUtil.setHandler(handler);
                       }
                       tunerViewer.init();
                       audioUtil.startAnalyze();
                   }
                break;
                   //악기모드 액티비티 열기
                case R.id.instrumentMenu :
                    Intent intent = new Intent(ContextManager.getContext(), InstrumentPopupActivity.class);
                    intent.putExtra("currentInstrum",audioUtil.getCurrentInstrument().getValue());
                    startActivityForResult(intent, 100);
                    break;
            }
        }
    }



}
