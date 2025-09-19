package com.bytefinger.toutuo.biz.project.dto;

import com.bytefinger.common.security.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 项目通告  实体
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@ApiModel(value = "项目通告", description = "项目通告")
public class ProjectNotifyDto {

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    @Excel(name = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "通告类型（枚举值）")
    private String notifyType;

    @ApiModelProperty(value = "通告类型（名称）")
    @Excel(name = "通告类型")
    private String notifyTypeName;

    @ApiModelProperty(value = "归属单位id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    @ApiModelProperty(value = "归属单位")
    @Excel(name = "归属单位")
    private String deptName;

    @ApiModelProperty(value = "项目负责人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attributorUserId;

    @ApiModelProperty(value = "项目负责人")
    @Excel(name = "项目负责人")
    private String attributorUserName;

    @ApiModelProperty(value = "逾期天数")
    @Excel(name = "逾期天数")
    private Integer overdueTime;

    @ApiModelProperty(value = "发布时间")
    @Excel(name = "发布时间")
    private String pushTime;

    @ApiModelProperty(value = "立项时间")
    @Excel(name = "立项时间")
    private  String lxTime;
}

