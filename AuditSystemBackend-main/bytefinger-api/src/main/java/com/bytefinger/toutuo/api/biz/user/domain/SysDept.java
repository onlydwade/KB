package com.bytefinger.toutuo.api.biz.user.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
@ApiModel(value = "SysDept对象", description = "部门表")
public class SysDept {

    @ApiModelProperty(value = "部门id", hidden = false, required = false)
    @JSONField(name = "deptId", serialize = true)
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    @ApiModelProperty(value = "父部门id", hidden = false, required = false)
    @JSONField(name = "parentId", serialize = true)
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "祖级列表", hidden = false, required = false)
    @JSONField(name = "ancestors", serialize = true)
    @TableField("ancestors")
    private String ancestors;

    @ApiModelProperty(value = "部门名称", hidden = false, required = false)
    @JSONField(name = "deptName", serialize = true)
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    @JSONField(name = "orderNum", serialize = true)
    @TableField("order_num")
    private Integer orderNum;

    @ApiModelProperty(value = "负责人", hidden = false, required = false)
    @JSONField(name = "leader", serialize = true)
    @TableField("leader")
    private String leader;

    @ApiModelProperty(value = "联系电话", hidden = false, required = false)
    @JSONField(name = "phone", serialize = true)
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱", hidden = false, required = false)
    @JSONField(name = "email", serialize = true)
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "部门状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "层级", hidden = false, required = false)
    @JSONField(name = "level", serialize = true)
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "人力资源组织机构", hidden = false, required = false)
    @JSONField(name = "hrOrg", serialize = true)
    @TableField("hr_org")
    private String hrOrg;


    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", hidden = false, required = false)
    @JSONField(name = "delFlag", serialize = true)
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "类型", hidden = false, required = false)
    @TableField("dept_type")
    @JSONField(name = "deptType", serialize = true)
    private String deptType;

    /**
     * 父部门名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<SysDept>();

    /**
     * 主数据部门id集合
     */
    @TableField(exist = false)
    private List<String> mainDeptIds;


    @TableField(exist = false)
    private List<IntMainDept> mainDepts;


}
