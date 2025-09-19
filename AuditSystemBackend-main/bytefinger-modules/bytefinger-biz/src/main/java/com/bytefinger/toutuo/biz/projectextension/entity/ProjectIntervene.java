package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.domain.TimeBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拓后运营项目干预
 *
 * @author ycj
 * @email
 * @date 2023-03-21 16:14:17
 */
@Data
@TableName("biz_project_intervene")
public class ProjectIntervene extends BizProjectBaseEntity{
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @HistoryFieldLog("项目id")
    @Excel(name = "项目id")
    @TableField("project_id")
    private Long projectId;
    /**
     * 评估id
     */
    @ApiModelProperty(value = "评估id", hidden = false, required = false)
    @HistoryFieldLog("评估id")
    @Excel(name = "评估id")
    @TableField("assess_id")
    private Long assessId;

    /**
     * 评估标题
     */
    @ApiModelProperty(value = "评估标题", hidden = false, required = false)
    @HistoryFieldLog("评估标题")
    @Excel(name = "评估标题")
    @TableField(exist = false)
    private String assessTitle;
    /**
     * 干预方案名称
     */
    @ApiModelProperty(value = "干预方案名称", hidden = false, required = false)
    @HistoryFieldLog("干预方案名称")
    @Excel(name = "干预方案名称")
    @TableField("intervene_scheme_name")
    private String interveneSchemeName;
    /**
     * 干预实施期限
     */
    @ApiModelProperty(value = "干预实施期限", hidden = false, required = false)
    @HistoryFieldLog("干预实施期限")
    @Excel(name = "干预实施期限")
    @TableField(value = "intervene_deadline")
    private String interveneDeadline;
    /**
     * 方案是否被实施（0否，2是)
     */
    @ApiModelProperty(value = "方案是否被实施（0否，2是)", hidden = false, required = false)
    @HistoryFieldLog("方案是否被实施（0否，2是)")
    @Excel(name = "方案是否被实施（0否，2是)")
    @TableField("scheme_isnot_embodiment")
    private Integer schemeIsnotEmbodiment;

    /**
     * 干预执行状态
     */
    @ApiModelProperty(value = "干预执行状态(0干预待执行，1待检查，2执行不通过，3已完成)", hidden = false, required = false)
    @HistoryFieldLog("干预执行状态(0干预待执行，1待检查，2执行不通过，3已完成)")
    @Excel(name = "干预执行状态")
    @TableField("intervene_state")
    private Integer interveneState;

    /**
     * 干预执行状态
     */
    @ApiModelProperty(value = "(0未检查，1一级检查，2二级检查)", hidden = false, required = false)
    @HistoryFieldLog("(0未检查，1一级检查，2二级检查)")
    @Excel(name = "(0未检查，1一级检查，2二级检查)")
    @TableField("examine_level")
    private Integer examineLevel;
    /**
     * 实施反馈日期
     */
    @ApiModelProperty(value = "实施反馈日期", hidden = false, required = false)
    @HistoryFieldLog("实施反馈日期")
    @Excel(name = "实施反馈日期")
    @TableField("feedback_time")
    private Date feedbackTime;
    /**
     * 关闭日期
     */
    @ApiModelProperty(value = "关闭日期", hidden = false, required = false)
    @HistoryFieldLog("关闭日期")
    @Excel(name = "关闭日期")
    @TableField("close_time")
    private Date closeTime;
    /**
     * 执行不通过意见
     */
    @ApiModelProperty(value = "执行不通过意见", hidden = false, required = false)
    @HistoryFieldLog("执行不通过意见")
    @Excel(name = "执行不通过意见")
    @TableField("execute_not_opinion")
    private String executeNotOpinion;
    /**
     * 执行情况
     */
    @ApiModelProperty(value = "执行情况", hidden = false, required = false)
    @HistoryFieldLog("执行情况")
    @Excel(name = "执行情况")
    @TableField("execute_condition")
    private String executeCondition;
    /**
     * 经营干预
     */
    @ApiModelProperty(value = "经营干预", hidden = false, required = false)
    @HistoryFieldLog("经营干预")
    @Excel(name = "经营干预")
    @TableField("manage_intervene")
    private String manageIntervene;
    /**
     * 风险干预
     */
    @ApiModelProperty(value = "风险干预", hidden = false, required = false)
    @HistoryFieldLog("风险干预")
    @Excel(name = "风险干预")
    @TableField("risk_intervene")
    private String riskIntervene;
    /**
     * 对客服务干预
     */
    @ApiModelProperty(value = "对客服务干预", hidden = false, required = false)
    @HistoryFieldLog("对客服务干预")
    @Excel(name = "对客服务干预")
    @TableField("customer_service_intervene")
    private String customerServiceIntervene;
    /**
     * 品质干预
     */
    @ApiModelProperty(value = "品质干预", hidden = false, required = false)
    @HistoryFieldLog("品质干预")
    @Excel(name = "品质干预")
    @TableField("quality_intervene")
    private String qualityIntervene;

    /**
     * 方案实施人
     */
    @ApiModelProperty(value = "方案实施人", hidden = false, required = false)
    @Excel(name = "方案实施人")
    @TableField(value ="scheme_user_id")
    private Long schemeUserId;
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


    @ApiModelProperty(value = "方案实施人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "schemeUserId", fillMethod = FillMethod.USER)
    private UserVO schemeUser;

    @ApiModelProperty(value = "方案实施人信息", hidden = false, required = false)
    @TableField(exist = false)
    private String schemeUserName;

    @ApiModelProperty(value = "项目评估信息", hidden = false, required = false)
    @TableField(exist = false)
    private ProjectAssess projectAssess;

}
