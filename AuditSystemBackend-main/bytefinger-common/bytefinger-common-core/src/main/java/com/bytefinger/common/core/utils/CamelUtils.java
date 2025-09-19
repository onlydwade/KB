package com.bytefinger.common.core.utils;

import com.google.common.base.CaseFormat;

/**
 * @author pat
 * @date 2022/10/06 11:46
 **/
public class CamelUtils {

    /**
     * 驼峰转下划线  testData => test_data 或 TestData => test_data
     *
     * @param s
     * @return
     */
    public static String camelToUndeline(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    /**
     * 下划线转驼峰 test_data => testData
     *
     * @param s
     * @return
     */
    public static String underlineToCamel(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }
}
