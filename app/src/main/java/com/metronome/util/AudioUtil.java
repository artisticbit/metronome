package com.metronome.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.metronome.util.tarsos.DynamicWavelet;
import com.metronome.util.tarsos.PitchDetectionResult;
import com.metronome.util.tarsos.PitchDetector;
import com.metronome.util.tarsos.Yin;

import java.util.ArrayDeque;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class AudioUtil implements Runnable{

    private AudioRecord audioRecord;
    /*
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate =    44100;//16384;//44100;
    private int channelCount = AudioFormat.CHANNEL_IN_MONO;//AudioFormat.CHANNEL_CONFIGURATION_MONO;//AudioFormat.CHANNEL_IN_STEREO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioRecord.getMinBufferSize(sampleRate,channelCount,audioFormat);
    */

    //YIN 용 초기화
      private int audioSource = MediaRecorder.AudioSource.MIC;
      private int sampleRate = 16384;
      private int channelCount = AudioFormat.CHANNEL_IN_MONO;
      private int audioFormat = AudioFormat.ENCODING_PCM_FLOAT;//AudioFormat.ENCODING_PCM_16BIT;////
      private int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelCount, audioFormat );
      private double frequency = 0f;
    //

    private AUDIO_MODE audioMode;
    private enum AUDIO_MODE{
        AUDIO_MODE_RECORD, AUDIO_MODE_ANALYZE , AUDIO_MODE_PLAY
    }
    private Thread thread;

    public ImageView imageView;
    public Bitmap bitmap;
    public Bitmap bitmapOutput;
    public Canvas canvas;
    public Canvas canvasOutput;
    public Paint paint;
    public Paint paint2;

    public AudioUtil(){
        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
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
            thread=new Thread(this);
            thread.start();
        }

    }
/*
 */

    @Override
    public void run() {
        byte[] readData = new byte[bufferSize];
        int blockSize = 8192;
        int readSize=1024;
        RealDoubleFFT transfromer = new RealDoubleFFT(blockSize);
        short[] buffer = new short[blockSize];
        double[] toTransform = new double[blockSize];
        ArrayDeque<Double> deque = new ArrayDeque<Double>(blockSize);

        //tarsos 라이브러리 사용
        PitchDetector pitchDetector = new Yin(sampleRate, readSize);
        PitchDetectionResult pitchDetectionResult;
        PitchDetector pitchDetector1 = new DynamicWavelet(sampleRate,readSize);

        for(int i=0; i<blockSize; i++){
            deque.add(0.0);
        }

        float[] floatBuffer = new float[readSize];
        Double[] toTransformD = new Double[blockSize];


        switch (audioMode){
            case AUDIO_MODE_ANALYZE:
               while(audioRecord.getRecordingState()==AudioRecord.RECORDSTATE_RECORDING){
                   //int bufferReadResult = audioRecord.read(buffer,0,blockSize); //blockSize에서 작은사이즈로

                   //int bufferReadResult = audioRecord.read(buffer,0,readSize);
                   int bufferReadResult = audioRecord.read(floatBuffer,0,readSize,AudioRecord.READ_NON_BLOCKING);

                   //for(int i = 0; i < blockSize && i < bufferReadResult; i++){
                   for(int i = 0; i < readSize && i < bufferReadResult; i++){
                       //toTransform[i] = (double)buffer[i] / Short.MAX_VALUE;
                       deque.removeFirst();//
                       //deque.addLast((double)buffer[i] / Short.MAX_VALUE);//
                       deque.addLast((double)floatBuffer[i]);
                   }
                   toTransformD = deque.toArray(toTransformD);
                   for(int i=0; i<blockSize; i++) {
                       toTransform[i] = toTransformD[i];
                   }
                    /////
                   //float[] floatBuffer2 = new float[blockSize];
                   //for(int i=0; i<blockSize;i++)
                       //floatBuffer2[i]= (float)toTransform[i];
                   pitchDetectionResult = pitchDetector1.getPitch(floatBuffer);
                   if(pitchDetectionResult.getPitch()!=-1.0)
                   frequency = pitchDetectionResult.getPitch();
                   //////


                   transfromer.ft(toTransform);

                   //Log.d("test", "run: "+toTransform[0]);
                   onProgressUpdate(toTransform);
               }
                break;
            case AUDIO_MODE_RECORD:
                break;
            case AUDIO_MODE_PLAY:
                break;
        }

        Log.d("test", "audioUtil Thread End!!!");
    }

/*
//YIN 알고리즘 테스트
    @Override
    public void run() {
        int readSize = bufferSize/4;
        short[] buffer = new short[readSize];
        float[] floatBuffer = new float[readSize];
        YINPitchDetector detector = new YINPitchDetector(sampleRate,readSize);
        short SHORT_DIVISOR = (short) (-1 * Short.MIN_VALUE);
        Log.d("test", "readSize :: "+ readSize);
        switch (audioMode){
            case AUDIO_MODE_ANALYZE:
                while(audioRecord.getRecordingState()==AudioRecord.RECORDSTATE_RECORDING){
                    audioRecord.read(floatBuffer,0,readSize,AudioRecord.READ_BLOCKING);

                    double frequency = detector.detect(floatBuffer);
                    Log.d("test", "Frequency ::" + frequency);
                }
                break;
            case AUDIO_MODE_RECORD:
                break;
            case AUDIO_MODE_PLAY:
                break;
        }

        Log.d("test", "audioUtil Thread End!!!");
    }

    */
    //그림용 테스트펑션
    public void setImageView(ImageView imageView){
        //테스트코드
        this.imageView = imageView;
        bitmap = Bitmap.createBitmap((int)1024, (int)300, Bitmap.Config.ARGB_8888);
        bitmapOutput = Bitmap.createBitmap((int)1024, (int)300, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvasOutput = new Canvas(bitmapOutput);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        imageView.setImageBitmap(bitmapOutput);
        //
    }
    public void onProgressUpdate(double[]... toTransform) {
        int index=0;
        canvas.drawColor(Color.BLACK);
        //Log.d("test", "toTransformLength:  "+toTransform[0].length);
        for(int i = 0; i < toTransform[0].length; i++){
            int x = i/4 ;
            int downy = (int) (400 - (toTransform[0][i] * 10));
            int upy = 400;
            canvas.drawLine(x, downy, x, upy, paint);

            if(toTransform[0][index]<toTransform[0][i]){
                index = i;
            }
        }

        canvas.drawText("Hz :: "+ index + "Mag ::"+ toTransform[0][index] ,100,100,paint2);
        canvas.drawText("Pitch::"+ frequency,100,200,paint2);
        canvasOutput.drawBitmap(bitmap ,0,0,null);
        imageView.invalidate();
    }
}
