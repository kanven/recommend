package com.kanven.recommend.cf;

public class ItemCF {

    //从存储上而言存在优化空间，可以节省一半内存
    private double[][] sims;

    private double[][] samples;

    private int products;


    public ItemCF(double[][] samples) {
        this.samples = samples;
        this.products = samples[0].length;
        this.sims = new double[products][products];
        similarity();
    }

    public double[] recommend(int user) {
        double[] products = samples[user];
        double[] scores = new double[products.length];
        for (int p = 0; p < products.length; p++) {
            scores[p] = calculateItemScore(user, p);
        }
        return scores;
    }

    /**
     * 预估用户未评分商品得分
     *
     * @param user
     * @param item
     * @return
     */
    public double calculateItemScore(int user, int item) {
        //获取用户未打分商品同其他商品的相似度
        double[] sims = this.sims[item];
        //获取用户打分的商品（没有打分的商品为0）
        double[] products = this.samples[user];
        //未打分商品用户打分预估
        double sum = 0;
        for (int i = 0; i < sims.length; i++) {
            if (i == item) {
                continue;
            }
            double score = products[i];
            sum += score * sims[i];
        }
        return sum;
    }

    /**
     * 商品相似度计算
     */
    private void similarity() {
        double[] items = new double[products];
        double[][] pmp = new double[this.products][this.products];
        int len = this.samples.length;
        for (int i = 0; i < len; i++) {
            //统计商品喜欢的用户数
            for (int k = 0; k < this.products; k++) {
                if (samples[i][k] > 0) {
                    items[k] += 1;
                }
            }
            //统计共同喜欢的商品用户数
            double[] products = samples[i];
            for (int j = 0; j < this.products; j++) {
                if (products[j] > 0) {
                    for (int k = j + 1; k < this.products; k++) {
                        if (products[k] > 0) {
                            pmp[j][k] += 1;
                            pmp[k][j] = pmp[j][k];
                        }
                    }
                }
            }
        }
        for (int p = 0; p < this.products; p++) {
            for (int i = 0; i < this.products; i++) {
                if (p == i) {
                    continue;
                }
                this.sims[p][i] = pmp[p][i] / items[p];
            }
        }
    }


    public static void main(String[] args) {
        ItemCF cf = new ItemCF(datas);
        double[] scores = cf.recommend(2);
        StringBuilder builder = new StringBuilder("[");
        int len = scores.length;
        for (int i = 0; i < len; i++) {
            builder.append(i + ":" + scores[i]);
            if (i != len - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        System.out.print(builder);
    }

    private static final double[][] datas = {
            {3, 4, 0, 3.5, 0},
            {4, 0, 4.5, 0, 3.5},
            {0, 3.5, 0, 0, 3},
            {0, 4, 0, 3.5, 3}
    };

}
