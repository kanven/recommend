package com.kanven.recommend.decision;


import com.kanven.recommend.utils.Entropy;
import com.kanven.recommend.utils.Stat;

import java.util.*;

public class DecisionTree {

    private Node root;

    public void train(double[][] samples, String[] labels) {
        buildTree(samples, labels, root);
        printTree();
    }

    private void buildTree(double[][] samples, String[] labels, Node current) {
        //特征统计
        List<Feature> features = stat(samples, labels);
        //计算特征熵
        double[] es = calculateFeatureEntropy(features);
        //获取熵最小的特征
        Stat.Tuple<Integer, Double> tuple = Stat.min(es);
        int idx = tuple.left();
        Feature feature = features.get(idx);
        System.out.println(tuple.left() + ":" + tuple.right());
        //构建节点
        Node node = buildTree(feature, current);
        List<Attribute> attrs = feature.attrs;
        for (Attribute attr : attrs) {
            if (attr.stats.size() == 1) {
                //确定标签值
                Node leaf = new Node();
                leaf.type = Type.LEAF;
                for (double key : attr.stats.keySet()) {
                    leaf.label = key;
                }
                node.addChild(leaf);
                continue;
            }
            //过滤特征下样本数据
            Stat.Tuple<double[][], String[]> filter = filter(samples, labels, feature, attr);
            buildTree(filter.left(), filter.right(), node);
        }
    }

    private Node buildTree(Feature feature, Node current) {
        Node node = new Node();
        node.feature = feature;
        if (current == null) {
            node.type = Type.ROOT;
            root = node;
        } else {
            current.addChild(node);
        }
        return node;
    }

    private Stat.Tuple<double[][], String[]> filter(double[][] samples, String[] labels, Feature feature, Attribute attribute) {
        int idx = feature.feature;
        double attr = attribute.attr;
        int len = samples.length;
        double[][] subs = new double[attribute.total][labels.length];
        //深度拷贝
        int n = 0;
        for (int i = 0; i < len; i++) {
            if (samples[i][idx] == attr) {
                double[] sample = samples[i];
                int pos = 0;
                for (int c = 0; c < sample.length; c++) {
                    if (c != idx) {
                        subs[n][pos++] = sample[c];
                    }
                }
                n++;
            }
        }
        String[] sl = new String[labels.length - 1];
        int pos = 0;
        for (int i = 0; i < labels.length; i++) {
            if (i != idx) {
                sl[pos++] = labels[i];
            }
        }
        return new Stat.Tuple<double[][], String[]>(subs, sl);
    }

    private double[] calculateFeatureEntropy(List<Feature> features) {
        double[] entropy = new double[features.size()];
        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);
            List<Attribute> attrs = feature.attrs;
            for (Attribute attr : attrs) {
                double e = calculateAttributeEntropy(attr);
                entropy[i] += attr.total / feature.total * e;
            }
        }
        return entropy;
    }

    private double calculateAttributeEntropy(Attribute attr) {
        double e = 0;
        for (double value : attr.stats.values()) {
            double p = value / attr.total;
            e += Entropy.entropy(p);
        }
        return e;
    }

    private List<Feature> stat(double[][] samples, String[] labels) {
        List<Feature> features = new ArrayList<Feature>();
        int len = samples.length;
        int cells = labels.length;
        for (int c = 0; c < cells; c++) {
            List<Attribute> attrs = new ArrayList<Attribute>();
            Map<Double, Map<Double, Double>> mps = new HashMap<Double, Map<Double, Double>>();
            for (int i = 0; i < len; i++) {
                double value = samples[i][c];
                if (mps.get(value) == null) {
                    mps.put(value, new HashMap<Double, Double>());
                }
                double kind = samples[i][labels.length];
                mps.get(value).put(kind, mps.get(value).get(kind) == null ? 1 : mps.get(value).get(kind) + 1);
            }
            Set<Double> keys = mps.keySet();
            for (double key : keys) {
                Attribute attr = new Attribute(key, mps.get(key));
                attrs.add(attr);
            }
            Feature feature = new Feature(c, labels[c], attrs);
            features.add(feature);
        }
        System.out.println("the features is:" + features);
        return features;
    }

    private static class Feature {

        private final int feature;

        private final String label;

        private final double total;

        private final List<Attribute> attrs;

        public Feature(int feature, String label, List<Attribute> attrs) {
            this.feature = feature;
            this.label = label;
            this.attrs = attrs;
            int total = 0;
            for (Attribute attr : attrs) {
                total += attr.total;
            }
            this.total = total;
        }

        @Override
        public String toString() {
            return "{\"feature\":" + feature + ", \"label\":\"" + label + "\", \"total\":" + total + ", \"attrs\":[" + attrs + "]}";
        }
    }

    private static class Attribute {

        private final double attr;

        private int total;

        private Map<Double, Double> stats;

        public Attribute(double attr, Map<Double, Double> stats) {
            this.attr = attr;
            this.stats = stats;
            int sum = 0;
            for (double stat : stats.values()) {
                sum += stat;
            }
            this.total = sum;
        }

        @Override
        public String toString() {
            return "{" +
                    "attr=" + attr +
                    ", total=" + total +
                    ", stats=" + stats +
                    '}';
        }
    }


    private class Node {

        private Type type = Type.COMMON;

        private Feature feature;

        private double label;

        private List<Node> children = new ArrayList<Node>(0);

        public void addChild(Node node) {
            children.add(node);
        }

    }

    private void  printTree(){
        System.out.println("===");
    }

    private enum Type {
        ROOT,
        LEAF,
        COMMON
    }


    public static void main(String[] args) {
        DecisionTree decision = new DecisionTree();
        decision.train(data, labels);
    }

    private final static double[][] data = {
            {2, 2, 1, 0, 1},
            {2, 2, 1, 1, 0},
            {1, 2, 1, 0, 1},
            {0, 0, 0, 0, 1},
            {0, 0, 0, 1, 0},
            {1, 0, 0, 1, 1},
            {2, 1, 1, 0, 0},
            {2, 0, 0, 0, 1},
            {0, 1, 0, 0, 1},
            {2, 1, 0, 1, 1},
            {1, 2, 0, 0, 0},
            {0, 1, 1, 1, 0}
    };

    private final static String[] labels = {"天气", "温度", "湿度", "风速"};

}