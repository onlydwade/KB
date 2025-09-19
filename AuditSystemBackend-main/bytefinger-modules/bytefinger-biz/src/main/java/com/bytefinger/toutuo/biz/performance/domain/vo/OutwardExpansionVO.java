package com.bytefinger.toutuo.biz.performance.domain.vo;

import lombok.Data;

@Data
public class OutwardExpansionVO {

    /**
     * 本月认领线索
     */
    private Integer leadCount;

    /**
     * 本月转商机
     */
    private Integer opportunityCount;

    /**
     * 本月签单数量
     */
    private Integer agreementCount;

    /**
     * 本月创建客户
     */
    private Integer customerCount;

    /**
     * 年度业绩完成率
     */
    private String achievement;

    /**
     * 年度项目总数
     */
    private Integer projectCount;


}
