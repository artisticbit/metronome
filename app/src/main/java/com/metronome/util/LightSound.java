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
    private String cameraId;
    private Context context;

    public LightSound(Context context) {
        this.context = context;
        mCameraManager = (CameraManager) context.getSystemService(context.CAMERA_SERVICE);
    }

    // 라이트 이벤트
    public void light() {
        try {
            cameraId = mCameraManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            mCameraManager.setTorchMode(cameraId, true);
            mCameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "Not Found Flash", Toast.LENGTH_LONG).show();
        }
    }
}