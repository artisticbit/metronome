package com.metronome.util;

public class TempoBPM {

    public double tempView(double bpm, double tempo) {
        return (bpm/60) * (tempo/4);
    }

    public String bpmView(int bpm) {
        String bpmText = "";

        if (bpm <= 19) {
            bpmText = "Lagamente";
        } else if (isBetween(bpm, 20, 39)) {
            bpmText = "Larghissimo";
        } else if (isBetween(bpm, 40, 49)) {
            bpmText = "Lento";
        } else if (isBetween(bpm, 50, 59)) {
            bpmText = "Lerghetto";
        } else if (isBetween(bpm, 60, 69)) {
            bpmText = "Adagio";
        } else if (isBetween(bpm, 70, 79)) {
            bpmText = "Andante";
        } else if (isBetween(bpm, 80, 99)) {
            bpmText = "Andante Moderato";
        } else if (isBetween(bpm, 100, 119)) {
            bpmText = "Allegro moderato";
        } else if (isBetween(bpm, 120, 139)) {
            bpmText = "Allegro";
        } else if (isBetween(bpm, 140, 149)) {
            bpmText = "Allegrissimo";
        } else if (isBetween(bpm, 150, 169)) {
            bpmText = "Vivace";
        } else if (isBetween(bpm, 170, 179)) {
            bpmText = "Vivacissimo";
        } else if (isBetween(bpm, 180, 199)) {
            bpmText = "Presto";
        } else if (bpm >= 200) {
            bpmText = "Prestissimo";
        }
        return bpmText;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
