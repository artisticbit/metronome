package com.metronome.util;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.metronome.R;

public class FlashSound {
    private CameraManager mCameraManager;
    private MediaPlayer mFirstSound, mMiddleSound;
    private String cameraId;
    private Context context;

    public FlashSound(Context context) {
        this.context = context;
        mCameraManager = (CameraManager) context.getSystemService(context.CAMERA_SERVICE);
        mFirstSound = MediaPlayer.create(context, R.raw.firstsound);
        mMiddleSound = MediaPlayer.create(context, R.raw.middlesound);
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

    // First Sound Event (Pipe)
    public void firstSound() {
        mFirstSound = MediaPlayer.create(context, R.raw.firstsound);
        mFirstSound.start();
        mFirstSound.stop();
    }

    // Middle Sound Event
    public void middleSound() {
        mMiddleSound = MediaPlayer.create(context, R.raw.middlesound);
        mMiddleSound.start();
        mMiddleSound.stop();
    }
}