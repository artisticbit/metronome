package com.metronome.util;
import java.lang.Math;

public class FFT {

    public static int bitReverse(int n, int bits) {
        int reversedN = n;
        int count = bits - 1;

        n >>= 1;
        while (n > 0) {
            reversedN = (reversedN << 1) | (n & 1);
            count--;
            n >>= 1;
        }

        return ((reversedN << count) & ((1 << bits) - 1));
    }

    static void fft(Complex[] buffer) {

        int bits = (int) (Math.log(buffer.length) / Math.log(2));
        for (int j = 1; j < buffer.length / 2; j++) {

            int swapPos = bitReverse(j, bits);
            Complex temp = buffer[j];
            buffer[j] = buffer[swapPos];
            buffer[swapPos] = temp;
        }

        for (int N = 2; N <= buffer.length; N <<= 1) {
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {

                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    Complex even = buffer[evenIndex];
                    Complex odd = buffer[oddIndex];

                    double term = (-2 * Math.PI * k) / (double) N;
                    Complex exp = (new Complex(Math.cos(term), Math.sin(term)).mult(odd));

                    buffer[evenIndex] = even.add(exp);
                    buffer[oddIndex] = even.sub(exp);
                }
            }
        }
    }

    static double[] fft(double buffer[]){
        Complex[] complexBuffer= new Complex[buffer.length];
        int length = buffer.length;
        for(int i=0; i<length; i++){
            complexBuffer[i] = new Complex(buffer[i],0.0);
        }

        fft(complexBuffer);
        return getAbs(complexBuffer);
    }

    static double[] getAbs(Complex[] complexBuffer){
        int length = complexBuffer.length;
        double[] absBuffer = new double[length];
        for(int i=0; i<length; i++){
            double re = complexBuffer[i].re;
            double im = complexBuffer[i].im;
            absBuffer[i]= Math.sqrt( (re * re) + (im*im));
        }
        return absBuffer;
    }
}

class Complex {
    public final double re;
    public final double im;

    public Complex() {
        this(0, 0);
    }

    public Complex(double r, double i) {
        re = r;
        im = i;
    }

    public Complex add(Complex b) {
        return new Complex(this.re + b.re, this.im + b.im);
    }

    public Complex sub(Complex b) {
        return new Complex(this.re - b.re, this.im - b.im);
    }

    public Complex mult(Complex b) {
        return new Complex(this.re * b.re - this.im * b.im,
                this.re * b.im + this.im * b.re);
    }

    @Override
    public String toString() {
        return String.format("(%f,%f)", re, im);
    }
}

