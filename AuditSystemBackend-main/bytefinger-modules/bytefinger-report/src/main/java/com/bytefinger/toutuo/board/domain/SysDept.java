package com.bytefinger.toutuo.board.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysDept对象", description = "部门表")
public class SysDept {

    @ApiModelProperty(value = "部门id", hidden = false, required = false)
    private Long deptId;

    @ApiModelProperty(value = "父部门id", hidden = false, required = false)
    private Long parentId;

    @ApiModelProperty(value = "祖级列表", hidden = false, required = false)
    private String ancestors;

    @ApiModelProperty(value = "部门名称", hidden = false, required = false)
    private String deptName;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    private Integer orderNum;

    @ApiModelProperty(value = "负责人", hidden = false, required = false)
    private String leader;

    @ApiModelProperty(value = "联系电话", hidden = false, required = false)
    private String phone;

    @ApiModelProperty(value = "邮箱", hidden = false, required = false)
    private String email;

    @ApiModelProperty(value = "部门状态（0正常 1停用）", hidden = false, required = false)
    private String status;

    @ApiModelProperty(value = "层级", hidden = false, required = false)
    private Integer level;

    @ApiModelProperty(value = "人力资源组织机构", hidden = false, required = false)
    private String hrOrg;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", hidden = false, required = false)
    private String delFlag;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    private Date updateTime;

    @ApiModelProperty(value = "类型", hidden = false, required = false)
    private String deptType;


}
