package com.kanven.recommend.lr;

import java.util.ArrayList;
import java.util.List;

public class LinearRegression {

    /**
     * 参数
     */
    private final double[] params;

    /**
     * 学习率
     */
    private final double leanRate;

    /**
     * 阀值
     */
    private final double threshold;

    public LinearRegression(double[] params, double leanRate, double threshold) {
        this.params = params;
        this.leanRate = leanRate;
        this.threshold = threshold;
    }

    public void train(List<Sample> samples) {
        int epoch = 0;
        while (true) {
            double[] dts = gradient(samples);
            double cost = cost(samples);
            if (judge(dts) || cost < threshold || Double.isInfinite(cost)) {
                break;
            }
            updateParams(dts);
            epoch++;
            System.out.println("the " + epoch + " epoch params:" + output() + ",cost:" + cost);
        }
    }

    public String output() {
        StringBuilder builder = new StringBuilder();
        int len = params.length;
        builder.append("[");
        for (int i = 0; i < len; i++) {
            builder.append(params[i]);
            if (i < len - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * 更新参数
     *
     * @param dts
     */
    private boolean updateParams(double[] dts) {
        int len = params.length;
        for (int i = 0; i < len; i++) {
            params[i] -= leanRate * dts[i];
        }
        return true;
    }

    public boolean judge(double[] dts) {
        boolean flag = true;
        for (int i = 0; i < dts.length; i++) {
            if (Math.abs(dts[i]) > threshold) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 批量梯度下降计算
     * 书籍：《推荐系统开发实战》p217-p222
     * 网络文章 https://www.toutiao.com/article/7050353756256453132/?app=news_article&timestamp=1711390381&use_new_style=1&req_id=20240326021300FE067E102E0F344BE7DC&group_id=7050353756256453132&wxshare_count=1&tt_from=weixin&utm_source=weixin&utm_medium=toutiao_android&utm_campaign=client_share&share_token=0a8c0beb-03ec-4a94-94e3-54022022e18a&source=m_redirect
     *
     * @param samples
     */
    private double[] gradient(List<Sample> samples) {
        int len = params.length;
        double[] dts = new double[len];
        for (int i = 0; i < len; i++) {
            double dt = 0;
            for (Sample sample : samples) {
                dt += (calculate(sample) - sample.value) * sample.features[i];
            }
            dts[i] = dt / samples.size();
        }
        return dts;
    }

    /**
     * 计算样本值
     *
     * @param sample
     * @return
     */
    private double calculate(Sample sample) {
        double y = 0;
        int len = params.length;
        for (int i = 0; i < len; i++) {
            y += sample.features[i] * params[i];
        }
        return y;
    }

    /**
     * 损失函数（计算样本方差）
     *
     * @return
     */
    private double cost(List<Sample> samples) {
        double sum = 0;
        for (Sample sample : samples) {
            sum += Math.pow(calculate(sample) - sample.value, 2);
        }
        return sum / (2 * samples.size());
    }


    public static class Sample {

        /**
         * 特征值列表
         */
        private double[] features;

        /**
         * 分类值
         */
        private double value;

        /**
         * 标签
         */
        private int label;

    }

    private static double[][] train = {
            {1, 2104, 5, 1, 45, 460},
            {1, 1416, 3, 2, 40, 232},
            {1, 1534, 3, 2, 30, 315},
            {1, 852, 2, 1, 36, 178},
            {1, 1254, 3, 3, 45, 321},
            {1, 987, 2, 2, 35, 241},
            {1, 1054, 3, 2, 30, 287},
            {1, 645, 2, 3, 25, 87},
            {1, 542, 2, 1, 30, 94},
            {1, 1065, 3, 1, 25, 241},
            {1, 2465, 7, 2, 50, 687},
            {1, 2410, 6, 1, 45, 654},
            {1, 1987, 4, 2, 45, 436},
            {1, 457, 2, 3, 35, 65},
            {1, 587, 2, 2, 25, 54},
            {1, 468, 2, 1, 40, 87},
            {1, 1354, 3, 1, 35, 215},
            {1, 1587, 4, 1, 45, 345},
            {1, 1789, 4, 2, 35, 325},
            {1, 2500, 8, 2, 40, 720}
    };

    private static double[][] test = {
            {1, 0.23, 0.35, 0},
            {1, 0.32, 0.24, 0},
            {1, 0.6, 0.12, 0},
            {1, 0.36, 0.54, 0},
            {1, 0.02, 0.89, 0},
            {1, 0.36, -0.12, 0},
            {1, -0.45, 0.62, 0},
            {1, 0.56, 0.42, 0},
            {1, 0.4, 0.56, 0},
            {1, 0.46, 0.51, 0},
            {1, 1.2, 0.32, 1},
            {1, 0.6, 0.9, 1},
            {1, 0.32, 0.98, 1},
            {1, 0.2, 1.3, 1},
            {1, 0.15, 1.36, 1},
            {1, 0.54, 0.98, 1},
            {1, 1.36, 1.05, 1},
            {1, 0.22, 1.65, 1},
            {1, 1.65, 1.54, 1},
            {1, 0.25, 1.68, 1}
    };

    public static void main(String[] args) {
        //构建训练样本
        List<Sample> samples = new ArrayList<Sample>();
        int len = train.length;
        for (int i = 0; i < len; i++) {
            double[] sample = train[i];
            double[] features = {sample[0], sample[1], sample[2], sample[3], sample[4]};
            Sample s = new Sample();
            s.features = features;
            s.value = sample[5];
            samples.add(s);
        }
        //初始化线性回归参数
        double[] params = {0, 0, 0, 0, 0};
        double rate = 0.5;
        double th = 0.001;
        LinearRegression lr = new LinearRegression(params, rate, th);
        lr.train(samples);
        String model = lr.output();
        System.out.println("linner regression model params is:" + model);
    }

}