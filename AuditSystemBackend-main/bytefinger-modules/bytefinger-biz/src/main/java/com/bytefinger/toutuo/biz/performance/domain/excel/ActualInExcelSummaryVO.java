package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author Jone Ying
 * @Date 2023/3/4
 **/
@Data
public class ActualInExcelSummaryVO {

    @Excel(sort = 1)
    private String regionName;
    @Excel(sort = 2)
    private String companyName;

    @Excel(sort = 3)
    private String projectName;

    @Excel(sort = 4)
    @Dict(ptype = "XIANG_MU_LEI_XING")
    private String expansionMode;

    @Excel(sort = 5)
    @Dict(type = "ZHAO_BIAO_LEI_XING")
    private String bidingMode;

    @Excel( sort = 6)
    @HistoryFieldLog("业态")
    private String businessType;

    @Excel(sort = 7)
    @Dict(type = "SHI_FOU")
    private String inStock;

    @Excel(sort = 8)
    private String constructionArea;


    @Excel(sort = 9)
    private String signTime;

    /**
     * 服务开始时间
     */
    @Excel(sort = 10)
    private String serviceBeginTime;

    /**
     * 服务结束时间
     */
    @Excel(sort = 11)
    private String serviceEndTime;

    @Excel(sort = 12)
    private String proposedServicePeriod;

    @Excel( sort = 13)
    private String contractAmount;

    /**
     * 合同年度金额(单位：元)
     */

    @Excel(sort = 14)
    private String contractAnnualAmount;

    /**
     * 当年转化金额(单位：元)
     */

    @Excel( sort = 15)
    private String annualConversionAmount;

    /**
     * 合同金额(单位：元)
     */

    @Excel( sort = 16)
    private String contractAmounts;


    @Excel( sort = 17)
    private String contractAnnualAmounts;


    @Excel( sort = 18)
    private String annualConversionAmounts;

    @Excel(sort = 19)
    private String expandCompany ;

    @Excel(sort = 20)
    private String expandCompanyRate ;
    @Excel(sort = 21)
    private String expandCompanyRemark ;
    @Excel( sort = 22)
    @Dict(type = "FU_WU_NEI_RONG")
    private String serviceContent;
    @Excel( sort = 23)
    private String firstResponsibleCompany;
    @Excel(sort = 24)
    @Cnarea(type = CnareaEnum.PROVINCE)
    @HistoryFieldLog("省")
    private String province;
    @Excel(sort = 25)
    @HistoryFieldLog("市")
    @Cnarea(type = CnareaEnum.CITY)
    private String city;

    /**
     * 合同下载地址
     */
    @Excel(sort = 26)
    private String downloadPath;
}



