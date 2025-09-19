package com.bytefinger.toutuo.biz.projectextension.enums;

import java.util.Objects;

/**
 * 风险评估
 *
 * @author ycj
 * @date 2023/3/23
 **/
public enum ProjectAssessLabel {

    law(0, "法律风险"),
    hidden(1, "隐患风险"),
    contract(2, "合同风险")
    ;

    private final Integer code;

    private String desc;

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectAssessLabel(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectAssessLabel getByCode(int code) {
        for (ProjectAssessLabel value : ProjectAssessLabel.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectAssessLabel isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
