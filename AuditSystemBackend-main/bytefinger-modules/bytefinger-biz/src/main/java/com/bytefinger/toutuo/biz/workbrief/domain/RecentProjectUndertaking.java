package com.bytefinger.toutuo.biz.workbrief.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 近期项目承接情况
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_recent_project_undertaking")
@ApiModel(value = "近期项目承接情况", description = "近期项目承接情况")
public class RecentProjectUndertaking extends BaseEntity {

    @ApiModelProperty(value = "工作简报id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("brief_id")
    private Long briefId;

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField("project_name")
    private String projectName;

    @ApiModelProperty(value = "归属单位id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "归属单位名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "合同金额(万)")
    @TableField("contract_amount")
    private String contractAmount;

    @ApiModelProperty(value = "服务年限(年)")
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "合同年度金额(万)")
    @TableField("contract_annual_amount")
    private String contractAnnualAmount;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time",  fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "是否已删除")
    @TableField("deleted")
    private Integer deleted;

}

