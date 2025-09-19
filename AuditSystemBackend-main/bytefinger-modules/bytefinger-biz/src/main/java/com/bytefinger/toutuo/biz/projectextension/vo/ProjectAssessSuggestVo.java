package com.bytefinger.toutuo.biz.projectextension.vo;

import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "项目评估建议")
public class ProjectAssessSuggestVo {
    @ApiModelProperty(value = "标签", hidden = false, required = false)
    @HistoryFieldLog("标签")
    @Excel(name = "标签")
    private String label;

    @ApiModelProperty(value = "风险描述", hidden = false, required = false)
    @HistoryFieldLog("风险描述")
    @Excel(name = "风险描述")
    private String riskDescribe;

    @ApiModelProperty(value = "改进建议", hidden = false, required = false)
    @HistoryFieldLog("改进建议")
    @Excel(name = "改进建议")
    private String improveSuggest;
}
