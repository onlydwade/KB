package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 拓后运营项目关联
 *
 * @author ycj
 * @email
 * @date 2023-03-09
 */
@Data
@TableName("biz_project_correlation")
public class ProjectCorrelation extends BaseEntity {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目名称", hidden = false, required = false)
    @HistoryFieldLog("项目名称")
    @Excel(name = "项目名称")
    @TableField(exist = false)
    private String projectName;

    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id", hidden = false, required = false)
    @HistoryFieldLog("批次id")
    @Excel(name = "批次id")
    @TableField(value = "batch_id")
    private Long batchId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("关联项目id")
    @Excel(name = "项目id")
    @TableField(exist = false)
    private Long projectRelevantId;
    /**
     * 是否删除 0-否 2-是
     */
    @ApiModelProperty(value = "是否删除 0-否 2-是", hidden = false, required = false)
    @HistoryFieldLog("是否删除 0-否 2-是")
    @Excel(name = "是否删除 0-否 2-是")
    @TableField(value = "deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;
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
}
