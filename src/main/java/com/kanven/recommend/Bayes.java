package com.kanven.recommend;

import java.util.*;

public class Bayes {

    /**
     * 类别概率
     */
    private double[] cp;

    private Map<Integer, Map<Integer, List<Double>>> params;


    public void train(int[][] samples, int[] labels) {
        Set<Integer> sls = new HashSet<Integer>();
        for (int label : labels) {
            sls.add(label);
        }
        int clazz = sls.size();
        cp = new double[clazz];
        this.calculateClazz(labels);
        Map<Integer, Map<Integer, List<Integer>>> partions = split(samples, labels);
        this.params = calculate(partions);
    }

    public int predict(int[] sample) {
        //求取归属类别最大值
        int clazz = -1;
        double max = 0;
        int len = cp.length;
        for (int i = 0; i < len; i++) {
            double py = cp[i];
            double px = 1;
            Map<Integer, List<Double>> features = this.params.get(i);
            for (int key : features.keySet()) {
                List<Double> values = features.get(key);
                int val = sample[key];
                px *= gauss(values.get(0), values.get(1), val);
            }
            System.out.println("the clazz is:" + i + ",the p is:" + px);
            if (px > max) {
                max = px;
                clazz = i;
            }
        }
        return clazz;
    }

    /**
     * @param arg      特征平均值
     * @param variance 特征方差
     * @param feature  预测样本特值
     * @return 概率
     */
    private double gauss(double arg, double variance, int feature) {
        return 1 / (Math.sqrt(2 * Math.PI) * variance) * Math.pow(Math.E, -Math.pow(feature - arg, 2) / (2 * variance));
    }

    /**
     * 计算不同类别、特征的均值、方差
     *
     * @param partions
     * @return
     */
    private Map<Integer, Map<Integer, List<Double>>> calculate(Map<Integer, Map<Integer, List<Integer>>> partions) {
        Map<Integer, Map<Integer, List<Double>>> mapping = new HashMap<Integer, Map<Integer, List<Double>>>();
        Set<Integer> clazzs = partions.keySet();
        for (Integer clazz : clazzs) {
            mapping.put(clazz, new HashMap<Integer, List<Double>>());
            Map<Integer, List<Integer>> features = partions.get(clazz);
            Set<Integer> keys = features.keySet();
            for (Integer key : keys) {
                List<Integer> values = features.get(key);
                mapping.get(clazz).put(key, calculate(values));
            }
        }
        return mapping;
    }

    /**
     * 计算特征均值和方差
     *
     * @param features
     * @return
     */
    private List<Double> calculate(List<Integer> features) {
        final double arg = arg(features);
        final double variance = variance(features, arg);
        return new ArrayList<Double>() {{
            add(arg);
            add(variance);
        }};
    }

    /**
     * 训练集按照<类别,特征，特征值>维度划分
     *
     * @param samples
     * @param labels
     * @return
     */
    public Map<Integer, Map<Integer, List<Integer>>> split(int[][] samples, int[] labels) {
        Map<Integer, Map<Integer, List<Integer>>> partitions = new HashMap<Integer, Map<Integer, List<Integer>>>();
        for (int l = 0; l < labels.length; l++) {
            int label = labels[l];
            if (partitions.get(label) == null) {
                partitions.put(label, new HashMap<Integer, List<Integer>>());
            }
            Map<Integer, List<Integer>> features = partitions.get(label);
            int[] sample = samples[l];
            for (int i = 0; i < sample.length; i++) {
                if (features.get(i) == null) {
                    features.put(i, new ArrayList<Integer>());
                }
                features.get(i).add(sample[i]);
            }
        }
        return partitions;
    }

    /**
     * 计算不同类别概率
     *
     * @param labels
     */
    private void calculateClazz(int[] labels) {
        for (int label : labels) {
            cp[label]++;
        }
        for (int i = 0; i < cp.length; i++) {
            cp[i] = cp[i] / labels.length;
        }
    }


    /**
     * 计算特征均值
     *
     * @param features
     * @return
     */
    private double arg(List<Integer> features) {
        double sum = 0;
        for (int feature : features) {
            sum += feature;
        }
        return sum / features.size();
    }

    /**
     * 计算特征方差
     *
     * @param features
     * @param arg
     * @return
     */
    private double variance(List<Integer> features, double arg) {
        double sum = 0;
        for (int feature : features) {
            sum += Math.pow(feature - arg, 2);
        }
        return sum / features.size();
    }


    private static int[][] datas = {
            {320, 204, 198, 265},
            {253, 53, 15, 2243},
            {53, 32, 5, 325},
            {63, 50, 42, 98},
            {1302, 523, 202, 5430},
            {32, 22, 5, 143},
            {105, 85, 70, 322},
            {872, 730, 840, 2762},
            {16, 15, 13, 52},
            {92, 70, 21, 693}
    };

    private static int[] labels = {1, 0, 0, 1, 0, 0, 1, 1, 1, 0};

    public static void main(String[] args) {
        Bayes bayes = new Bayes();
        bayes.train(datas, labels);
        System.out.println(bayes.predict(new int[]{134, 84, 235, 349}));
    }

}
