package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * 拓后管理续签状态
 *
 * @author ycj
 * @email
 * @date 2023-03-09
 */
@Data
@TableName("biz_project_extension")
public class ProjectExtension extends BaseEntity {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;

    /**
     * 关联新建项目id
     */
    @ApiModelProperty(value = "关联新建项目id", hidden = false, required = false)
    @HistoryFieldLog("关联新建项目id")
    @Excel(name = "关联新建项目id")
    @TableField(value = "relevance_project_id")
    private Long relevanceProjectId;
    /**
     * 处理状态(0未处理,1审批中,2已处理)
     */
    @ApiModelProperty(value = "处理状态(0未处理,1审批中,2已处理,3处理驳回)", hidden = false, required = false)
    @HistoryFieldLog("处理状态(0未处理,1审批中,2已处理,3处理驳回)")
    @Excel(name = "处理状态(0未处理,1审批中,2已处理)")
    @TableField(value = "process_state")
    private Integer processState;
    /**
     * 处理方式(0未续签,1续签,2重新投标,3退场)
     */
    @ApiModelProperty(value = "处理方式(0未续签,1续签,2重新投标,3退场)", hidden = false, required = false)
    @HistoryFieldLog("处理方式(0未续签,1续签,2重新投标,3退场)")
    @Excel(name = "处理方式(0未续签,1续签,2重新投标,3退场)")
    @TableField(value = "process_mode")
    private Integer processMode;
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
