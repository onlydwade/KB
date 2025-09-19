package com.bytefinger.toutuo.biz.interfacelog.enums;

import java.util.Objects;

/**
 * 状态
 *
 * @author chengwei
 * @date 2023/12/12 18:30
 **/
public enum InterfaceStatusEnums {

    Failure (0, "失败"),
    Success(1, "成功"),
    Error(2, "异常"),
    //中间状态：接口调用进行中，不允许再次调用操作
    Doing(3, "进行中"),
    ;

    private final Integer code;

    private String desc;

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    InterfaceStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static InterfaceStatusEnums getByCode(Integer code) {
        for (InterfaceStatusEnums value : InterfaceStatusEnums.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static InterfaceStatusEnums isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
