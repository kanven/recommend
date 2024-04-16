package com.kanven.recommend.utils;

public class Entropy {

    public static double entropy(double[] p, double rate) {
        double e = 0;
        int len = p.length;
        for (int i = 0; i < len; i++) {
            e += p[i] * (Math.log(p[i]) / Math.log(2));
        }
        return -e * rate;
    }

    public static double entropy(double p) {
        if (p == 0) {
            return 0;
        }
        return -p * (Math.log(p) / Math.log(2));
    }

    public static void main(String[] args) {
        double[] p = new double[2];
        p[0] = 1.0 / 3.0;
        p[1] = 2.0 / 3.0;
        System.out.println(entropy(p, 3.0 / 5));
        //System.out.println( Math.log(1.0/2) / Math.log(2));
    }

}
