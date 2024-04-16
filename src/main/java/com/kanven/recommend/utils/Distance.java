package com.kanven.recommend.utils;

public class Distance {

    /**
     * 欧式距离
     *
     * @param x
     * @param y
     * @return
     */
    public static double euclidean(double[] x, double[] y) {
        int len = x.length;
        double distance = 0;
        for (int i = 0; i < len; i++) {
            distance += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(distance);
    }

    /**
     * 曼哈顿距离
     *
     * @param x
     * @param y
     * @return
     */
    public static double manhattan(double[] x, double[] y) {
        int len = x.length;
        double distance = 0;
        for (int i = 0; i < len; i++) {
            distance += Math.abs(x[i] - y[i]);
        }
        return distance;
    }

    /**
     * 切比雪夫距离
     *
     * @param x
     * @param y
     * @return
     */
    public static double chebyshev(double[] x, double[] y) {
        int len = x.length;
        double distance = 0;
        for (int i = 0; i < len; i++) {
            distance = Math.max(distance, Math.abs(x[i] - y[i]));
        }
        return distance;
    }

}
