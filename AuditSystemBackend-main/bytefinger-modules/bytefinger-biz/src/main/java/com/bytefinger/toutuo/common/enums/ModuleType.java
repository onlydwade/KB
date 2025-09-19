package com.bytefinger.toutuo.common.enums;

import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.toutuo.biz.customer.domain.Customer;
import com.bytefinger.toutuo.biz.oa.domain.OaApproval;
import com.bytefinger.toutuo.biz.project.domain.Project;

/**
 * @author pat
 * @date 2022/10/21 10:43
 **/
public enum ModuleType {

    AUDIT("Audit", "审计"),
    PROJECT("Project", "项目"),
    CUSTOMER("Customer", "客户"),
    COMPANY("Company", "子公司"),
    PROJECT_EXTEXSION("ProjectExtexsion", "拓后项目"),
    COMPANY_INFO("COMPANY_INFO", "子公司信息"),
    ;

    private String code;
    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ModuleType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }


}
