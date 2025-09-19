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
import java.util.List;

/**
 * <p>
 * 数据看板推送对象
 * </p>
 *
 * @author tang
 * @since 2024-03-16
 */
@Data
@ApiModel(value = "数据看板推送对象", description = "数据看板推送对象")
public class SendDataBoard  {

    private List<String> pushUserList;

    private Integer year;

    private Long deptId;

}

