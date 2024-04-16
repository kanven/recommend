package com.kanven.recommend;

import java.util.List;
import java.util.Random;

public class Bootstrap {

    private static final Random random = new Random();

    public double[] train(int[] samples, int size, int epochs) {
        double[] args = new double[epochs];
        for (int i = 0; i < epochs; i++) {
            int[] items = random(samples, size);
            double arg = arg(items);
            args[i] = arg;
        }
        double arg = arg(args);
        double var = variance(args, arg);
        return new double[]{arg, var};
    }

    private int[] random(int[] samples, int size) {
        int[] items = new int[samples.length];
        for (int i = 0; i < size; i++) {
            int idx = random.nextInt(items.length);
            items[i] = samples[idx];
        }
        return items;
    }

    /**
     * 均值计算
     *
     * @param items
     * @return
     */
    private double arg(int[] items) {
        double sum = 0;
        for (Integer item : items) {
            sum += item;
        }
        return sum / items.length;
    }

    private double arg(double[] items) {
        double sum = 0;
        for (double item : items) {
            sum += item;
        }
        return sum / items.length;
    }


    /**
     * 方差计算
     *
     * @param items
     * @return
     */
    private double variance(double[] items, double arg) {
        double sum = 0;
        for (double item : items) {
            sum += Math.pow(item - arg, 2);
        }
        return sum / (items.length - 1);
    }

    private void println(List<Integer> samples) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0, len = samples.size(); i < len; i++) {
            builder.append(samples.get(i));
            if (i != len - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        int[] samples = {1, 3, 5, 9, 12, 45, 67, 21, 16, 8, 87, 100, 32, 45};
        int target = 4;
        int epochs = 6;
        Bootstrap bootstrap = new Bootstrap();
        double[] results = bootstrap.train(samples, target, epochs);
        System.out.println("the arg:" + results[0] + ",the var:" + results[1]);
    }

}
