package com.kanven.recommend.utils;

public class Stat {

    public static Tuple<Integer, Double> min(double[] items) {
        int idx = -1;
        double min = Double.MAX_VALUE;
        int len = items.length;
        for (int i = 0; i < len; i++) {
            double item = items[i];
            if (item < min) {
                min = item;
                idx = i;
            }
        }
        return new Tuple<Integer, Double>(idx, min);
    }

    public static class Tuple<L, R> {

        private final L left;

        private final R right;

        public Tuple(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L left() {
            return left;
        }

        public R right() {
            return right;
        }

    }

}
