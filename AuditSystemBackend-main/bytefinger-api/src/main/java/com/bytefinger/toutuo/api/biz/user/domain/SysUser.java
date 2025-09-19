package com.bytefinger.toutuo.api.biz.user.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.xss.Xss;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户信息表")
public class SysUser {

    @ApiModelProperty(value = "用户ID", hidden = false, required = false)
    @JSONField(name = "userId", serialize = true)
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "部门ID", hidden = false, required = false)
    @JSONField(name = "deptId", serialize = true)
    @TableField("dept_id")
    private Long deptId;

    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "用户账号", hidden = false, required = false)
    @JSONField(name = "userName", serialize = true)
    @TableField("user_name")
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    @ApiModelProperty(value = "真实姓名", hidden = false, required = false)
    @JSONField(name = "realname", serialize = true)
    @TableField("realname")
    @Xss(message = "真实姓名不能包含脚本字符")
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 0, max = 30, message = "真实姓名长度不能超过30个字符")
    private String realname;

    @ApiModelProperty(value = "用户昵称", hidden = false, required = false)
    @JSONField(name = "nickName", serialize = true)
    @TableField("nick_name")
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    @ApiModelProperty(value = "用户类型（00系统用户）", hidden = false, required = false)
    @JSONField(name = "userType", serialize = true)
    @TableField("user_type")
    private String userType;

    @ApiModelProperty(value = "用户邮箱", hidden = false, required = false)
    @JSONField(name = "email", serialize = true)
    @TableField("email")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @ApiModelProperty(value = "手机号码", hidden = false, required = false)
    @JSONField(name = "phonenumber", serialize = true)
    @TableField("phonenumber")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）", hidden = false, required = false)
    @JSONField(name = "sex", serialize = true)
    @TableField("sex")
    private String sex;

    @ApiModelProperty(value = "头像地址", hidden = false, required = false)
    @JSONField(name = "avatar", serialize = true)
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "密码", hidden = false, required = false)
    @JSONField(name = "password", serialize = false)
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", hidden = false, required = false)
    @JSONField(name = "delFlag", serialize = false)
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty(value = "最后登录IP", hidden = false, required = false)
    @JSONField(name = "loginIp", serialize = true)
    @TableField("login_ip")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间", hidden = false, required = false)
    @JSONField(name = "loginDate", serialize = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("login_date")
    private Date loginDate;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "ladp账号id", hidden = false, required = false)
    @TableField("ladp_user_id")
    private Long ladpUserId;

    @ApiModelProperty(value = "主账号id", hidden = false, required = false)
    @TableField("main_user_id")
    private String mainUserId;

    @ApiModelProperty(value = "主账号id", hidden = false, required = false)
    @TableField("main_dept_id")
    private String mainDeptId;

    @ApiModelProperty(value = "主部门名称", hidden = false, required = false)
    @TableField(exist = false)
    private String mainDeptName;

    @TableField(exist = false)
    private String ladpDeptName;

    @TableField(exist = false)
    private Long postId;

    @ApiModelProperty(value = "openId", hidden = false, required = false)
    @JSONField(name = "openId", serialize = true)
    @TableField("open_id")
    private String openId;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    private Long companyId;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    private String companyName;

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId.longValue();
    }

}