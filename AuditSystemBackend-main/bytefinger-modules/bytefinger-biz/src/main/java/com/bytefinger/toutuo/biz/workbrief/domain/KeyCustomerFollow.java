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
 * 重点客户跟进情况
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_project_key_customer_follow")
@ApiModel(value = "重点客户跟进情况", description = "重点客户跟进情况")
public class KeyCustomerFollow extends BaseEntity {

    @ApiModelProperty(value = "工作简报id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("brief_id")
    private Long briefId;

    @ApiModelProperty(value = "客户id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("customer_id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    @TableField("customer_name")
    private String customerName;

    @ApiModelProperty(value = "任务摘要")
    @TableField("summary")
    private String summary;

    @ApiModelProperty(value = "客户类型")
    @TableField("project_type")
    private Integer projectType;

    @ApiModelProperty(value = "专班建立")
    @TableField("teams_build")
    private String teamsBuild;

    @ApiModelProperty(value = "跟进状态")
    @TableField("follow_status")
    private String followStatus;

    @ApiModelProperty(value = "负责人")
    @TableField("visit_user_name")
    private String visitUserName;

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

    @ApiModelProperty(value = "项目类型名称")
    @TableField(exist = false)
    private String projectTypeName;

}

