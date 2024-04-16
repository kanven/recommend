package com.kanven.recommend.utils;

public class Mean {

    public static double mean(double[] items) {
        int len = items.length;
        return Sum.sum(items) / len;
    }

}
