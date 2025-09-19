package com.bytefinger.toutuo.biz.workbrief.vo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 重点客户 实体
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@ApiModel(value = "重点客户-实体", description = "重点客户-实体")
public class KeyCustomerInfoVo implements Serializable {

    @ApiModelProperty(value = "跟进记录id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "工作摘要")
    private String workSummary;

    @ApiModelProperty(value = "跟进状态")
    private String followStatus;

    @ApiModelProperty(value = "负责人（拜访人）")
    private String visitUserName;

    @ApiModelProperty(value = "专班建立")
    private String teamEstablish;

}

