package com.bytefinger.common.core.enums;

/**
 * @author pat
 * @date 2022/10/12 12:54
 **/
public enum Deleted {


    YES(2), NO(0);

    private Integer code;

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return String.valueOf(this.code);
    }

    public static final String Y = "2";

    public static final String N = "0";

    Deleted(Integer code) {
        this.code = code;
    }

}
