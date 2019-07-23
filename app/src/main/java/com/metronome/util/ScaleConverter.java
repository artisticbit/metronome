package com.metronome.util;

import com.metronome.util.domain.ScaleConvertResult;

public class ScaleConverter {

    public float[][] scaleMatrix;
    public static String[] scaleWordList={"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
    public  boolean staticScaleMode = false;
    public  int staticOctave;
    public int staticScale;

    public ScaleConverter(){
        scaleMatrix= new float[9][12];
        initScaleMatrix();
    }

    public ScaleConverter(int maxOctav ) {
        scaleMatrix= new float[maxOctav][12];
        initScaleMatrix();
    }

    private void initScaleMatrix(){
        for(int i=0; i<scaleMatrix.length; i++){
            for(int j=0; j<scaleMatrix[i].length; j++){
                scaleMatrix[i][j] = (float)(Math.pow(2.0, i-1) * 55 * Math.pow(2.0, (((j+1)-10.0)/12.0)));
            }
        }
    }

    public void setStaticScaleMode(boolean mode){
        this.staticScaleMode = mode;
    }

    public void setStaticScale(int octave, int scale ){
        this.staticOctave = octave;
        this.staticScale = scale;
    }


    public ScaleConvertResult getScale(float frequency){
        ScaleConvertResult scaleConvertResult = new ScaleConvertResult();
        int octave = 0;
        int scale=0;
        float errorFrequency = 0;
        String scaleWord="";

        //고정 음계 탐색모드
        //세팅 된 음계와 현재 주파수 비교
        if(staticScaleMode){
            scaleConvertResult.frequency = frequency;
            scaleConvertResult.octave = staticOctave;
            scaleConvertResult.scale = staticScale;

            float targetFrequency = scaleMatrix[octave][scale];
            scaleConvertResult.erroFrequency = frequency - targetFrequency;

            return scaleConvertResult;
        }
        //옥타브 계산
        for( int i=1; i < scaleMatrix.length; i++ ){
            if( frequency < scaleMatrix[i][0] ){
                octave = i-1;
                break;
            }
        }
        boolean findInterOctav = false;
        //옥타브 사이 음정 판별
        if(frequency>=scaleMatrix[octave][11]){
            findInterOctav = true;
            float upperError = frequency - scaleMatrix[octave][11] ;
            float lowerError = frequency - scaleMatrix[octave+1][0];
            if(Math.abs(upperError) < Math.abs(lowerError)){
                errorFrequency = upperError;
                scale = 11;
            }else{
                errorFrequency = lowerError;
                octave = octave +1;
                scale = 0;
            }
        }
        //음정 계산 위에서 판별시 스킵
        if(!findInterOctav) {
            for (int j = 1; j < 12; j++) {
                if (frequency <= scaleMatrix[octave][j]) {
                    float upperError = frequency - scaleMatrix[octave][j - 1];
                    float lowerError = frequency - scaleMatrix[octave][j];
                    if (Math.abs(upperError) < Math.abs(lowerError)) {
                        errorFrequency = upperError;
                        scale = j - 1;
                    } else {
                        errorFrequency = lowerError;
                        scale = j;
                    }
                    //scaleWord = scaleWordList[scale];
                    break;
                }
            }
        }
        scaleConvertResult.frequency= frequency;
        scaleConvertResult.octave = octave;
        scaleConvertResult.scale = scale;
        //scaleConvertResult.scaleWord = scaleWord;
        scaleConvertResult.erroFrequency = errorFrequency;
        return scaleConvertResult;
    }




}
