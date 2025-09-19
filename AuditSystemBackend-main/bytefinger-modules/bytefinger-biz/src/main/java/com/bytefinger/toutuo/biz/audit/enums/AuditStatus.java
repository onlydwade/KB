package com.bytefinger.toutuo.biz.audit.enums;

import java.util.Objects;

/**
 * 状态
 *
 * @author pat
 * @date 2022/10/09 18:30
 **/
public enum AuditStatus {


    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    COMPLETED("COMPLETED", "Completed"),
    CANCELLED("CANCELLED", "Cancelled"),

    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    AuditStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditStatus getByCode(String code) {
        for (AuditStatus value : AuditStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static AuditStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
