package com.bytefinger.toutuo.biz.performance.domain.vo;
import com.bytefinger.common.security.annotation.BigDecimalSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndivualUserVO {
    @ApiModelProperty(value = "主键", hidden = false, required = false)
    private Long id;
    @ApiModelProperty(value = "记录Id", hidden = false, required = false)
    private Long recordId;
    @ApiModelProperty(value = "用户Id", hidden = false, required = false)
    private Long userId;
    @ApiModelProperty(value = "用户名称", hidden = false, required = false)
    private String realname;
    @ApiModelProperty(value = "用户头像", hidden = false, required = false)
    private String avatar;
    @ApiModelProperty(value = "部门Id", hidden = false, required = false)
    private Long deptId;
    @ApiModelProperty(value = "帐号状态（0在职 1离职）", hidden = false, required = false)
    private String status;
    @ApiModelProperty(value = "部门名称", hidden = false, required = false)
    private String deptName;
    @ApiModelProperty(value = "未发个佣", hidden = false, required = false)
    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal individualAmount;

}
