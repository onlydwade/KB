package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.bytefinger.common.security.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * 业绩签约汇总导出数据封装*
 */
@Data
public class ActualInExcelSummaryHeadVO {

    /**
     * 大区*
     */
    @Excel(sort = 1)
    private String regionName;

    /**
     * 单位*
     */
    @Excel(sort = 2)
    private String companyName;

    /**
     * 项目名称*
     */
    @Excel(sort = 3)
    private String projectName;

    /**
     * 业务板块*
     */
    @Excel(sort = 4)
    private String businessSegments;

    /**
     * 拓展模式*
     */
    @Excel(sort = 4)
    private String expansionMode;

    /**
     * 招标类型*
     */
    @Excel(sort = 5)
    private String bidingMode;

    /**
     * 业态*
     */
    @Excel( sort = 6)
    private String businessType;

    /**
     * 是否为续签项目*
     */
    @Excel(sort = 7)
    private String inStock;

    /**
     * 是否计算业绩*
     */
    @Excel(sort = 7)
    private String isCountPerformance;

    /**
     * 合同金额(元)*
     */
    @Excel( sort = 8)
    private String contractAmount;

    /**
     * 合同年度金额(单位：元)
     */

    @Excel(sort = 9)
    private String contractAnnualAmount;

    /**
     * 当年转化金额(单位：元)
     */
    @Excel( sort = 10)
    private String annualConversionAmount;


    /**
     * 增量合同总金额(单位：元)
     */

    @Excel( sort = 11)
    private String contractAmounts;

    /**
     * 增量合同年度金额(元)*
     */
    @Excel( sort = 12)
    private String contractAnnualAmounts;

    /**
     * 增量当年转化金额(元)*
     */
    @Excel( sort = 13)
    private String annualConversionAmounts;

    /**
     * 建筑面积 (㎡)*
     */
    @Excel(sort = 14)
    private String constructionArea;

    /**
     * 签约日期*
     */
    @Excel(sort = 15)
    private String signTime;

    /**
     * 服务开始时间
     */
    @Excel(sort = 16)
    private String serviceBeginTime;

    /**
     * 合同到期时间
     */
    @Excel(sort = 17)
    private String serviceEndTime;

    /**
     * 拟服务期(月)*
     */
    @Excel(sort = 18)
    private String proposedServicePeriod;


    /**
     * 拓展单位*
     */
    @Excel(sort = 19)
    private String expandCompany ;

    /**
     * 业绩分配比例（%）*
     */
    @Excel(sort = 20)
    private String expandCompanyRate ;

    /**
     * 业绩分配描述*
     */
    @Excel(sort = 21)
    private String expandCompanyRemark ;

    /**
     * 甲方单位名称*
     */
    @Excel( sort = 22)
    private String firstResponsibleCompany;

    /**
     * 乙方单位名称*
     */
    @Excel( sort = 23)
    private String secondParty;

    /**
     * 服务内容*
     */
    @Excel( sort = 24)
    private String serviceContent;

    /**
     * 省份*
     */
    @Excel(sort = 25)
    private String province;

    /**
     * 城市*
     */
    @Excel(sort = 26)
    private String city;

    /**
     * 合同下载地址
     */
    @Excel(sort = 27)
    private String downloadPath;



}
