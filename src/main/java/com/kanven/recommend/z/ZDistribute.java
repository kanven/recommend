package com.kanven.recommend.z;

/**
 * Z分布计算工具类
 */
public class ZDistribute {

    private static final float LOWER_LIMIT = -3.89f;

    private static final float UPPER_LIMIT = 3.89f;

    private static final float DURATION = 0.0001f;

    private final static float SIGNIFICANT_LEVEL = 0.05f;


    /**
     * 通过置信度获取Z值
     * 根据分割积分法来逆向求积分上限
     * -3.89～3.89区间外的积分面积 小于 0.0001，
     * 所以确定有效的积分区间为-3.89～3.89
     * 在实现分割的时候精度定为0.0001，得到的结果和查表得到的结果误差在-0.0002～+0.0002之间（已经检验）
     *
     * @param p 负无穷到积分上限的积分值
     * @return 积分上限
     */

    public static float zValue(float p) {
        float ret = 0;
        if (p >= 1) {
            return UPPER_LIMIT;
        } else if (p <= 0) {
            return LOWER_LIMIT;
        }

        float temp = LOWER_LIMIT;
        while (ret < p) {
            ret += DURATION * fx(temp);
            temp += DURATION;
        }

        return temp;
    }

    /**
     * 通过Z值获取置信度
     * 根据分割积分法来求得积分值
     * -3.89～3.89区间外的积分面积 小于 0.0001，
     * 所以确定有效的积分区间为-3.89～3.89
     * 在实现分割的时候精度定为0.0001，得到的结果和查表得到的结果误差在-0.0002～+0.0002之间（已经检验）
     *
     * @param z 积分上限
     * @return 积分值
     */
    public static float confidence(float z) {
        float ret = 0;

        if (z < LOWER_LIMIT) {
            return 0;
        } else if (z > UPPER_LIMIT) {
            return 1;
        }

        float temp = LOWER_LIMIT;
        while (temp <= z) {
            ret += DURATION * fx(temp);

            temp += DURATION;
        }

        return ret;
    }

    /**
     * 求被积函数的函数值    (1/(2 * PI)^(0.5))e^(-t^2/2)
     *
     * @param x 变量x
     * @return 函数值
     */
    private static float fx(float x) {
        float ret = 0;

        double a = 1.0 / Math.sqrt(Math.PI * 2);

        a = a * Math.pow(Math.E, -0.5 * Math.pow(x, 2));

        ret = (float) a;

        return ret;
    }

    public static void main(String[] args){
       System.out.println(zValue(0.25f));
       System.out.println(confidence(0.25f));
    }

}
