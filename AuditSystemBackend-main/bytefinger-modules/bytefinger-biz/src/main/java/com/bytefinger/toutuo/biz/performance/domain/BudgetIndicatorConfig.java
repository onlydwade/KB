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
@TableName("biz_budget_indicator_config")
@ApiModel(value = "年度目标支出预算-指标设置", description = "年度目标支出预算-指标设置")
public class BudgetIndicatorConfig extends BaseEntity {

    @ApiModelProperty(value = "字段年", hidden = false, required = false)
    @TableField("field_year")
    private Integer fieldYear;

    @ApiModelProperty(value = "指标编码", hidden = false, required = false)
    @TableField("code")
    private String code;


    @ApiModelProperty(value = "名称", hidden = false, required = false)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "序号", hidden = false, required = false)
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "是否为目标值", hidden = false, required = false)
    @TableField("is_target")
    private Integer isTarget;

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
