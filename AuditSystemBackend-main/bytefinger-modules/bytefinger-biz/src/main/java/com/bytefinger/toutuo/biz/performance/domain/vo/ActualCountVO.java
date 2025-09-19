package com.bytefinger.toutuo.biz.performance.domain.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "动态业绩统计")
public class ActualCountVO {

    private String label;//
    private String key;//统计的类型
    private String year;//统计的年份
    private String mouth;//统计的月份
    private String percent;//实际所占百分比
    private String type;

    private String cnType;

    private BigDecimal value = BigDecimal.ZERO;//金额

    private List<ActualCountVO> mouthList;//月份的子集

}
