package com.metronome.util;

import com.metronome.util.domain.ScaleConvertResult;

public class ScaleConverter {

    float[][] scaleMatrix;
    String[] scaleWordList={"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};

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
                scaleMatrix[i][j] = (float)(Math.pow(2.0, i-1) * 55 * Math.pow(2.0, ((j-10.0)/12.0)));
            }
        }
    }

    public ScaleConvertResult getScale(float frequency){
        ScaleConvertResult scaleConvertResult = new ScaleConvertResult();
        int octave = 0;
        float errorFrequency = 0;
        String scaleWord=""
                ;
        for(int i=1; i<scaleMatrix.length; i++){
            if(frequency < scaleMatrix[i][0]){
                octave = i-1;
                for(int j=1; j<scaleMatrix[octave].length; j++){
                    if(frequency<scaleMatrix[octave][j]){
                        float upperError = frequency - scaleMatrix[octave][j-1] ;
                        float lowerError = frequency - scaleMatrix[octave][j];
                        if(Math.abs(upperError) < Math.abs(lowerError)){
                            errorFrequency = upperError;
                            scaleWord = scaleWordList[j-1];
                        }else{
                            errorFrequency = lowerError;
                            scaleWord = scaleWordList[j];
                        }
                        break;
                    }
                }
            }
        }

        scaleConvertResult.frequency= frequency;
        scaleConvertResult.octave = octave;
        scaleConvertResult.scaleWord = scaleWord;
        scaleConvertResult.erroFrequency = errorFrequency;
        return scaleConvertResult;
    }




}
