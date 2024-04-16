package com.kanven.recommend;

import com.kanven.recommend.utils.Distance;
import com.kanven.recommend.utils.Mean;

import java.util.*;

/**
 * k-mean 聚类算法
 */
public class KMean {

    private static final Random random = new Random();

    private int k;

    private List<Center> centers;

    private Map<Integer, List<Sample>> clazz;

    public KMean(int k) {
        this.k = k;
        centers = new ArrayList<Center>(k);
    }

    public void train(List<Sample> samples, int epochs) {
        init(samples);
        do {
            classify(samples);
            updateCenter(clazz);
        } while (--epochs > 0);
    }

    public void predict(double[] sample) {

    }

    /**
     * 初始化k中心节点
     */
    private void init(List<Sample> samples) {
        int len = samples.size();
        for (int i = 0; i < k; k++) {
            int idx = random.nextInt(len);
            Center center = new Center(i, samples.get(idx));
            centers.add(center);
        }
    }

    /**
     * 更新聚类中心节点(中心节点可以是虚拟节点)
     *
     * @param clazz
     */
    private void updateCenter(Map<Integer, List<Sample>> clazz) {
        for (int key : clazz.keySet()) {
            Center center = centers.get(key);
            List<Sample> samples = clazz.get(key);
            double[][] features = new double[samples.size()][];
            for (int i = 0; i < samples.size(); i++) {
                Sample sample = samples.get(i);
                features[i] = sample.features;
            }
            int num = features.length;
            int cl = features[0].length;
            for (int c = 0; c < cl; c++) {
                double[] cells = new double[num];
                for (int n = 0; n < num; n++) {
                    cells[n] = features[n][c];
                }
                center.sample.features[c] = Mean.mean(cells);
            }
        }
    }

    /**
     * 按中心节点聚集
     *
     * @param samples 样本集
     * @return
     */
    private void classify(List<Sample> samples) {
        this.clazz = new HashMap<Integer, List<Sample>>();
        for (Sample sample : samples) {
            double min = Double.MAX_VALUE;
            int idx = -1;
            for (int i = 0; i < k; i++) {
                double distance = Distance.euclidean(sample.features, centers.get(i).sample.features);
                if (distance < min) {
                    min = distance;
                    idx = i;
                }
            }
            if (clazz.get(idx) == null) {
                clazz.put(idx, new ArrayList<Sample>());
            }
            clazz.get(idx).add(sample);
        }
    }

    private class Classify {

        private int k;

        private List<Sample> samples;

        public Classify() {

        }

        public Classify(int k) {
            this.k = k;
        }

        public Classify(int k, List<Sample> samples) {
            this.k = k;
            this.samples = samples;
        }

    }

    private class Sample {

        private double[] features;

        public Sample(double[] features) {
            this.features = features;
        }

    }

    private class Center {

        private int idx;

        private Sample sample;

        public Center(int idx, Sample sample) {
            this.idx = idx;
            this.sample = sample;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Center center = (Center) o;
            return idx == center.idx;
        }

        @Override
        public int hashCode() {
            return Objects.hash(idx);
        }
    }

}
