package com.metronome.util;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import static android.content.Context.CAMERA_SERVICE;

public class LightSound {
    private CameraManager mCameraManager;
    private String mCameraId;

    // 라이트 이벤트
    public void light(Context context) {
        if(streakLight(context)) {
            try {
                mCameraManager.setTorchMode(mCameraId, true);
                //mCameraManager.setTorchMode(mCameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(), "Not Found Flash 1", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(context.getApplicationContext(), "Not Found Flash 2", Toast.LENGTH_LONG).show();
        }
    }

    public boolean streakLight(final Context context) {
        mCameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        if (mCameraId == null) {
            try {
                for (final String id : mCameraManager.getCameraIdList()) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    int lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraId = id;
                        return true;
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(), "Not Found Flash 3", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), "Not Found Flash 4", Toast.LENGTH_LONG).show();
            }
        },0);
        return false;
    }
}