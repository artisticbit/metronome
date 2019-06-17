package com.metronome.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class AudioUtil {

    private AudioRecord audioRecord;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate =    44100;
    private int channelCount = AudioFormat.CHANNEL_IN_STEREO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioTrack.getMinBufferSize(sampleRate,channelCount,audioFormat);

    //private Activity activity;
    public AudioUtil(){
        //this.activity = activity;
        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
    }

    public void startRecording(){

        Log.d("test","audioUtilInit");
        audioRecord.startRecording();
    }


}
