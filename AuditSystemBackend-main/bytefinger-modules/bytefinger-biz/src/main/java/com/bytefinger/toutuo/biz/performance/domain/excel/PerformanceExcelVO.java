package com.bytefinger.toutuo.biz.performance.domain.excel;

import com.bytefinger.common.security.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * @Author Jone Ying
 * @Date 2023/3/4
 **/
@Data
public class PerformanceExcelVO {

    @Excel(sort = 1, needMerge = true)
    private String name;

    @Excel(sort = 2, needMerge = true)
    private String rate;

    @Excel
    private List<PerformanceDetailExcelVO> list;


}
