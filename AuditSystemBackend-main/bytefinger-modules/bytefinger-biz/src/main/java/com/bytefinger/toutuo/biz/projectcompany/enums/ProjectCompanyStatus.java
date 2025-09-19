package com.bytefinger.toutuo.biz.projectcompany.enums;

import java.util.Objects;


public enum ProjectCompanyStatus {

    ZHENG_CHANG("ZHENG_CHANG", "正常"),
    YI_TUI_CHU("YI_TUI_CHU", "已退出"),
    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectCompanyStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectCompanyStatus getByCode(String code) {
        for (ProjectCompanyStatus value : ProjectCompanyStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectCompanyStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
