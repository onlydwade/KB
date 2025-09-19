package com.bytefinger.toutuo.biz.performance.domain.dto;

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
public class ProjectDaoQiDto {

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "状态")
    private String serviceStatus;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    private String bidedResult;
}

