package com.kanven.recommend.utils;

public class Sum {

    public static double sum(double[] items) {
        int len = items.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += items[i];
        }
        return sum;
    }

}
