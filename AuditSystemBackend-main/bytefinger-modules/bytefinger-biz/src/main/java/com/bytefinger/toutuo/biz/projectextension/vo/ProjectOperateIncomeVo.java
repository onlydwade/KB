package com.bytefinger.toutuo.biz.projectextension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 经营管理-项目收支
 *
 * @author ycj
 * @email
 * @date 2023-03-17 14:23:41
 */
@Data
@ApiModel(value = "经营管理-项目收支")
public class ProjectOperateIncomeVo {

    /**
     *
     */
    @ApiModelProperty(value = "", name = "id")
    private Long id;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", name = "projectId")
    private Long projectId;
    /**
     * 业态
     */
    @ApiModelProperty(value = "业态", name = "businessType")
    private String businessType;
    /**
     * 季度阶段(1.第一季2.第二季,3.第三季,4.第四季)
     */
    @ApiModelProperty(value = "季度阶段(1.第一季2.第二季,3.第三季,4.第四季)", name = "quarterStage")
    private Integer quarterStage;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份", name = "operateYear")
    private String operateYear;
    /**
     * 收缴率%
     */
    @ApiModelProperty(value = "收缴率%", name = "takeoverRate")
    private BigDecimal takeoverRate;
    /**
     * 结算率
     */
    @ApiModelProperty(value = "结算率", name = "closeRate")
    private BigDecimal closeRate;
    /**
     * 主营收入(元)
     */
    @ApiModelProperty(value = "主营收入(元)", name = "businessIncome")
    private BigDecimal businessIncome;
    /**
     * 主营外收入(元)
     */
    @ApiModelProperty(value = "主营外收入(元)", name = "outerBusinessIncome")
    private BigDecimal outerBusinessIncome;
    /**
     * 实际结算金额（元）
     */
    @ApiModelProperty(value = "实际结算金额（元）", name = "practicalCloseMoney")
    private BigDecimal practicalCloseMoney;
    /**
     * 成本(元)
     */
    @ApiModelProperty(value = "成本(元)", name = "cost")
    private BigDecimal cost;
    /**
     * 利润(元)
     */
    @ApiModelProperty(value = "利润(元)", name = "profit")
    private BigDecimal profit;
    /**
     * 是否删除(0否,2是)
     */
    @ApiModelProperty(value = "是否删除(0否,2是)", name = "deleted")
    private Integer deleted;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createUserId")
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", name = "updateUserId")
    private Long updateUserId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", name = "updateTime")
    private Date updateTime;
    /**
     * 其他附件id
     */
    @ApiModelProperty(value = "其他附件id", name = "restFileId")
    private String restFileId;

}
