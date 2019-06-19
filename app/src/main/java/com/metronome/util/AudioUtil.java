package com.metronome.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.metronome.R;

import ca.uol.aig.fftpack.RealDoubleFFT;

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

    public ImageView imageView;
    public Bitmap bitmap;
    public Canvas canvas;
    public Paint paint;

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
       // audioRecord.startRecording();

    }
    public void stopRecord(){

    }

    public void startAnalyze(){
        if(audioRecord.getRecordingState()==AudioRecord.RECORDSTATE_RECORDING){
            Log.d("test", "startAnalyze: stop");
            audioRecord.stop();
        }else{
            audioMode = AUDIO_MODE.AUDIO_MODE_ANALYZE;
            audioRecord.startRecording();
            thread.start();
        }

    }

    @Override
    public void run() {
        byte[] readData = new byte[bufferSize];
        int blockSize = 256;
        RealDoubleFFT transfromer = new RealDoubleFFT(blockSize);
        short[] buffer = new short[blockSize];
        double[] toTransform = new double[blockSize];

        switch (audioMode){
            case AUDIO_MODE_ANALYZE:
               while(audioRecord.getRecordingState()==AudioRecord.RECORDSTATE_RECORDING){
                   int bufferReadResult = audioRecord.read(buffer,0,blockSize);
                   for(int i = 0; i < blockSize && i < bufferReadResult; i++){
                       toTransform[i] = (double)buffer[i] / Short.MAX_VALUE;
                   }
                   transfromer.ft(toTransform);
                   //Log.d("test", "run: "+toTransform[0]);
                   //onProgressUpdate(toTransform);
               }
                break;
            case AUDIO_MODE_RECORD:
                break;
            case AUDIO_MODE_PLAY:
                break;
        }
    }


    //그림용 테스트펑션
    public void setImageView(ImageView imageView){
        //테스트코드
        this.imageView = imageView;
        bitmap = Bitmap.createBitmap((int)256, (int)100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);
        //
    }
    public void onProgressUpdate(double[]... toTransform) {

        canvas.drawColor(Color.BLACK);

        for(int i = 0; i < toTransform[0].length; i++){
            int x = i;
            int downy = (int) (100 - (toTransform[0][i] * 10));
            int upy = 100;
            canvas.drawLine(x, downy, x, upy, paint);
        }
        imageView.invalidate();
    }
}
