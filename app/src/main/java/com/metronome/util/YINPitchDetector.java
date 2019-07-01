package com.metronome.util;


import android.media.AudioRecord;
import android.util.Log;

public class YINPitchDetector {
    // According to the YIN Paper, the threshold should be between 0.10 and 0.15
    private static final float ABSOLUTE_THRESHOLD = 0.125f;

    private final double sampleRate;
    private final float[] resultBuffer;

    public YINPitchDetector(int sampleRate, int readSize) {
        this.sampleRate = sampleRate;
        this.resultBuffer = new float[readSize / 2];
    }

    public double detect(float[] wave) {
        int tau;

        autoCorrelationDifference(wave);

        cumulativeMeanNormalizedDifference();

        tau = absoluteThreshold();

        float betterTau = parabolicInterpolation(tau);
        Log.d("test","Tau::"+betterTau);
        return sampleRate / betterTau;
    }

    private void autoCorrelationDifference(final float[] wave) {
        // Note this algorithm is currently slow (O(n^2)). Should look for any possible optimizations.
        int length = resultBuffer.length;
        int i, j;

        for (j = 1; j < length; j++) {
            for (i = 0; i < length; i++) {
                // d sub t (tau) = (x(i) - x(i - tau))^2, from i = 1 to result buffer size
                resultBuffer[j] += Math.pow((wave[i] - wave[i + j]), 2);
            }
            Log.d("test", "resultBuffer["+j+"]: "+ resultBuffer[j]);
        }
    }

    private void cumulativeMeanNormalizedDifference() {
        // newValue = oldValue / (runningSum / tau)
        // == (oldValue / 1) * (tau / runningSum)
        // == oldValue * (tau / runningSum)

        // Here we're using index i as the "tau" in the equation
        int i;
        int length = resultBuffer.length;
        float runningSum = 0f;

        // Set the first value in the result buffer to the value of one
        resultBuffer[0] = 1;

        for (i = 1; i < length; i++) {
            // The sum of this value plus all the previous values in the buffer array
            runningSum += resultBuffer[i];
            //Log.d("test","buffer["+i+"]::"+resultBuffer[i]+"runningSum::"+runningSum);
            // The current value is updated to be the curre            runningSum += resultBuffer[i];nt value multiplied by the index divided by the running sum value
            resultBuffer[i] *= i / runningSum;

        }

    }

    private int absoluteThreshold() {
        int tau;
        int length = resultBuffer.length;

        // The first two values in the result buffer should be 1, so start at the third value
        for (tau = 2; tau < length; tau++) {
            // If we are less than the threshold, continue on until we find the lowest value
            // indicating the lowest dip in the wave since we first crossed the threshold.
            if (resultBuffer[tau] < ABSOLUTE_THRESHOLD) {
                while (tau + 1 < length && resultBuffer[tau + 1] < resultBuffer[tau]) {
                    tau++;
                }

                // We have the approximate tau value, so break the loop
                break;
            }
        }

        // Some implementations of this algorithm set the tau value to -1 to indicate no correct tau
        // value was found. This implementation will just return the last tau.
        tau = tau >= length ? length - 1 : tau;

        return tau;
    }

    private float parabolicInterpolation(final int currentTau) {
        // Finds the points to fit the parabola between
        int x0 = currentTau < 1 ? currentTau : currentTau - 1;
        int x2 = currentTau + 1 < resultBuffer.length ? currentTau + 1 : currentTau;

        // Finds the better tau estimate
        float betterTau;

        if (x0 == currentTau) {
            if (resultBuffer[currentTau] <= resultBuffer[x2]) {
                betterTau = currentTau;
            } else {
                betterTau = x2;
            }
        } else if (x2 == currentTau) {
            if (resultBuffer[currentTau] <= resultBuffer[x0]) {
                betterTau = currentTau;
            } else {
                betterTau = x0;
            }
        } else {
            // Fit the parabola between the first point, current tau, and the last point to find a
            // better tau estimate.
            float s0 = resultBuffer[x0];
            float s1 = resultBuffer[currentTau];
            float s2 = resultBuffer[x2];

            betterTau = currentTau + (s2 - s0) / (2 * (2 * s1 - s2 - s0));
        }

        return betterTau;
    }
}
