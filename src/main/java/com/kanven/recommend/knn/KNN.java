package com.kanven.recommend.knn;

import java.util.HashMap;
import java.util.Map;

public class KNN {

    /**
     * {身高,体重}
     */
    private static final int[][] origins = {
            {180, 76},
            {158, 43},
            {176, 78},
            {161, 49}
    };

    /**
     * 性别：男：1，女：0
     */
    private static final int[] labels = {1, 0, 1, 0};

    /**
     * 数据min-max标准化
     *
     * @param datas
     * @return
     */
    private static Tuple<double[][]> normalize(int[][] datas) {
        int len = datas.length, cl = datas[0].length;
        double results[][] = new double[len][cl];
        double min_max[][] = new double[cl][2];
        for (int cell = 0; cell < cl; cell++) {
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
            for (int i = 0; i < len; i++) {
                min = Math.min(min, datas[i][cell]);
                max = Math.max(max, datas[i][cell]);
            }
            min_max[cell][0] = min;
            min_max[cell][1] = max;
            double dt = max - min;
            for (int i = 0; i < len; i++) {
                results[i][cell] = (datas[i][cell] - min) / dt;
            }
        }
        return new Tuple<double[][]>(results, min_max);
    }

    private static int knn(double[] one, double[][] datas, int[] labels) {
        int len = datas.length, cl = datas[0].length;
        double[][] distances = new double[len][2];
        for (int i = 0; i < len; i++) {
            double distance = 0;
            for (int c = 0; c < cl; c++) {
                distance += Math.pow(one[c] - datas[i][c], 2);
            }
            distances[i][0] = i;
            distances[i][1] = distance;
        }
        sort(distances);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int k = 0; k < 2; k++) {
            int idx = (int) distances[k][0];
            int gender = labels[idx];
            Integer val = map.get(gender);
            if (val == null) {
                val = 1;
            } else {
                val++;
            }
            map.put(gender, val);
        }
        int key = 0, max = Integer.MIN_VALUE;
        for (int k : map.keySet()) {
            if (map.get(k) > max) {
                max = map.get(k);
                key = k;
            }
        }
        return key;
    }

    public static void sort(double[][] datas) {
        int len = datas.length;
        for (int i = 0; i < len; i++) {
            for (int j = len - 1; j > i; j--) {
                double first = datas[j][1];
                double second = datas[j - 1][1];
                if (first < second) {
                    double val = datas[j][1];
                    double idx = datas[j][0];
                    datas[j][0] = datas[j - 1][0];
                    datas[j][1] = datas[j - 1][1];
                    datas[j - 1][0] = idx;
                    datas[j - 1][1] = val;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] one = {176, 76};
        Tuple<double[][]> tuple = normalize(origins);
        double[][] left = tuple.left();
        double[][] right = tuple.right();
        double[] new_one = {(one[0] - right[0][0]) / (right[0][1] - right[0][0]), (one[1] - right[1][0]) / (right[1][1] - right[1][0])};
        System.out.println(knn(new_one, left, labels));
    }


    public static class Tuple<T> {

        private T left;

        private T right;

        public Tuple(T left, T right) {
            this.left = left;
            this.right = right;
        }

        public T left() {
            return this.left;
        }

        public T right() {
            return this.right;
        }

    }


}
