package com.bytefinger.toutuo.biz.project.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "ProjectAchievement对象", description = "项目业绩分配")
public class ProjectAchievementBO extends BaseEntity {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @TableField("project_id")
    @HistoryFieldLog("项目id")
    private Long projectId;





    /**
     * 所属大区
     */
    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField("region_id")
    @HistoryFieldLog(value = "所属大区", joinField = "regionName")
    @Excel(name = "大区", dataType = Excel.DataType.DEPT, sort = 1)
    private Long regionId;

    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "regionId", fillMethod = FillMethod.DEPT)
    private String regionName;


    /**
     * 地区公司
     */
    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField("company_id")
    @HistoryFieldLog(value = "地区公司", joinField = "companyName")
    @Excel(name = "归属单位", dataType = Excel.DataType.DEPT, sort = 2)
    private Long companyId;

    @ApiModelProperty(value = "项目名称", hidden = false, required = false)
    @TableField("project_name")
    @HistoryFieldLog("项目名称")
    @Excel(name = "项目名称", sort = 3)
    private String projectName;


    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "companyId", fillMethod = FillMethod.DEPT)
    private String companyName;


    /**
     * 拓展模式
     */
    @ApiModelProperty(value = "拓展模式", hidden = false, required = false)
    @TableField("expansion_mode")
    @HistoryFieldLog("拓展模式")
    @Dict(ptype = "XIANG_MU_LEI_XING")
    @Excel(name = "拓展模式", sort = 4)
    private String expansionMode;

    /**
     * 招标类型
     */
    @ApiModelProperty(value = "招标类型", hidden = false, required = false)
    @TableField("biding_mode")
    @HistoryFieldLog("招标类型")
    @Dict(type = "ZHAO_BIAO_LEI_XING")
    @Excel(name = "招标类型", sort = 5)
    private String bidingMode;
    /**
     * 业态
     */
    @ApiModelProperty(value = "业态", hidden = false, required = false)
    @TableField("business_type")
    @HistoryFieldLog("业态")
    //@Dict(ptype = "XIANG_MU_YE_TAI")
    private String businessType;

    @ApiModelProperty(value = "业态", hidden = false, required = false)
    @TableField("business_type")
    @HistoryFieldLog("业态")
    @Excel(name = "业态", sort = 6)
    private String businessTypeStr;

    /**
     * 是否存量项目
     */
    @ApiModelProperty(value = "是否为续签项目", hidden = false, required = false)
    @TableField("in_stock")
    @HistoryFieldLog("是否为续签项目")
    @Dict(type = "SHI_FOU")
    @Excel(name = "是否为续签项目", sort = 7)
    private String inStock;



    /**
     * 签约时间
     */
    @ApiModelProperty(value = "签约时间", hidden = false, required = false)
    @TableField("sign_time")
    @HistoryFieldLog("签约时间")
    private Date signTime;

    @ApiModelProperty(value = "签约时间", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("签约时间")
    @Excel(name = "签约日期", sort = 8)
    private String signTimeStr;


    /**
     * 服务开始时间
     */
    @ApiModelProperty(value = "服务开始时间", hidden = false, required = false)
    @TableField("service_begin_time")
    @HistoryFieldLog("服务开始时间")
    private Date serviceBeginTime;

    @ApiModelProperty(value = "服务开始时间", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("服务开始时间")
    @Excel(name = "服务开始日期", sort = 9)
    private String serviceBeginTimeStr;


    /**
     * 服务结束时间
     */
    @ApiModelProperty(value = "服务结束时间", hidden = false, required = false)
    @TableField("service_end_time")
    @HistoryFieldLog("服务结束时间")
    private Date serviceEndTime;


    @ApiModelProperty(value = "服务结束时间", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("服务结束时间")
    @Excel(name = "合同到期日期", sort = 10)
    private String serviceEndTimeStr;


    private Date performanceConfirmTime;

    /**
     * 拟服务期限(单位:月)
     */
    @ApiModelProperty(value = "拟服务期限(单位:月)", hidden = false, required = false)
    @TableField("proposed_service_period")
    @HistoryFieldLog("拟服务期限(单位:月)")
    @Excel(name = "拟服务期限(月)", sort = 11)
    private Integer proposedServicePeriod;


    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积", hidden = false, required = false)
    @TableField("construction_area")
    @HistoryFieldLog("建筑面积")
    @Excel(name = "建筑面积(㎡)", sort = 12)
    private BigDecimal constructionArea;


    /**
     * 合同金额(单位：元)
     */
    @ApiModelProperty(value = "合同金额(单位：元)", hidden = false, required = false)
    @TableField("contract_amount")
    @HistoryFieldLog("合同金额(单位：元)")
    @Excel(name = "合同总金额(元)", sort = 13)
    private BigDecimal contractAmount;


    /**
     * 合同年度金额(单位：元)
     */
    @ApiModelProperty(value = "合同年度金额(单位：元)", hidden = false, required = false)
    @TableField("contract_annual_amount")
    @HistoryFieldLog("合同年度金额(单位：元)")
    @Excel(name = "合同年度金额(元)", sort = 14)
    private BigDecimal contractAnnualAmount;



    /**
     * 当年转化金额(单位：元)
     */
    @ApiModelProperty(value = "当年转化金额(单位：元)", hidden = false, required = false)
    @TableField("annual_conversion_amount")
    @HistoryFieldLog("当年转化金额(单位：元)")
    @Excel(name = "当年转化收入(元)", sort = 15)
    private BigDecimal annualConversionAmount;

    /**
     * 甲方单位名称
     */
    @ApiModelProperty(value = "甲方单位名称", hidden = false, required = false)
    @TableField("first_responsible_company")
    @HistoryFieldLog("甲方单位名称")
    @Excel(name = "甲方单位名称", sort = 16)
    private String firstResponsibleCompany;

    @ApiModelProperty(value = "服务内容", hidden = false, required = false)
    @TableField("service_content")
    @HistoryFieldLog("服务内容")
    @Excel(name = "服务内容", sort = 17)
    @Dict(type = "FU_WU_NEI_RONG")
    private String serviceContent;



    /**
     * 拓展单位
     */
    @ApiModelProperty(value = "拓展单位", hidden = false, required = false)
    @TableField("expand_company_id")
    @HistoryFieldLog("拓展单位")
    private Long expandCompanyId;

    @Excel(name = "拓展单位", sort = 18)
    @ApiModelProperty(value = "拓展单位", hidden = false, required = false)
    @TableField(exist = false)
    @HistoryFieldLog("拓展单位")
    @DataFillField(dataField = "expandCompanyId", fillMethod = FillMethod.DEPT)
    private String expandCompanyName;

    /**
     * 业绩分配比例(%)
     */
    @ApiModelProperty(value = "业绩分配比例(%)", hidden = false, required = false)
    @TableField("assignment_rate")
    @HistoryFieldLog("业绩分配比例(%)")
    @Excel(name = "业绩分配比例(%)", sort = 19)
    private BigDecimal assignmentRate;

    /**
     * 业绩分配描述
     */
    @ApiModelProperty(value = "业绩分配描述", hidden = false, required = false)
    @TableField("assignment_desc")
    @HistoryFieldLog("业绩分配描述")
    @Excel(name = "业绩分配描述", sort = 20)
    private String assignmentDesc;

    /**
     * 合同下载地址
     */
    @ApiModelProperty(value = "合同下载地址", hidden = false, required = false)
    @TableField("download_path")
    @HistoryFieldLog("合同下载地址")
     @Excel(name = "合同下载地址", sort = 21)
    private String downloadPath;
}
