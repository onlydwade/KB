package com.bytefinger.toutuo.biz.performance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("biz_budget_statistical_method_config")
@ApiModel(value = "考核指标统计方式配置", description = "考核指标统计方式配置")
public class BudgetStatisticalMethodConfig extends BaseEntity {

    @ApiModelProperty(value = "字段年", hidden = false, required = false)
    @TableField("field_year")
    private Integer fieldYear;

    @ApiModelProperty(value = "字段编码(HTZJE:合同总金额,HTNDJE:合同年度金额)", hidden = false, required = false)
    @TableField("field_key")
    private String fieldKey;

    @ApiModelProperty(value = "单一投标-增量扩展", hidden = false, required = false)
    @TableField("yes_incremental")
    private Boolean yesIncremental;

    @ApiModelProperty(value = "单一投标-存量保盘", hidden = false, required = false)
    @TableField("yes_keep")
    private Boolean yesKeep;

    @ApiModelProperty(value = "单一投标-存量新增", hidden = false, required = false)
    @TableField("yes_Add")
    private Boolean yesAdd;

    @ApiModelProperty(value = "非单一投标-增量扩展", hidden = false, required = false)
    @TableField("no_incremental")
    private Boolean noIncremental;

    @ApiModelProperty(value = "非单一投标-存量保盘", hidden = false, required = false)
    @TableField("no_keep")
    private Boolean noKeep;

    @ApiModelProperty(value = "非单一投标-存量新增", hidden = false, required = false)
    @TableField("no_Add")
    private Boolean noAdd;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "'创建时间'", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
