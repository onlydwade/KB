package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.bytefinger.common.security.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jone Ying
 * @Date 2023/3/4
 **/
@Data
public class ActualInDetailExcelVO {

    @Excel(sort = 1)
    private String name;

    @Excel(sort = 2)
    private String subtotal;

    @Excel(sort = 3)
    private String monthIndex1;
    @Excel(sort = 4)
    private String monthIndex2;
    @Excel(sort = 5)
    private String monthIndex3;
    @Excel(sort = 6)
    private String monthIndex4;
    @Excel(sort = 7)
    private String monthIndex5;
    @Excel(sort = 8)
    private String monthIndex6;
    @Excel(sort = 9)
    private String monthIndex7;
    @Excel(sort = 10)
    private String monthIndex8;
    @Excel(sort = 11)
    private String monthIndex9;
    @Excel(sort = 12)
    private String monthIndex10;
    @Excel(sort = 13)
    private String monthIndex11;
    @Excel(sort = 14)
    private String monthIndex12;


}
