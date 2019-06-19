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

public class AudioUtil implements Runnable{

    private AudioRecord audioRecord;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate =    44100;
    private int channelCount = AudioFormat.CHANNEL_IN_STEREO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioTrack.getMinBufferSize(sampleRate,channelCount,audioFormat);

    private AUDIO_MODE audioMode;
    private enum AUDIO_MODE{
        AUDIO_MODE_RECORD, AUDIO_MODE_ANALYZE , AUDIO_MODE_PLAY
    }
    private Thread thread;
    public AudioUtil(){
        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
        thread = new Thread(this);
    }
    public AudioUtil(int audioSource, int sampleRate, int channelCount, int audioFormat, int bufferSize){
        this.audioSource = audioSource;
        this.sampleRate = sampleRate;
        this.channelCount=channelCount;
        this.audioFormat = audioFormat;
        this.bufferSize = bufferSize;

        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
    }

    public void startRecord(){
        audioRecord.startRecording();

    }
    public void stopRecord(){

    }

    public void startAnalyze(){
        audioMode = AUDIO_MODE.AUDIO_MODE_ANALYZE;
        audioRecord.startRecording();

    }
    public void stopAnlyze(){

    }

    @Override
    public void run() {
        switch (audioMode){
            case AUDIO_MODE_ANALYZE:
                Log.d("test", "run : thread Test!!!! ");
                break;
            case AUDIO_MODE_RECORD:
                break;
            case AUDIO_MODE_PLAY:
                break;
        }
    }
}
