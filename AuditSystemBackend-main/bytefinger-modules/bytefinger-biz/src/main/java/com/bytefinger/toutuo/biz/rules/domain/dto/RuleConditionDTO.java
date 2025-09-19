package com.bytefinger.toutuo.biz.rules.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 规则条件
 *
 * @author pat
 * @date 2022/11/01 09:34
 **/
@Data
@ApiModel(value = "规则条件", description = "规则条件")
public class RuleConditionDTO {

    @ApiModelProperty("字段类型")
    @NotNull(message = "不能为空")
    private String fieldType;

    @ApiModelProperty("字段名称")
    @NotNull(message = "不能为空")
    private String fieldName;

    @ApiModelProperty("条件，枚举")
    @NotNull(message = "条件")
    private String condition;

    @ApiModelProperty("其他字段类型，枚举")
    @NotNull(message = "不能为空")
    private String fieldTypeOther;

    @ApiModelProperty("条件值，枚举")
    @NotNull(message = "不能为空")
    private String conditionValue;

    @ApiModelProperty("时间单位，枚举")
    private String unit;


}
