package com.bytefinger.toutuo.gen;

/**
 * @author pat
 * @date 2022/10/21 14:23
 **/
public class KeywordsGen {

    public static void main(String[] args) {
        one(7, "品牌、背景取胜、服务品质取胜、最低报价取胜、关系维护取胜");
    }

    static void one(int type, String name) {
        String[] split = name.split("、");
        int index = 1;
        for (String s : split) {
            String sql = "INSERT INTO `biz_keywords` VALUES (null, %d, '%s', %d);";
            String format = String.format(sql, type, s, index++);
            System.out.println(format);
        }
        System.out.println();
    }


}
