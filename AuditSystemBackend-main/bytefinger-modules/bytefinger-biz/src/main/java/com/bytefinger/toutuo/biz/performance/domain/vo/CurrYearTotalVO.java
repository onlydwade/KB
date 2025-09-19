package com.bytefinger.toutuo.biz.performance.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrYearTotalVO {

    private Long userId;

    private BigDecimal currYearTotal;

    private BigDecimal currYearSum;

    private String currYear;
}
