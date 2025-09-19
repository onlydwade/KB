package com.bytefinger.toutuo.common.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 通用关联表的新增修改
 *
 * @author pat
 * @date 2022/11/17 13:50
 **/
public enum BizUpdateType {

    /**
     * 参与人
     */
    USERS("ADD", "UPDATE"),

    /**
     * 竞争对手
     */
    COMPETITOR("ADD", "UPDATE"),

    /**
     * 团队
     */
    TEAM("ADD"),

    /**
     * 客户联系人
     */
    CUSTOMER_CONTACT("ADD", "UPDATE"),

    /**
     * 竞对联系人
     */
    COMPETITOR_CONTACT("ADD");

    private List<String> method;

    public boolean hasMethod(String method) {
        return this.method.contains(method);
    }

    BizUpdateType(final String... methods) {
        this.method = Arrays.asList(methods);
    }
}
