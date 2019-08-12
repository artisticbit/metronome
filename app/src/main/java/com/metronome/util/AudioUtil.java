package com.metronome.util;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.metronome.util.domain.AudioAnalysisResult;
import com.metronome.util.domain.ScaleConvertResult;
import com.metronome.util.tarsos.McLeodPitchMethod;
import com.metronome.util.tarsos.PitchDetectionResult;
import com.metronome.util.tarsos.PitchDetector;
import com.metronome.viewer.TunerViewer;


import java.util.ArrayDeque;


import ca.uol.aig.fftpack.RealDoubleFFT;

public class AudioUtil implements Runnable{

    private AudioRecord audioRecord;

    Handler handler;
    TunerViewer tunerViewer;

    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRate = 44100;//16384;
    private int channelCount = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_FLOAT;//AudioFormat.ENCODING_PCM_16BIT;////
    private int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelCount, audioFormat );


    private AUDIO_MODE audioMode;
    private enum AUDIO_MODE{
        AUDIO_MODE_RECORD, AUDIO_MODE_ANALYZE , AUDIO_MODE_PLAY
    }
    private Thread thread;


    public AudioUtil(TunerViewer tunerViewer){
        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
        this.tunerViewer = tunerViewer;
    }
    public AudioUtil(int audioSource, int sampleRate, int channelCount, int audioFormat, int bufferSize){
        this.audioSource = audioSource;
        this.sampleRate = sampleRate;
        this.channelCount=channelCount;
        this.audioFormat = audioFormat;
        this.bufferSize = bufferSize;

        audioRecord = new AudioRecord(audioSource,sampleRate,channelCount,audioFormat,bufferSize);
    }

    public void setHandler(Handler handler){
        this.handler = handler;
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
    //튜너용 변수 초기화
    byte[] readData = new byte[bufferSize];
    int blockSize = 8192;
    int readSize=1024;
    RealDoubleFFT transfromer = new RealDoubleFFT(blockSize);
    short[] buffer = new short[blockSize];
    double[] toTransform = new double[blockSize];
    ArrayDeque<Double> deque = new ArrayDeque<Double>(blockSize);
    float frequency =0;
    //음역추출용 클래스 초기화
    ScaleConverter scaleConverter = new ScaleConverter();
    ScaleConvertResult scaleConvertResult=scaleConverter.getScale(64);;
    //tarsos 라이브러리 사용
    PitchDetector pitchDetector = new McLeodPitchMethod(sampleRate,readSize);
    PitchDetectionResult pitchDetectionResult;

    //메시지 전송 오브젝트
    AudioAnalysisResult audioAnalysisResult = new AudioAnalysisResult();

    //
    @Override
    public void run() {

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

                   //////현재 주파수 분석 결과물
                   pitchDetectionResult = pitchDetector.getPitch(floatBuffer);
                   //FFT 변화
                   transfromer.ft(toTransform);

                   frequency = pitchDetectionResult.getPitch();

                   if(frequency > -1) {
                       audioAnalysisResult.frequency = frequency;

                       //획득 주파수를 이용하여 음역대 분석 후 화면 줄력
                       scaleConvertResult = scaleConverter.getScale(frequency);

                   }

                   //fft 삭제예정
                   //audioAnalysisResult.fftResult = toTransform;

                   //메인스레드로 이관
                   //tunerViewer.drawPitchView(audioAnalysisResult);
                   //tunerViewer.drawTunerResult(scaleConvertResult);
                   //
                   //Log.d("test", "run: "+toTransform[0]);


                   //핸들러쪽으로 메시지전송

                   Message msg = new Message();
                   msg.obj=audioAnalysisResult;
                   msg.what=0;
                   handler.sendMessage(msg);

                   msg = new Message();
                   msg.obj=scaleConvertResult;
                   msg.what=1;
                   handler.sendMessage(msg);

               }
                break;
            case AUDIO_MODE_RECORD:
                break;
            case AUDIO_MODE_PLAY:
                break;
        }

        Log.d("test", "audioUtil Thread End!!!");
    }

    public ScaleConverter.INSTRUMENT_MODE getCurrentInstrument(){
        if(scaleConverter!=null){
            return scaleConverter.instrumentMode;
        }
        return ScaleConverter.INSTRUMENT_MODE.NONE;
    }
}
