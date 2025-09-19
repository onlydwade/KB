package com.bytefinger.toutuo.biz.performance.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrMothTotalVo {
    private Long userId;

    private BigDecimal currMonthTotal;

    private BigDecimal currMonthSum;

    private String currMonth;
}
