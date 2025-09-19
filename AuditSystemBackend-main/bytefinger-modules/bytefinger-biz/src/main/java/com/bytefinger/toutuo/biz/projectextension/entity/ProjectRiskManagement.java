package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.domain.TimeBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拓后运营-风险管理
 *
 * @author ycj
 * @email
 * @date 2023-03-20 15:05:12
 */
@Data
@TableName("biz_project_risk_management")
public class ProjectRiskManagement extends BizProjectBaseEntity {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField("project_id")
    private Long projectId;
    /**
     * 风险名称
     */
    @ApiModelProperty(value = "风险名称", hidden = false, required = false)
    @HistoryFieldLog("风险名称")
    @Excel(name = "风险名称")
    @TableField("risk_name")
    private String riskName;
    /**
     * 风险描述
     */
    @ApiModelProperty(value = "风险描述", hidden = false, required = false)
    @HistoryFieldLog("风险描述")
    @Excel(name = "风险描述")
    @TableField("risk_describe")
    private String riskDescribe;
    /**
     * 是否删除 0-否 2-是
     */
    @ApiModelProperty(value = "是否删除 0-否 2-是", hidden = false, required = false)
    @HistoryFieldLog("是否删除 0-否 2-是")
    @Excel(name = "是否删除 0-否 2-是")
    @TableField("deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;
    /**
     * 创建人
     */
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

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

}
