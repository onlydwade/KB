package com.bytefinger.toutuo.biz.projectextension.vo;

import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel(value = "立项收缴率")
public class ProjectCollectionRateVo {
    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "id", hidden = false, required = false)
    @HistoryFieldLog("id")
    private Long id;
    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第一年（%）", hidden = false, required = false)
    @HistoryFieldLog("立项收缴率指标第一年(%)")
    private BigDecimal collectionRateFirst;

    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第二年（%）", hidden = false, required = false)
    @HistoryFieldLog("立项收缴率指标第二年(%)")
    private BigDecimal collectionRateSecond;

    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第三年（%）", hidden = false, required = false)
    @HistoryFieldLog("立项收缴率指标第三年(%)")
    private BigDecimal collectionRateThird;
}
