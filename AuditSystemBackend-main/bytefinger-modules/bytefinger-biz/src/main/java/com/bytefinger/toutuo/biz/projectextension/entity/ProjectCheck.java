package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 承接查验
 *
 * @author ycj
 * @email
 * @date 2023-03-15 14:44:28
 */
@Data
@TableName("biz_project_check")
public class ProjectCheck extends BaseEntity {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;
    /**
     * 查验状态(0未完成,1进行中,2已完成)
     */
    @ApiModelProperty(value = "查验状态(0未完成,1进行中,2已完成)", hidden = false, required = false)
    @HistoryFieldLog("查验状态(0未完成,1进行中,2已完成)")
    @Excel(name = "查验状态(0未完成,1进行中,2已完成)")
    @TableField(value = "check_state")
    private Integer checkState;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", hidden = false, required = false)
    @HistoryFieldLog("开始时间")
    @Excel(name = "开始时间")
    @TableField(value = "start_time",updateStrategy=FieldStrategy.IGNORED)
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", hidden = false, required = false)
    @HistoryFieldLog("结束时间")
    @Excel(name = "结束时间")
    @TableField(value = "end_time",updateStrategy=FieldStrategy.IGNORED)
    private Date endTime;
    /**
     * 责任id
     */
    @ApiModelProperty(value = "责任id", hidden = false, required = false)
    @HistoryFieldLog("责任id")
    @Excel(name = "责任id")
    @TableField(value = "person_charge_id")
    private Integer personChargeId;
    /**
     * 责任名称
     */
    @ApiModelProperty(value = "责任名称", hidden = false, required = false)
    @HistoryFieldLog("责任名称")
    @Excel(name = "责任名称")
    @TableField(value = "person_charge_name")
    private String personChargeName;
    /**
     * 发现问题数
     */
    @ApiModelProperty(value = "发现问题数", hidden = false, required = false)
    @HistoryFieldLog("发现问题数")
    @Excel(name = "发现问题数")
    @TableField(value = "problem_num")
    private Integer problemNum;
    /**
     * 已整改数量
     */
    @ApiModelProperty(value = "已整改数量", hidden = false, required = false)
    @HistoryFieldLog("已整改数量")
    @Excel(name = "已整改数量")
    @TableField(value = "rectify_finished_num")
    private Integer rectifyFinishedNum;
    /**
     * 待整改数量
     */
    @ApiModelProperty(value = "待整改数量", hidden = false, required = false)
    @HistoryFieldLog("待整改数量")
    @Excel(name = "待整改数量")
    @TableField(value = "rectify_await_num")
    private Integer rectifyAwaitNum;

    /**
     * 整改期限
     */
    @ApiModelProperty(value = "整改期限", hidden = false, required = false)
    @HistoryFieldLog("整改期限")
    @Excel(name = "整改期限")
    @TableField(value = "rectify_deadline",updateStrategy= FieldStrategy.IGNORED)
    private Date rectifyDeadline;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @HistoryFieldLog("创建人")
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Excel(name = "创建人", dataType = Excel.DataType.USER)
    private Long createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @HistoryFieldLog("创建时间")
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
    @HistoryFieldLog("修改时间")
    @Excel(name = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 暂存
     */
    @ApiModelProperty(value = "暂存(0暂存,1.已完成)", hidden = false, required = false)
    @HistoryFieldLog("暂存(0暂存,1.已完成)")
    @Excel(name = "暂存(0暂存,1.已完成)")
    @TableField(value = "temporary")
    private String temporary;

}
