package com.bytefinger.toutuo.biz.projectextension.enums;

import com.bytefinger.toutuo.biz.projectcompany.enums.ProjectCompanyStatus;

import java.util.Objects;


public enum ProjectProcessStatus {
    WEI_CHU_LI(0, "未处理"),
    SHENG_PI_ZHONG(1, "审批中"),
    YI_CHU_LI(2, "已处理"),
    BO_HUI(3, "处理被驳回"),
    ;

    private final Integer code;

    private String desc;

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectProcessStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectProcessStatus getByCode(Integer code) {
        for (ProjectProcessStatus value : ProjectProcessStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectProcessStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}