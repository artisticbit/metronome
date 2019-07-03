package com.metronome.util;

public class ScaleConverter {
    float[][] scaleMatrix;
    String[] scaleWord={"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
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

    public int getOctave(float frequency){
        for(int i=0; i<scaleMatrix.length; i++){
            if(frequency < scaleMatrix[i][0]){
                return i-1;
            }
        }
        return 0;
    }
    public String getPitchWord(float frequency){
        int octave = 0;
        for(int i=0; i<scaleMatrix.length; i++){
            if(frequency < scaleMatrix[i][0]){

            }
        }
        return "";
    }




}
