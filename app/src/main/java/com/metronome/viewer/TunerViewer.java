package com.metronome.viewer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.metronome.R;
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
    public int centerPoint;
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
        int width=view.getWidth();
        centerPoint = width/2;
        imageView2 = view.findViewById(R.id.imageView2);
        tunerBitmap =  Bitmap.createBitmap((int)width, (int)400, Bitmap.Config.ARGB_8888);
        //
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

    }

}
