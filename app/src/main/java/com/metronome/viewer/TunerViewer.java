package com.metronome.viewer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.metronome.R;
import com.metronome.util.ContextManager;
import com.metronome.util.ScaleConverter;
import com.metronome.util.domain.AudioAnalysisResult;
import com.metronome.util.domain.ScaleConvertResult;

public class TunerViewer implements Runnable {

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
    //
    public ImageView imageView2;
    public Bitmap tunerBitmap;
    public Bitmap tunerBitmapOutput;
    public Canvas tunerCanvas;
    public Canvas tunerOutputCanvas;
    public Paint tunerPaint;
    public int centerPoint;
    public int screenWidth;
    public BitmapDrawable backgroundDrawble;
    public Bitmap backgroundBitmap;
    public Bitmap backgroundBitmapResize;
    public Resources resources;
    //

    //AudioAnalysisResult audioAnalysisResult;

    public TunerViewer(View view){
        this.view = view;
        thread = new Thread(this);

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
        centerPoint = screenWidth/2;
        imageView2 = view.findViewById(R.id.imageView2);
        tunerBitmap =  Bitmap.createBitmap((int)screenWidth, (int)400, Bitmap.Config.ARGB_8888);
        tunerBitmapOutput = Bitmap.createBitmap((int)screenWidth, (int)400, Bitmap.Config.ARGB_8888);
        tunerCanvas = new Canvas(tunerBitmap);
        tunerOutputCanvas = new Canvas(tunerBitmapOutput);
        tunerPaint = new Paint();
        tunerPaint.setColor(Color.BLACK);
        tunerPaint.setTextSize(30);
        imageView2.setImageBitmap(tunerBitmapOutput);

        //배경 비트맵 초기화 , 사이즈조절
        int viewWidth = imageView2.getMeasuredWidth();
        int viewHeight = imageView2.getMeasuredHeight();
        resources =  ContextManager.getContext().getResources();
        backgroundDrawble = (BitmapDrawable)resources.getDrawable(R.drawable.bg_tuner_frequency,null);
        backgroundBitmap = backgroundDrawble.getBitmap();
        Log.d("test", "bgBitmap"+backgroundBitmap.getWidth()+":"+backgroundBitmap.getHeight());
        double aspectRatio = (double)backgroundBitmap.getHeight() / (double)backgroundBitmap.getWidth();
        int resizeWidth = screenWidth;
        int resizeHeight = (int) (resizeWidth*aspectRatio);
        backgroundBitmapResize = Bitmap.createScaledBitmap(backgroundBitmap ,resizeWidth, resizeHeight,false);
        //backgroundBitmapResize = Bitmap.createScaledBitmap(backgroundBitmap ,300, 200,false);

        Log.d("test", aspectRatio+":"+resizeWidth+":"+resizeHeight);
    }


    public void drawPitchView(Message msg){
        //audioAnalysisResult = (AudioAnalysisResult) msg.obj;
    }

    @Override
    public void run() {
        Log.d("test", "tunerViewer Thread start!!!! ");
       /*
        switch (mode){
            case 1:
                drawPitchView();
                break;
        }
        */
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

    public void drawTunerResult(ScaleConvertResult scaleConvertResult){
        int scale = scaleConvertResult.scale;
        String scaleWord = ScaleConverter.scaleWordList[scale];
        String errorFrequency = scaleConvertResult.erroFrequency+"";
        float frequency = scaleConvertResult.frequency;
        Log.d("scaleWord", "scaleWord: "+scaleWord);
        tunerCanvas.drawColor(Color.WHITE);
        tunerCanvas.drawBitmap(backgroundBitmapResize,0,0,null);
        tunerCanvas.drawCircle(centerPoint + scaleConvertResult.erroFrequency, 100 , 10 , tunerPaint );
        tunerCanvas.drawText(scaleWord,centerPoint,150,tunerPaint);
        tunerCanvas.drawCircle(0,0,10,tunerPaint);
        tunerCanvas.drawCircle(screenWidth,0,10,tunerPaint);
        tunerOutputCanvas.drawBitmap(tunerBitmap,0,0,null);

        imageView2.invalidate();
    }

}
