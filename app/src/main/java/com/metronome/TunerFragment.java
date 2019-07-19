package com.metronome;


import android.Manifest;
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
import com.metronome.util.PermissionUtil;
import com.metronome.util.domain.AudioAnalysisResult;
import com.metronome.util.domain.ScaleConvertResult;
import com.metronome.viewer.TunerViewer;


/**
 * A simple {@link Fragment} subclass.
 */
public class TunerFragment extends Fragment {

    private  AudioUtil audioUtil;
    private PermissionUtil permissionUtil;

    private Handler handler;

    private TunerViewer tunerViewer;

    private BtnOnClickListener btnOnClickListener;

    private Button tunerStartBtn;


    public TunerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test", "TunerFragment onCreate!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test", "TunerFragment onCreateView!!");
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

        //튜너 진입시 퍼시션 체크
        permissionUtil = new PermissionUtil(getActivity());
        permissionUtil.checkPermission(Manifest.permission.RECORD_AUDIO,PermissionUtil.PERMISSION_CODE_RECORD_AUDIO);

        View view= inflater.inflate(R.layout.fragment_tuner,null);
        btnOnClickListener = new BtnOnClickListener();

        tunerStartBtn = view.findViewById(R.id.tunerStartBtn);
        tunerStartBtn.setOnClickListener(btnOnClickListener);

        tunerViewer = new TunerViewer(view);

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                       audioUtil.startAnalyze();
                   }
                break;
            }
        }
    }


}
