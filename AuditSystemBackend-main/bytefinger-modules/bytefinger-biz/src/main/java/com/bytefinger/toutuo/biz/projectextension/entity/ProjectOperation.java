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
 * 拓后运营管理
 *
 * @author ycj
 * @email
 * @date 2023-03-14 11:05:23
 */
@Data
@TableName("biz_project_operation")
public class ProjectOperation extends BaseEntity {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;
    /**
     * 总户数
     */
    @ApiModelProperty(value = "总户数", hidden = false, required = false)
    @HistoryFieldLog("总户数")
    @Excel(name = "总户数")
    @TableField(value = "househoId_sum")
    private Integer househoidSum;
    /**
     * 占地面积
     */
    @ApiModelProperty(value = "占地面积", hidden = false, required = false)
    @HistoryFieldLog("占地面积")
    @Excel(name = "占地面积")
    @TableField(value = "floor_space")
    private String floorSpace;
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

}
