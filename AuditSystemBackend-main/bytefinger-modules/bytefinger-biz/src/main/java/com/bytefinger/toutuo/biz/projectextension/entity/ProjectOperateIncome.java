package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.domain.TimeBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 经营管理-项目收支
 *
 * @author ycj
 * @email
 * @date 2023-03-17 14:23:41
 */
@Data
@TableName("biz_project_operate_income")
public class ProjectOperateIncome extends BizProjectBaseEntity {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;
    /**
     * 业态
     */
    @ApiModelProperty(value = "业态", hidden = false, required = false)
    @HistoryFieldLog("业态")
    @Excel(name = "业态")
    @TableField("business_type")
    private String businessType;
    /**
     * 季度阶段(1.第一季2.第二季,3.第三季,4.第四季)
     */
    @ApiModelProperty(value = "季度阶段(1.第一季2.第二季,3.第三季,4.第四季)", hidden = false, required = false)
    @HistoryFieldLog("季度阶段(1.第一季2.第二季,3.第三季,4.第四季)")
    @Excel(name = "季度阶段")
    @TableField("quarter_stage")
    private Integer quarterStage;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份", hidden = false, required = false)
    @HistoryFieldLog("年份")
    @Excel(name = "年份")
    @TableField("operate_year")
    private String operateYear;
    /**
     * 收缴率%
     */
    @ApiModelProperty(value = "收缴率", hidden = false, required = false)
    @HistoryFieldLog("收缴率")
    @Excel(name = "收缴率")
    @TableField("takeover_rate")
    private BigDecimal takeoverRate;
    /**
     * 结算率
     */
    @ApiModelProperty(value = "结算率", hidden = false, required = false)
    @HistoryFieldLog("结算率")
    @Excel(name = "结算率")
    @TableField("close_rate")
    private BigDecimal closeRate;
    /**
     * 主营收入(元)
     */
    @ApiModelProperty(value = "主营收入", hidden = false, required = false)
    @HistoryFieldLog("主营收入")
    @Excel(name = "主营收入")
    @TableField("business_income")
    private BigDecimal businessIncome;
    /**
     * 主营外收入(元)
     */
    @ApiModelProperty(value = "主营外收入", hidden = false, required = false)
    @HistoryFieldLog("主营外收入")
    @Excel(name = "主营外收入")
    @TableField("outer_business_income")
    private BigDecimal outerBusinessIncome;
    /**
     * 实际结算金额（元）
     */
    @ApiModelProperty(value = "实际结算金额", hidden = false, required = false)
    @HistoryFieldLog("实际结算金额")
    @Excel(name = "实际结算金额")
    @TableField("practical_close_money")
    private BigDecimal practicalCloseMoney;
    /**
     * 成本(元)
     */
    @ApiModelProperty(value = "成本", hidden = false, required = false)
    @HistoryFieldLog("成本")
    @Excel(name = "成本")
    @TableField("cost")
    private BigDecimal cost;
    /**
     * 利润(元)
     */
    @ApiModelProperty(value = "利润", hidden = false, required = false)
    @HistoryFieldLog("利润")
    @Excel(name = "利润")
    @TableField("profit")
    private BigDecimal profit;
    /**
     * 是否删除(0否,2是)
     */
    @ApiModelProperty(value = "是否删除", hidden = false, required = false)
    @HistoryFieldLog("是否删除")
    @Excel(name = "是否删除")
    @TableField("deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @HistoryFieldLog("创建人")
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Excel(name = "创建人", dataType = Excel.DataType.USER)
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @HistoryFieldLog("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @HistoryFieldLog(value ="修改人",joinField = "updateUser")
    @Excel(name = "修改人")
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    @HistoryFieldLog("修改时间")
    @Excel(name = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 附件id
     */
    @ApiModelProperty(value = "附件id", hidden = false, required = false)
    @HistoryFieldLog("附件id")
    @Excel(name = "附件id")
    @TableField("rest_file_id")
    private String restFileId;

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;


}
