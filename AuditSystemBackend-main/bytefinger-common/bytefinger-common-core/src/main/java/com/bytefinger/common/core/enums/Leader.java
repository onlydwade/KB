package com.bytefinger.common.core.enums;

/**
 * 是否是负责人
 *
 * @author pat
 * @date 2022/10/09 17:33
 **/
public enum Leader {

    YES(1, "是"), NO(0, "否");

    private final Integer code;
    private final String info;

    Leader(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
