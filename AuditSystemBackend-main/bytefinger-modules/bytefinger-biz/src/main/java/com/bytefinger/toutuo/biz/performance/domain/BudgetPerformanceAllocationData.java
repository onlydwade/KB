package com.bytefinger.toutuo.biz.performance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("biz_budget_performance_allocation_data")
@ApiModel(value = "业绩分配数据", description = "业绩分配数据")
public class BudgetPerformanceAllocationData extends BaseEntity {

    @ApiModelProperty(value = "字段年", hidden = false, required = false)
    @TableField("field_year")
    private Integer fieldYear;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty(value = "公司id", hidden = false, required = false)
    @TableField("company_id")
    private Long companyId;


    @ApiModelProperty(value = "项目类型", hidden = false, required = false)
    @TableField("project_type")
    private String projectType;

    @ApiModelProperty(value = "合同金额分配比率", hidden = false, required = false)
    @TableField("contract_amount_rate")
    private BigDecimal contractAmountRate;

    @ApiModelProperty(value = "主导拓展分配比率", hidden = false, required = false)
    @TableField("leading_expansion_rate")
    private BigDecimal leadingExpansionRate;

    @ApiModelProperty(value = "增量拓展数据（金额）", hidden = false, required = false)
    @TableField("incremental_amount")
    private BigDecimal incrementalAmount;

    @ApiModelProperty(value = "存量保盘数据（金额）", hidden = false, required = false)
    @TableField("keep_amount")
    private BigDecimal keepAmount;

    @ApiModelProperty(value = "存量新增数据（金额）", hidden = false, required = false)
    @TableField("add_amount")
    private BigDecimal addAmount;

    @ApiModelProperty(value = "增量拓展数据（年度金额）", hidden = false, required = false)
    @TableField("incremental_amount_year")
    private BigDecimal incrementalAmountYear;

    @ApiModelProperty(value = "存量保盘数据（年度金额）", hidden = false, required = false)
    @TableField("keep_amount_year")
    private BigDecimal keepAmountYear;

    @ApiModelProperty(value = "存量新增数据（年度金额）", hidden = false, required = false)
    @TableField("add_amount_year")
    private BigDecimal addAmountYear;


    @ApiModelProperty(value = "当年转化收入（年度金额）", hidden = false, required = false)
    @TableField("currentConvertIncome")
    private BigDecimal currentConvertIncome;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "'创建时间'", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否已删除
     */
    @ApiModelProperty(value = "是否已删除", hidden = false, required = false)
    @TableField("deleted")
    @HistoryFieldLog("是否已删除")
    private Integer deleted;

    @ApiModelProperty(value = "更新人", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
