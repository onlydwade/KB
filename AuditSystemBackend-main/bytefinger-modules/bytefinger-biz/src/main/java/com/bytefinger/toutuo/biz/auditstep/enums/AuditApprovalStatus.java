package com.bytefinger.toutuo.biz.auditstep.enums;

import java.util.Objects;

/**
 * 状态
 *
 * @author pat
 * @date 2022/10/09 18:30
 **/
public enum AuditApprovalStatus {

    WEI_FA_QI_SHEN_PI(0, "未发起审批"),
    SHEN_PI_ZHONG(1, "审批中"),
    SHEN_PI_TONG_GUO(2, "审批通过"),
    SHEN_PI_BO_HUI(3, "审批驳回"),
    XIAN_XIA_SHEN_PI_TONG_GUO(8, "线下审批通过"),
    WU_XU_SHEN_PI(9, "无需审批 "),
    ;

    private final Integer code;

    private String desc;

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    AuditApprovalStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditApprovalStatus getByCode(int code) {
        for (AuditApprovalStatus value : AuditApprovalStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static AuditApprovalStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
