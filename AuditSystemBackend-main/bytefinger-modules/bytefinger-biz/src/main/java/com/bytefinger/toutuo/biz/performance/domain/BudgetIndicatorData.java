package com.bytefinger.toutuo.biz.performance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("biz_budget_indicator_data")
@ApiModel(value = "年度目标支出预算数据", description = "年度目标支出预算数据")
public class BudgetIndicatorData extends BaseEntity {

    @ApiModelProperty(value = "字段年", hidden = false, required = false)
    @TableField("field_year")
    private Integer fieldYear;


    @ApiModelProperty(value = "公司id", hidden = false, required = false)
    @TableField("company_id")
    private Long companyId;

    @ApiModelProperty(value = "指标项编码（合同总金额）", hidden = false, required = false)
    @TableField("field_key")
    private String fieldKey;

    @ApiModelProperty(value = "指标项名称（合同总金额）", hidden = false, required = false)
    @TableField("field_name")
    private String fieldName;

    @ApiModelProperty(value = "指标项排序", hidden = false, required = false)
    @TableField("field_sort")
    private Integer fieldSort;

    @ApiModelProperty(value = "锁定 0-未锁定 1-锁定", hidden = false, required = false)
    @TableField("locked")
    private Integer locked;

    @ApiModelProperty(value = "指标编码（门槛值）", hidden = false, required = false)
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "指标编码排序", hidden = false, required = false)
    @TableField("code_sort")
    private Integer codeSort;

    @ApiModelProperty(value = "金额", hidden = false, required = false)
    @TableField(value = "amount",updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal amount;

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
