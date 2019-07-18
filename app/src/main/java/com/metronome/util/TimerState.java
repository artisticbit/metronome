package com.metronome.util;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Vibrator;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.CAMERA_SERVICE;
import static com.metronome.util.ContextManager.getContext;

public class TimerState {
    private Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    ;
    private CameraManager cameraManager = (CameraManager) getContext().getSystemService(CAMERA_SERVICE);
    private TimerTask task;
    private Timer timer;
    int second = 1;
    int molecule = 1;
    boolean sound, vibration, light;
    String cameraId;

    public void start(int molecule, double timeValue, boolean sound, boolean vibration, boolean light) {
        this.molecule = molecule;
        this.sound = sound;
        this.vibration = vibration;
        this.light = light;
        task = timerTask();
        timer = new java.util.Timer();
        timer.schedule(task, 1, (int) (timeValue * 1000));
    }

    public void stop() {
        task.cancel();
        task.scheduledExecutionTime();
        timer.purge();
        vibrator.cancel();
    }

    public TimerTask timerTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (second == 1 || (double) (second % molecule) == 0) {
                    Log.e("timer log " + second, "+");
                    if (sound) {
                    }

                    if (vibration) {
                        vibrator.vibrate(200);
                    }

                    if (light) {
                        flashLightOn();
                    }
                    second++;
                } else {
                    Log.e("timer log " + second, "-");
                    if (sound) {
                    }

                    if (vibration) {
                        vibrator.vibrate(100);
                    }

                    if (light) {
                        flashLightOn();
                    }
                    second++;
                }
            }
        };
        return task;
    }


        private void flashLightOn() {
            if (cameraId == null) {
                try {
                    for (String id : cameraManager.getCameraIdList()) {
                        CameraCharacteristics c = cameraManager.getCameraCharacteristics(id);
                        Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                        if (flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                            cameraId = id;
                            cameraManager.setTorchMode(cameraId, false);
                            Log.e("cameraID", id);
                            break;
                        }
                    }
                } catch (CameraAccessException e) {
                    //에러 메세지 출력
                    cameraId = null;
                }
            }
        }
        /*
    public void flashlight() {
        if (mCameraId == null) {
            try {
                for (String id : mCameraManager.getCameraIdList()) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flashAvailable != null && flashAvailable
                            && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraId = id;
                        break;
                    }
                }
            } catch (CameraAccessException e) {
                mCameraId = null;
                e.printStackTrace();
                return;
            }
        }

        mFlashOn = !mFlashOn;

        try {
            mCameraManager.setTorchMode(mCameraId, mFlashOn);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }*/
}