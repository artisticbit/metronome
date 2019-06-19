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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.metronome.util.AudioUtil;
import com.metronome.util.PermissionUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class tunerFragment extends Fragment {

    private  AudioUtil audioUtil;
    private PermissionUtil permissionUtil;

    private BtnOnClickListener btnOnClickListener;
    private  ImageView imageView;

    private Button tunerStartBtn;
    public tunerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //튜너 진입시 퍼시션 체크
        permissionUtil = new PermissionUtil(getActivity());
        permissionUtil.checkPermission(Manifest.permission.RECORD_AUDIO,PermissionUtil.PERMISSION_CODE_RECORD_AUDIO);

        View view= inflater.inflate(R.layout.fragment_tuner,null);
        btnOnClickListener = new BtnOnClickListener();

        tunerStartBtn = view.findViewById(R.id.tunerStartBtn);
        tunerStartBtn.setOnClickListener(btnOnClickListener);


        imageView = view.findViewById(R.id.imageView);
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
                           audioUtil = new AudioUtil();
                           audioUtil.setImageView(imageView);//
                       }
                       audioUtil.startAnalyze();
                   }
                break;
            }
        }
    }



}
