package com.bytefinger.toutuo.biz.workbrief.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 重点客户跟进情况
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_group_info")
@ApiModel(value = "分组表", description = "分组表")
public class GroupInfo extends BaseEntity {

    @ApiModelProperty(value = "分组名称")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "客户名称")
    @TableField("record_type")
    private String recordType;

    @ApiModelProperty(value = "客户名称")
    @TableField("record_id")
    private String recordId;

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


    @ApiModelProperty(value = "工作简报用户集合", hidden = false, required = false)
    @Schema(description = "工作简报用户集合", name = "checkState")
    @TableField(exist = false)
    private List<GroupUser> groupUserList;

    @ApiModelProperty(value = "工作简报用户集合", hidden = false, required = false)
    @Schema(description = "工作简报用户集合", name = "checkState")
    @TableField(exist = false)
    private List<Long> groupUserIdList;

    @TableField(exist = false)
    public String userNames;
}

