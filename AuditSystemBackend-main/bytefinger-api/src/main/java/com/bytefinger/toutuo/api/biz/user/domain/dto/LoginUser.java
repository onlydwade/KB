package com.bytefinger.toutuo.api.biz.user.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户信息
 *
 * @author patrick
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 部门id
     */
    private Long deptId = 0L;

    /**
     * 岗位id
     */
    private Long postId;

    /**
     * 岗位code
     */
    private String postCode;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 当前用户的下属用户的id
     */
    private Set<Long> subDeptIds = Sets.newHashSet();
}
