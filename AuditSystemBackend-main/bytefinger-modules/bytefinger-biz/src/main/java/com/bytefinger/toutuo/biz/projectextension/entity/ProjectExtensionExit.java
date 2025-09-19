package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拓后项目退场
 *
 * @author ycj
 * @email
 * @date 2023-03-27 16:23:19
 */
@Data
@TableName("biz_project_extension_exit")
public class ProjectExtensionExit extends BizProjectBaseEntity{

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField("project_id")
    private Long projectId;
    /**
     * 主题
     */
    @ApiModelProperty(value = "主题", hidden = false, required = false)
    @HistoryFieldLog("主题")
    @Excel(name = "主题")
    @TableField("title")
    private String title;
    /**
     * oa审批状态
     */
    @ApiModelProperty(value = "OA 审批状态 1-发起审批 2-审批通过 3-审批驳货", hidden = false, required = false)
    @HistoryFieldLog("OA 审批状态 1-发起审批 2-审批通过 3-审批驳货")
    @Excel(name = "oa审批状态")
    @TableField("approval_status")
    private Integer approvalStatus;
    /**
     * oa审批url
     */
    @ApiModelProperty(value = "oa审批url", hidden = false, required = false)
    @HistoryFieldLog("oa审批url")
    @Excel(name = "oa审批url")
    @TableField("approval_url")
    private String approvalUrl;
    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人", hidden = false, required = false)
    @HistoryFieldLog(value = "申请人",joinField = "approvalUser")
    @Excel(name = "申请人")
    @TableField("approval_user_id")
    private Long approvalUserId;

    /**
     * 申请人名称
     */
    @ApiModelProperty(value = "申请人名称", hidden = false, required = false)
    @HistoryFieldLog("申请人名称")
    @Excel(name = "申请人名称")
    @TableField("approval_user_name")
    private String approvalUserName;

    /**
     * 申请人归属
     */
    @ApiModelProperty(value = "申请人归属", hidden = false, required = false)
    @HistoryFieldLog("申请人归属")
    @Excel(name = "申请人归属")
    @TableField("approval_affiliation")
    private String approvalAffiliation;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间", hidden = false, required = false)
    @HistoryFieldLog("申请时间")
    @Excel(name = "申请时间")
    @TableField("approval_send_time")
    private String approvalSendTime;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "退场发起时间", hidden = false, required = false)
    @HistoryFieldLog("退场发起时间")
    @Excel(name = "退场发起时间")
    @TableField("approval_sponsor_time")
    private Date approvalSponsorTime;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", hidden = false, required = false)
    @HistoryFieldLog("联系电话")
    @Excel(name = "联系电话")
    @TableField("relation_phone")
    private String relationPhone;
    /**
     * 是否涉及金额
     */
    @ApiModelProperty(value = "是否涉及金额", hidden = false, required = false)
    @HistoryFieldLog("是否涉及金额")
    @Excel(name = "是否涉及金额")
    @TableField("whether_involve_money")
    private Integer whetherInvolveMoney;
    /**
     * 涉及金额
     */
    @ApiModelProperty(value = "涉及金额", hidden = false, required = false)
    @HistoryFieldLog("涉及金额")
    @Excel(name = "涉及金额")
    @TableField("involve_money")
    private BigDecimal involveMoney;
    /**
     * 大写金额
     */
    @ApiModelProperty(value = "大写金额", hidden = false, required = false)
    @HistoryFieldLog("大写金额")
    @Excel(name = "大写金额")
    @TableField("majuscule_money")
    private String majusculeMoney;
    /**
     * 退场描述
     */
    @ApiModelProperty(value = "退场描述", hidden = false, required = false)
    @HistoryFieldLog("退场描述")
    @Excel(name = "退场描述")
    @TableField("content")
    private String content;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @HistoryFieldLog("备注")
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;


    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Excel(name = "创建人", dataType = Excel.DataType.USER)
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @HistoryFieldLog(value ="修改人",joinField = "updateUser")
    @Excel(name = "修改人")
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    @Excel(name = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "项目名称", hidden = false, required = false)
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty(value = "公司", hidden = false, required = false)
    @TableField(exist = false)
    private String companyName;

    @ApiModelProperty(value = "所属部门", hidden = false, required = false)
    @TableField(exist = false)
    private String businessDeptName;

    @ApiModelProperty(value = "申请人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "approvalUserId", fillMethod = FillMethod.USER)
    private UserVO approvalUser;

    /**
     * 暂存
     */
    @ApiModelProperty(value = "暂存", hidden = false, required = false)
    @HistoryFieldLog("暂存")
    @Excel(name = "暂存")
    @TableField(exist = false)
    private String temporary;

//    /**
//     * 草稿(0草稿,1.已完成)
//     */
//    @ApiModelProperty(value = "草稿(0草稿,1.已完成)", hidden = false, required = false)
//    @HistoryFieldLog("草稿(0草稿,1.已完成)")
//    @Excel(name = "草稿(0草稿,1.已完成)")
//    @TableField(value = "draft")
//    private Integer draft;

}
