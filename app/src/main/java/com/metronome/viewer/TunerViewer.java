package com.metronome.viewer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.metronome.R;
import com.metronome.util.ContextManager;
import com.metronome.util.ScaleConverter;
import com.metronome.util.domain.AudioAnalysisResult;
import com.metronome.util.domain.ScaleConvertResult;

import java.util.Random;

public class TunerViewer {

    private View view;

    private Thread thread;
    int mode=0;
    //imageView 용
    public ImageView imageView;
    public Bitmap bitmap;
    public Bitmap bitmapOutput;
    public Canvas canvas;
    public Canvas canvasOutput;
    public Paint paint;
    public Paint paint2;
    //
    public FrameLayout tunerViewFrame;
    //
    public ImageView imageView2;
    public Bitmap tunerBitmap;
    public Bitmap tunerBitmapOutput;
    public Canvas tunerCanvas;
    public Canvas tunerOutputCanvas;
    public Paint tunerPaint;
    public int centerPointX;
    public int centerPointY;
    public int screenWidth;
    //
    public float density;
    public int frameHeight;
    //


    //AudioAnalysisResult audioAnalysisResult;

    public TunerViewer(View view){
        this.view = view;

        imageView = view.findViewById(R.id.imageView);
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
        screenWidth = ContextManager.getScreenSize().x;
        density = ContextManager.getDensity();
        frameHeight = (int)(150*density+0.5);

        centerPointX = screenWidth/2;
        centerPointY = frameHeight/2;
        //
        imageView2 = view.findViewById(R.id.imageView2);
        tunerBitmap =  Bitmap.createBitmap((int)screenWidth, (int)400, Bitmap.Config.ARGB_8888);
        tunerBitmapOutput = Bitmap.createBitmap((int)screenWidth, (int)400, Bitmap.Config.ARGB_8888);
        tunerCanvas = new Canvas(tunerBitmap);
        tunerOutputCanvas = new Canvas(tunerBitmapOutput);
        tunerPaint = new Paint();
        tunerPaint.setColor(Color.BLACK);
        tunerPaint.setTextSize(30);

        imageView2.setImageBitmap(tunerBitmapOutput);
        //imageView2.setImageBitmap(tunerBitmap); // 더블버퍼링해제


        Log.d("test", "frameHeight: "+frameHeight);



    }


    public void drawPitchView(AudioAnalysisResult audioAnalysisResult){
        double[] fftOutput = audioAnalysisResult.fftResult;
        float pitch = audioAnalysisResult.frequency;

        int index=0;
        canvas.drawColor(Color.BLACK);
        //Log.d("test", "toTransformLength:  "+toTransform[0].length);

        for(int i = 0; i < fftOutput.length; i++){
            int x = i/4 ;
            int downy = (int) (400 - (fftOutput[i] * 10));
            int upy = 400;
            canvas.drawLine(x, downy, x, upy, paint);

            if(fftOutput[index]<fftOutput[i]){
                index = i;
            }
        }

        canvas.drawText("Hz :: "+ index + "Mag ::"+ fftOutput[index] ,100,100,paint2);
        canvas.drawText("Pitch::"+ pitch,100,200,paint2);
        canvasOutput.drawBitmap(bitmap ,0,0,null);
        imageView.invalidate();
    }

    int scale=0;
    String scaleWord="";
    String errorFrequency="";
    float frequency =0;
    public void drawTunerResult(ScaleConvertResult scaleConvertResult){
        scale = scaleConvertResult.scale;
        scaleWord = ScaleConverter.scaleWordList[scale];
        errorFrequency = scaleConvertResult.erroFrequency+"";
        frequency = scaleConvertResult.frequency;

        //비트맵 초기화
        imageView2.setImageBitmap(null);
        imageView2.invalidate();

        tunerBitmap =  Bitmap.createBitmap((int)screenWidth, (int)400, Bitmap.Config.ARGB_8888);
        tunerCanvas = new Canvas(tunerBitmap);
        tunerCanvas.drawColor(Color.argb(0,0,0,0));
        //해상도 테스트
        tunerCanvas.drawCircle(centerPointX , centerPointY , 10 , tunerPaint );
        tunerCanvas.drawCircle(centerPointX , frameHeight , 10 , tunerPaint );
        //
        tunerCanvas.drawCircle(centerPointX + scaleConvertResult.erroFrequency, centerPointY , 10 , tunerPaint );
        tunerCanvas.drawText(scaleWord,centerPointX,150,tunerPaint);

        tunerCanvas.drawCircle(0,0,10,tunerPaint);
        tunerCanvas.drawCircle(screenWidth,0,10,tunerPaint);

        tunerOutputCanvas.drawBitmap(tunerBitmap,0,0,null);


        imageView2.setImageBitmap(tunerBitmap);
        imageView2.invalidate();
    }

}
