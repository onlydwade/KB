package com.bytefinger.toutuo.api.biz.user.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@ApiModel(value = "部门岗位", description = "VIEW")
public class SysUserDeptPostInfoDTO {

    @ApiModelProperty(value = "部门id", hidden = false, required = false)
    @JSONField(name = "deptId", serialize = true)
    private Long deptId;

    @ApiModelProperty(value = "父部门id", hidden = false, required = false)
    @JSONField(name = "parentId", serialize = true)
    private Long parentId;

    @ApiModelProperty(value = "父部门名称", hidden = false, required = false)
    @JSONField(name = "parentName", serialize = true)
    private String parentName;

    @ApiModelProperty(value = "祖级列表", hidden = false, required = false)
    @JSONField(name = "ancestors", serialize = true)
    private String ancestors;

    @ApiModelProperty(value = "部门名称", hidden = false, required = false)
    @JSONField(name = "deptName", serialize = true)
    private String deptName;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    @JSONField(name = "orderNum", serialize = true)
    private Integer orderNum;

    @ApiModelProperty(value = "层级", hidden = false, required = false)
    @JSONField(name = "level", serialize = true)
    private Integer level;

    @ApiModelProperty(value = "部门状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "deptStatus", serialize = true)
    private String deptStatus;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", hidden = false, required = false)
    @JSONField(name = "delFlag", serialize = true)
    private String delFlag;

    @ApiModelProperty(value = "岗位ID", hidden = false, required = false)
    @JSONField(name = "postId", serialize = true)
    private Long postId;

    @ApiModelProperty(value = "岗位编码", hidden = false, required = false)
    @JSONField(name = "postCode", serialize = true)
    private String postCode;

    @ApiModelProperty(value = "岗位名称", hidden = false, required = false)
    @JSONField(name = "postName", serialize = true)
    private String postName;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    @JSONField(name = "postSort", serialize = true)
    private Integer postSort;

    @ApiModelProperty(value = "状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "postStatus", serialize = true)
    private String postStatus;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    private String remark;

    @ApiModelProperty(value = "用户id", hidden = false, required = false)
    @JSONField(name = "userId", serialize = true)
    private Long userId;

    @ApiModelProperty(value = "用户名称", hidden = false, required = false)
    @JSONField(name = "realname", serialize = true)
    private String realname;

    @ApiModelProperty(value = "最后一次切换岗位时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    private Date updateTime;

}