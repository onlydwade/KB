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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拓后运营-服务满意度信息
 *
 * @author ycj
 * @email
 * @date 2023-03-20 11:40:08
 */
@Data
@TableName("biz_project_serve_satisfaction")
public class ProjectServeSatisfaction extends BaseEntity {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField(value = "project_id")
    private Long projectId;
    /**
     * 满意度类型(1.合同相对方满意度,2.业主服务满意度)
     */
    @ApiModelProperty(value = "满意度类型(1.合同相对方满意度,2.业主服务满意度)", hidden = false, required = false)
    @HistoryFieldLog("满意度类型(1.合同相对方满意度,2.业主服务满意度)")
    @Excel(name = "满意度类型(1.合同相对方满意度,2.业主服务满意度)")
    @TableField("satisfaction_type")
    private Integer satisfactionType;
    /**
     * 满意度说明
     */
    @ApiModelProperty(value = "满意度说明", hidden = false, required = false)
    @HistoryFieldLog("满意度说明")
    @Excel(name = "满意度说明")
    @TableField("satisfaction_explain")
    private String satisfactionExplain;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份", hidden = false, required = false)
    @HistoryFieldLog("年份")
    @Excel(name = "年份")
    @TableField("year")
    private String year;
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

}
