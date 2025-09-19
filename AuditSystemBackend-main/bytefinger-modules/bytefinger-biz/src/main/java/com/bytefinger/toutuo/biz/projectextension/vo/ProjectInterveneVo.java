package com.bytefinger.toutuo.biz.projectextension.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 拓后运营项目干预列表
 *
 * @author ycj
 * @email
 * @date 2023-03-21 16:14:17
 */
@Data
@ApiModel(value = "拓后运营项目干预")
public class ProjectInterveneVo extends Project {

    /**
     *是否已下达干预方案0否2是
     */
    @ApiModelProperty(value = "", name = "isNotTransmitInterveneScheme")
    private String isNotTransmitInterveneScheme;

    /**
     * 干预方案名称
     */
    @ApiModelProperty(value = "干预方案名称", hidden = false, required = false)
    @HistoryFieldLog("干预方案名称")
    private String interveneSchemeName;

    @ApiModelProperty(value = "方案实施人信息", hidden = false, required = false)
    @DataFillField(dataField = "schemeUserId", fillMethod = FillMethod.USER)
    private UserVO schemeUser;

    /**
     * 最新方案下达日期
     */
    @ApiModelProperty(value = "最新方案下达日期", hidden = false, required = false)
    @HistoryFieldLog("最新方案下达日期")
    private Date transmitSchemeTime;

    /**
     * 干预执行状态
     */
    @ApiModelProperty(value = "干预执行状态(0干预待执行，1待检查，2执行不通过，3已完成)", hidden = false, required = false)
    @HistoryFieldLog("干预执行状态")
    @Excel(name = "干预执行状态")
    private Integer interveneState;

    @ApiModelProperty(value = "下达人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO transmitSchemeUser;

    /**
     * 当前登入人是否是项目归属人、拓后负责人、项目团队成员
     */
    @ApiModelProperty(value = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", hidden = false, required = false)
    @Schema(description = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", name = "checkState")
    private Boolean show;

}
