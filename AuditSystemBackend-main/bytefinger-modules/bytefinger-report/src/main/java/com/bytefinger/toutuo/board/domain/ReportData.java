package com.bytefinger.toutuo.board.domain;

import com.bytefinger.common.security.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 描述:
 * NC系统指标报表坐标导出类
 *
 * @author LaiDaCheng
 * @email dacheng.lai@bytefinger.com
 * @date 2023/3/21 14:15
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ReportData对象", description = "NC系统指标报表坐标导出类")
public class ReportData implements Serializable {
    @ApiModelProperty(value = "指标在NC表样中坐标", hidden = false, required = false)
    @Excel(name = "指标在NC表样中坐标")
    private String position;
    @Excel(name = "指标名称")
    @ApiModelProperty(value = "指标名称", hidden = false, required = false)
    private String name;
    @Excel(name = "指标值")
    @ApiModelProperty(value = "指标值", hidden = false, required = false)
    private String value;
}
