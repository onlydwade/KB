package com.bytefinger.toutuo.api.biz.user.domain.dto;

import lombok.Data;

/**
 * 权限审核人
 *
 * @author pat
 * @date 2022/11/01 12:49
 **/
@Data
public class RoleDTO {

    /**
     * 当前数据所属部门
     */
    private Long dataDeptId;

    /**
     * 当前登录人所属部门
     */
    private Long currDeptId;

    /**
     * 权限数据标识
     */
    private String perms;

    public RoleDTO() {
    }

    public RoleDTO(final Long currDeptId, final String perms) {
        this.currDeptId = currDeptId;
        this.perms = perms;
    }

    public RoleDTO(final Long dataDeptId, final Long currDeptId, final String perms) {
        this.dataDeptId = dataDeptId;
        this.currDeptId = currDeptId;
        this.perms = perms;
    }
}
