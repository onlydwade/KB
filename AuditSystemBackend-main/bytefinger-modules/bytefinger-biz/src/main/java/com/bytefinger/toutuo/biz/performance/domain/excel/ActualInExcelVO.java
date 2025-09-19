package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.bytefinger.common.security.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author Jone Ying
 * @Date 2023/3/4
 **/
@Data
public class ActualInExcelVO {

    @Excel(sort = 1, needMerge = true)
    private String regionName;
    @Excel(sort = 2, needMerge = true)
    private String companyName;

    @Excel(sort = 3, needMerge = true)
    private String name;

    @Excel(sort = 4, needMerge = true)
    private String signTime;

    @Excel(sort = 5, needMerge = true)
    private String serviceTime;

    @Excel(sort = 6, needMerge = true)
    private String proposedServicePeriod;

    @Excel(sort = 7, needMerge = true)
    private String enterTime;

    @Excel(sort = 8, needMerge = true)
    private String constructionArea;

    @Excel
    private List<ActualInDetailExcelVO> list;


}
