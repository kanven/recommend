package com.kanven.recommend.cf;

public class UserCF {

    private double[][] samples;

    private double[][] sims;

    public UserCF(double[][] samples) {
        this.samples = samples;
        similarity(samples);
    }

    public double[] recommend(int user) {
        int products = samples[0].length;
        double[] results = new double[products];
        for (int i = 0; i < products; i++) {
            //给用户未评分商品打分
            if (samples[user][i] == 0) {
                results[i] = calculateItemScore(user, i);
            } else {
                results[i] = samples[user][i];
            }
        }
        return results;
    }

    private void similarity(double[][] samples) {
        this.sims = new double[samples.length][samples.length];
        int len = samples.length;
        for (int i = 0; i < len; i++) {
            double[] target = samples[i];
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                double[] user = samples[j];
                double sim = calculate(target, user);
                this.sims[i][j] = sim;
            }
        }
    }

    private double calculateItemScore(int user, int item) {
        double score = 0;
        double[] ss = sims[user];
        for (int i = 0; i < ss.length; i++) {
            if (i != user) {
                double sim = ss[i];
                score += sim * samples[i][item];
            }
        }
        return score;
    }


    /**
     * 计算两个用户相似度
     *
     * @param target
     * @param user
     * @return
     */
    private double calculate(double[] target, double[] user) {
        double mixed = 0;
        double tc = 0;
        double uc = 0;
        int len = target.length;
        for (int i = 0; i < len; i++) {
            if (target[i] != 0) {
                tc++;
            }
            if (user[i] != 0) {
                uc++;
            }
            if (target[i] * user[i] != 0) {
                mixed++;
            }
        }
        if (tc == 0 || uc == 0) {
            return 0;
        }
        return mixed / Math.sqrt(tc * uc);
    }

    /**
     * 纵坐标表示用户A,B,C,D
     * 横坐标表示商品a,b,c,d,e
     */
    private static double[][] ssamples = {
            {3, 4, 0, 3.5, 0},
            {4, 0, 4.5, 0, 3.5},
            {0, 3.5, 0, 0, 3},
            {0, 4, 0, 3.5, 3}
    };

    public static void main(String[] args) {
        UserCF cf = new UserCF(ssamples);
        double[] scores = cf.recommend(2);
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < scores.length; i++) {
            builder.append(scores[i]);
            if (i != scores.length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        System.out.print(builder.toString());
    }

}
