package com.metronome;


import android.Manifest;
import android.content.pm.PackageManager;
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

import com.metronome.util.AudioUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class tunerFragment extends Fragment {

    private  AudioUtil audioUtil;
    private BtnOnClickListener btnOnClickListener;
    private int PERMISSION_CODE_RECORD_AUDIO = 1;

    private Button tunerStartBtn;
    public tunerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //checkPermission();
        //audioUtil = new AudioUtil();
        //audioUtil.startRecording();

        View view= inflater.inflate(R.layout.fragment_tuner,null);
        btnOnClickListener = new BtnOnClickListener();

        tunerStartBtn = view.findViewById(R.id.tunerStartBtn);
        tunerStartBtn.setOnClickListener(btnOnClickListener);



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tuner, container, false);
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.RECORD_AUDIO);

        Log.d("test","permission :"+permissionCheck);
        //RECORD_AUDIO 퍼미션없을경우 퍼미션 요청청
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            //권한취소이력확인
            if(this.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){

            }else{
                //권한요청
                this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},PERMISSION_CODE_RECORD_AUDIO);
            }
        }else{
            return true;
        }
        return false;
    }

    class BtnOnClickListener implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tunerStartBtn :
                    Log.d("test","tunerStartBtnClick!");
                break;
            }
        }
    }

}
