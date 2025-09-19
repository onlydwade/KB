package com.bytefinger.toutuo.biz.project.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBriefPushUser;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 通告撤销日志表
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_project_notify_log")
@ApiModel(value = "通告撤销日志", description = "通告撤销日志")
public class ProjectNotifyLog extends BaseEntity {

    @ApiModelProperty(value = "归属单位id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "归属单位名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    @TableField("project_name")
    private String projectName;

    @ApiModelProperty(value = "通告类型")
    @TableField("notify_type")
    private String notifyType;

    @ApiModelProperty(value = "通告类型名称")
    @TableField(exist = false)
    private String notifyTypeName;

    @ApiModelProperty(value = "项目负责人id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("attributor_user_id")
    private Long attributorUserId;

    @ApiModelProperty(value = "项目负责人名称")
    @TableField("attributor_user_name")
    private String attributorUserName;

    @ApiModelProperty(value = "逾期天数")
    @TableField("overdue_time")
    private Integer overdueTime;

    @ApiModelProperty(value = "发布时间")
    @TableField("push_time")
    private String pushTime;

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
    @Excel(name = "简报发送时间", dateFormat = "yyyy-MM-dd HH:mm:ss",sort = 4)
    private Date updateTime;

    @ApiModelProperty(value = "是否已删除")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty(value = "最后更新人名称")
    @TableField(exist = false)
    private String updateUserName;

}

