package com.metronome.util;

public class ScaleConverter {
    float[][] scaleMatrix;
    String[] scaleWord={"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
    public ScaleConverter(){
        scaleMatrix= new float[9][12];
    }

    public ScaleConverter(int maxOctav ){
        scaleMatrix= new float[maxOctav][12];
    }

    private void initScaleMatrix(){
        for(int i=0; i<scaleMatrix.length; i++){
            for(int j=0; j<scaleMatrix[i].length; j++){
                scaleMatrix[i][j] = (float)(Math.pow(2.0, i-1) * 55 * Math.pow(2.0, ((j-10.0)/12.0)));
            }
        }
    }


}
