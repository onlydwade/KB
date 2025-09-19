package com.bytefinger.toutuo.biz.workbrief.enums;

/**
 * 推送状态
 *
 * @author lin
 * @date 2023/01/03
 **/
public enum PushStatusEnum {

    NOT_PUSHED(1, "未推送"),
    PUSHING(2, "推送中"),
    PUSH_SUCCESSFUL(3, "推送成功")
    ;

    private Integer code;

    private String name;

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    PushStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
