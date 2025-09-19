package com.bytefinger.toutuo.api.biz.project.enums;

import java.util.Objects;

/**
 * @author pat
 * @date 2022/10/26 09:45
 **/
public enum OAApprovalModule {

    LI_XIANG_SHENG_PI("LI_XIANG_SHENG_PI", "立项审批（可行性评审）"),
    JIE_FA_REN_ZHU_TI("JIE_FA_REN_ZHU_TI", "借法人主体投标审批"),
    JIAO_NA_BAO_ZHENG_JIN("JIAO_NA_BAO_ZHENG_JIN", "缴纳保证金审批"),
    ;

    private final String code;
    private final String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    OAApprovalModule(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OAApprovalModule getByCode(String code) {
        for (OAApprovalModule value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

}
