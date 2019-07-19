package com.metronome.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class LightControl implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;

    public void streakLight(Context context) {
            sensorManager =(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if(lightSensor == null) {
                Toast.makeText(context, "No Light Sensor Found!", Toast.LENGTH_SHORT).show();
            }
        }
        /*
        @Override
        protected void onResume() {
            super.onResume();
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        protected void onPause() {
            super.onPause();
            sensorManager.unregisterListener(this);
        }
        */
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
                Log.e("Light : ", String.valueOf(event.values[0]));
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
}
