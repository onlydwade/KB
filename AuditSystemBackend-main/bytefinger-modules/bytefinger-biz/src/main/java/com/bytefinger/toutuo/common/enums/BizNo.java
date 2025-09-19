package com.bytefinger.toutuo.common.enums;

import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectBacklog;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;

/**
 * @author pat
 * @date 2022/10/21 10:43
 **/
public enum BizNo {

    PROJECT("XM", "项目", Project.class),
    CUSTOMER("KH", "客户", Customer.class),
    PROJECT_COMPANY("GS", "公司", ProjectCompany.class),

    OAAPPROVAL("OASP", "客户", OaApproval.class),

    OA_BORROWBODY_APPROVAL("JZT", "OA借主体审批", null),
    OA_ASSESS_APPROVAL("LXPS", "OA立项评审编号", null),
    PROJECT_PROCESS("LX", "立项编号", null),
    RECORD("BA", "备案编号", null),
    PROJECT_BACKLOG("XM", "项目", ProjectBacklog.class),

    ;

    private String code;
    private String desc;
    private Class<? extends BaseEntity> clazz;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Class<? extends BaseEntity> getClazz() {
        return this.clazz;
    }

    BizNo(final String code, final String desc, final Class<? extends BaseEntity> clazz) {
        this.code = code;
        this.desc = desc;
        this.clazz = clazz;
    }

    public static BizNo findValue(String className) {
        for (BizNo value : values()) {
            if (null != value.clazz && value.clazz.getSimpleName().equalsIgnoreCase(className)) {
                return value;
            }
        }

        return null;
    }

}
