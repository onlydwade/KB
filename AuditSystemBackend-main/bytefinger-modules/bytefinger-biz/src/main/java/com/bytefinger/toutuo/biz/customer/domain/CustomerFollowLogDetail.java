package com.bytefinger.toutuo.biz.customer.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.domain.TimeBaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户跟踪记录明细表
 * </p>
 *
 * @author lin
 * @since 2022-10-25
 */
@Data
@TableName("biz_customer_follow_log_detail")
@ApiModel(value = "客户跟踪记录明细表", description = "客户跟踪记录明细表")
public class CustomerFollowLogDetail extends TimeBaseEntity {

//    @TableId(type = IdType.AUTO)
//    @ApiModelProperty(value = "主键id")
//    @JsonSerialize(using = ToStringSerializer.class)
//    @TableField("id")
//    private Long id;

    @ApiModelProperty(value = "跟进动态id")
    @HistoryFieldLog("跟进动态id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("follow_log_id")
    private Long followLogId;

    @ApiModelProperty(value = "任务情况")
    @HistoryFieldLog("任务情况")
    @TableField("task_status")
    @Dict(type = "REN_WU_QING_KUANG")
    private String taskStatus;

    @ApiModelProperty(value = "工作摘要")
    @HistoryFieldLog("工作摘要")
    @TableField("work_summary")
    private String workSummary;

    @ApiModelProperty(value = "跟进状态")
    @HistoryFieldLog("跟进状态")
    @TableField("follow_status")
    private String followStatus;

    @ApiModelProperty(value = "专班建立")
    @HistoryFieldLog("专班建立")
    @TableField("team_establish")
    private String teamEstablish;

    @ApiModelProperty(value = "负责人")
    @HistoryFieldLog("负责人")
    @TableField("head")
    private String head;

    @ApiModelProperty(value = "是否已删除")
    @TableField("deleted")
    private Integer deleted;

}