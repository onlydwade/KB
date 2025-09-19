package com.bytefinger.toutuo.biz.performance.domain.vo;

import com.bytefinger.common.security.annotation.BigDecimalSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HeadStatisticalVO {
    /**
     * 今日认领线索数量
     */
    private Integer leadCurryDays;
    /**
     * 累计线索量
     */
    private Integer leadCount;
    /**
     * 今日转商机数量
     */
    private Integer opportunityCurrDays;

    /**
     * 累计转商机数量
     */
    private Integer opportunityCount;

    /**
     * 今日签约数量
     */
    private Integer agreementDaysNum;

    /**
     * 累计签约数量
     */
    private Integer agreementNum;

    /**
     * 今日签约金额
     */
    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal agreementDaysAmount;

    /**
     * 累计签约金额
     */
    @JsonSerialize(using = BigDecimalSerialize.class)
    private BigDecimal agreementAmount;

}
