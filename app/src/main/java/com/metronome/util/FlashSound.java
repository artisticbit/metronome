package com.metronome.util;

import android.content.Context;
        import android.hardware.camera2.CameraAccessException;
        import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.metronome.R;

public class FlashSound {
    private CameraManager mCameraManager;
    private MediaPlayer mp;
    private String cameraId;
    private Context context;

    public FlashSound(Context context) {
        this.context = context;
        mCameraManager = (CameraManager) context.getSystemService(context.CAMERA_SERVICE);
        mp = MediaPlayer.create(context, R.raw.sound);
    }

    // Flash Event
    public void flash() {
        try {
            cameraId = mCameraManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            mCameraManager.setTorchMode(cameraId, true);
            mCameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "Not Found Flash", Toast.LENGTH_LONG).show();
        }
    }

    // Sound Event
    public void sound() {
        mp.start();
        mp.stop();
    }
}