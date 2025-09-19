package com.bytefinger.toutuo.biz.projectextension.vo;

import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 项目评估
 *
 * @author ycj
 * @email
 * @date 2023-03-16 10:18:21
 */
@Data
@ApiModel(value = "项目评估")
public class ProjectAssessVo extends Project {

    @ApiModelProperty(value = "经营评估期限", hidden = false, required = false)
    @HistoryFieldLog("经营评估期限")
    @Excel(name = "经营评估期限")
    private String assessDeadline;

    @ApiModelProperty(value = "评估日期", hidden = false, required = false)
    @HistoryFieldLog("评估日期")
    @Excel(name = "评估日期")
    private Date assessTime;

    @ApiModelProperty(value = "评估标题", hidden = false, required = false)
    @HistoryFieldLog("评估标题")
    @Excel(name = "评估标题")
    private String assessTitle;

    @ApiModelProperty(value = "是否评估", hidden = false, required = false)
    @HistoryFieldLog("是否评估")
    @Excel(name = "是否评估")
    private String assessState;

    /**
     * 当前登入人是否是项目归属人、拓后负责人、项目团队成员
     */
    @ApiModelProperty(value = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", hidden = false, required = false)
    @Schema(description = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", name = "checkState")
    private Boolean show;
}
