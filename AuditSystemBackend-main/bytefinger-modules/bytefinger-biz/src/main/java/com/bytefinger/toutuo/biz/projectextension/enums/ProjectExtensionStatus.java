package com.bytefinger.toutuo.biz.projectextension.enums;

import com.bytefinger.toutuo.biz.projectcompany.enums.ProjectCompanyStatus;

import java.util.Objects;


public enum ProjectExtensionStatus {

    WEI_XU_QIAN(0, "未续签"),
    XU_QIAN(1, "续签"),
    CHONG_XIN_TOU_BIAO(2, "重新投标"),
    TUI_CHANG(3, "退场"),
    ;

    private final Integer code;

    private String desc;

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectExtensionStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectExtensionStatus getByCode(Integer code) {
        for (ProjectExtensionStatus value : ProjectExtensionStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectExtensionStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}