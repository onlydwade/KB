package com.bytefinger.toutuo.biz.project.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 操作历史
 * </p>
 */
@Data
@TableName("biz_project_operation_history")
@ApiModel(value = "操作历史", description = "操作历史")
public class ProjectOperationHistory extends BaseEntity {

    @ApiModelProperty(value = "老项目id", hidden = false, required = false)
    @TableField("old_project_id")
    private Long oldProjectId;

    @ApiModelProperty(value = "新项目id", hidden = false, required = false)
    @TableField("new_project_id")
    private Long newProjectId;

    @ApiModelProperty(value = "操作之前状态", hidden = false, required = false)
    @TableField("old_status")
    private String oldStatus;

    @ApiModelProperty(value = "操作之前处理方式", hidden = false, required = false)
    @TableField("old_process_mode")
    private String oldProcessMode;

    @ApiModelProperty(value = "操作类型", hidden = false, required = false)
    @TableField("operation_type")
    private String operationType;

    @ApiModelProperty(value = "处理人", hidden = false, required = false)
    @TableField("operation_man")
    private Long operationMan;

    @ApiModelProperty(value = "处理时间", hidden = false, required = false)
    @TableField(value = "operation_time", fill = FieldFill.INSERT)
    private Date operationTime;

    @ApiModelProperty(value = "是否删除", hidden = false, required = false)
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;


}