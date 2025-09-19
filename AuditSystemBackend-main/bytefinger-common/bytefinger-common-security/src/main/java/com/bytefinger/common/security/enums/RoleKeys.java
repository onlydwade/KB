package com.bytefinger.common.security.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.List;

/**
 * @author pat
 * @date 2022/10/21 10:43
 **/
public enum RoleKeys {

    DEFAULT("default", 0),
    USER_IDS("userIds", 1,2,11),
    ATTRIBUTOR_USER_ID("attributorUserId", 1, 2),
    DEPT_ID("companyId", 1, 2),
    APPROVAL("id", 3),
    SHOW("companyId", 9),

    CREATE_USER_ID("createUserId", 1, 2),
    ;

    private String field;
    private Integer[] code;

    public String getField() {
        return this.field;
    }

    public Integer[] getCode() {
        return this.code;
    }

    RoleKeys(final String field, final Integer... code) {
        this.field = field;
        this.code = code;
    }

    public static boolean dataShow(List<Integer> roleKeys) {
        if (CollUtil.isNotEmpty(roleKeys)) {
            for (Integer roleKey : roleKeys) {
                if (ArrayUtil.contains(new Integer[]{1, 2, 3, 9}, roleKey)) {
                    return true;
                }
            }
        }
        return false;
    }

}
