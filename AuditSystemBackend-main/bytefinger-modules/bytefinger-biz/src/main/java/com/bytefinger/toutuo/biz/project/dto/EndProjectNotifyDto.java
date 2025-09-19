package com.bytefinger.toutuo.biz.project.dto;

import com.bytefinger.common.security.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 项目丢盘监控-通告  实体
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@ApiModel(value = "项目丢盘监控-通告", description = "项目丢盘监控-通告")
public class EndProjectNotifyDto {

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "归属单位")
    @Excel(name = "归属单位")
    private String deptName;

    @ApiModelProperty(value = "项目名称")
    @Excel(name = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目状态")
    @Excel(name = "项目状态")
    private String serviceStatusName;

    @ApiModelProperty(value = "合同金额")
    @Excel(name = "合同金额(元)")
    private String contractAmount;

    @ApiModelProperty(value = "服务期限")
    @Excel(name = "服务期限(月)")
    private Integer proposedServicePeriod;

    @ApiModelProperty(value = "处理时间")
    @Excel(name = "处理时间")
    private String updateTime;

    @ApiModelProperty(value = "处理人")
    @Excel(name = "处理人")
    private String updateUserName;

}

