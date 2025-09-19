package com.bytefinger.common.core.enums;

import java.util.Objects;

/**
 * 是否状态
 *
 * @author patrick
 */
public enum ShiFou {
    SHI("SHI", "是"), FOU("FOU", "否");

    private final String code;
    private final String info;

    ShiFou(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ShiFou getByCode(String code) {
        for (ShiFou value : ShiFou.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
