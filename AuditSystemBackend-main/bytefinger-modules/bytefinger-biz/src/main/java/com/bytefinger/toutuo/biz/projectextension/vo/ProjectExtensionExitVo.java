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

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 拓后项目退场
 *
 * @author ycj
 * @email
 * @date 2023-03-27 16:23:19
 */
@Data
@ApiModel(value = "拓后项目退场")
public class ProjectExtensionExitVo extends Project {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", name = "projectId")
    private Long projectId;
    /**
     * 主题
     */
    @ApiModelProperty(value = "主题", name = "title")
    private String title;

    /**
     * 处理状态(0未处理,1审批中,2已处理)
     */
    @ApiModelProperty(value = "处理状态(0未处理,1审批中,2已处理,3处理驳回)", hidden = false, required = false)
    private Integer processState;
    /**
     * 处理方式(0未续签,1续签,2重新投标,3退场)
     */
    @ApiModelProperty(value = "处理方式(0未续签,1续签,2重新投标,3退场)", hidden = false, required = false)
    private Integer processMode;
    /**
     * oa审批状态
     */
    @ApiModelProperty(value = "oa审批状态", name = "approvalStatus")
    private Integer approvalStatus;
    /**
     * oa审批url
     */
    @ApiModelProperty(value = "oa审批url", name = "approvalUrl")
    private String approvalUrl;
    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人", name = "approvalUserId")
    private Long approvalUserId;
    /**
     * 申请人名称
     */
    @ApiModelProperty(value = "申请人名称", name = "approvalUserName")
    private String approvalUserName;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间", name = "approvalSendTime")
    private String approvalSendTime;

    /**
     * 退场描述
     */
    @ApiModelProperty(value = "退场描述", name = "content")
    private String content;

    @ApiModelProperty(value = "申请人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "approvalUserId", fillMethod = FillMethod.USER)
    private UserVO approvalUser;
}
