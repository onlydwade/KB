package com.bytefinger.toutuo.biz.projectextension.enums;

import java.util.Objects;

/**
 * 风险评估
 *
 * @author ycj
 * @date 2023/3/23
 **/
public enum ProjectModuleName {

    ProjectAssess("ProjectAssess", "项目评估"),
    ProjectCheck("ProjectCheck", "承接查验"),
    ProjectCorrelation("ProjectCorrelation", "拓后运营项目关联"),
    ProjectExpansionDocument("ProjectExpansionDocument", "拓后项目文件"),
    ProjectExtension("ProjectExtension", "拓后管理"),
    ProjectExtensionExit("ProjectExtensionExit", "拓后项目退场"),
    ProjectIntervene("ProjectIntervene", "拓后运营项目干预"),
    ProjectOperateIncome("ProjectOperateIncome", "经营管理"),
    ProjectOperation("ProjectOperation", "拓后运营管理"),
    ProjectRiskManagement("ProjectRiskManagement", "风险管理"),
    ProjectServeSatisfaction("ProjectServeSatisfaction", "服务满意度信息"),
    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectModuleName(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectModuleName getByCode(String code) {
        for (ProjectModuleName value : ProjectModuleName.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectModuleName isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
