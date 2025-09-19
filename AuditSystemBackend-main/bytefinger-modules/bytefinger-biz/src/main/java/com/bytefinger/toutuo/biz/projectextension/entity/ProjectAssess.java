package com.bytefinger.toutuo.biz.projectextension.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessSuggestVo;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目评估
 *
 * @author ycj
 * @email
 * @date 2023-03-16 10:18:21
 */
@Data
@TableName("biz_project_assess")
public class ProjectAssess extends BizProjectBaseEntity {

    /**
     * 评估日期
     */
    @ApiModelProperty(value = "评估日期", hidden = false, required = false)
    @HistoryFieldLog("评估日期")
    @Excel(name = "评估日期")
    @TableField(value = "assess_time", fill = FieldFill.INSERT)
    private Date assessTime;
    /**
     * 经营评估期限
     */
    @ApiModelProperty(value = "经营评估期限", hidden = false, required = false)
    @HistoryFieldLog("经营评估期限")
    @Excel(name = "经营评估期限")
    @TableField("assess_deadline")
    private String assessDeadline;
    /**
     * 评估人id
     */
    @ApiModelProperty(value = "评估人id", hidden = false, required = false)
    @HistoryFieldLog("评估人id")
    @Excel(name = "评估人id")
    @TableField(value = "assess_user_id",fill = FieldFill.INSERT)
    private Long assessUserId;
    /**
     * 评估人名称
     */
    @ApiModelProperty(value = "评估人名称", hidden = false, required = false)
    @HistoryFieldLog("评估人名称")
    @Excel(name = "评估人名称")
    @TableField(value = "assess_user_name")
    private String assessUserName;
    /**
     * 评估标题
     */
    @ApiModelProperty(value = "评估标题", hidden = false, required = false)
    @HistoryFieldLog("评估标题")
    @Excel(name = "评估标题")
    @TableField("assess_title")
    private String assessTitle;
    /**
     * 评估阶段(1.月份2.季度,3.半年,4.年度)
     */
    @ApiModelProperty(value = "评估阶段(1.月份2.季度,3.半年,4.年度)", hidden = false, required = false)
    @HistoryFieldLog("评估阶段(1.月份2.季度,3.半年,4.年度)")
    @Excel(name = "评估阶段(1.月份2.季度,3.半年,4.年度)")
    @TableField("assess_stage")
    @Dict(type = "BAO_GAO_ZHOU_QI_LEI_XING")
    private String assessStage;

    /**
     * 评估阶段(1.月份2.季度,3.半年,4.年度)
     */
    @ApiModelProperty(value = "评估阶段(1.月份2.季度,3.半年,4.年度)", hidden = false, required = false)
    @HistoryFieldLog("评估阶段(1.月份2.季度,3.半年,4.年度)")
    @Excel(name = "评估阶段(1.月份2.季度,3.半年,4.年度)")
    @TableField("assess_stage_details")
    @Dict(ptype = "BAO_GAO_ZHOU_QI_LEI_XING")
    private String assessStageDetails;
    /**
     * 评估年份
     */
    @ApiModelProperty(value = "评估年份", hidden = false, required = false)
    @HistoryFieldLog("评估年份")
    @Excel(name = "评估年份")
    @TableField("assess_year")
    private String assessYear;
    /**
     * 评估状态(0草稿,1.已完成)
     */
    @ApiModelProperty(value = "评估状态(0草稿,1.已完成)", hidden = false, required = false)
    @HistoryFieldLog("评估状态(0草稿,1.已完成)")
    @Excel(name = "评估状态(0草稿,1.已完成)")
    @TableField("assess_state")
    private Integer assessState;
    /**
     * 是否需要下达(0.否,1.是)
     */
    @ApiModelProperty(value = "是否需要下达(0.否,1.是)", hidden = false, required = false)
    @HistoryFieldLog("是否需要下达(0.否,1.是)")
    @Excel(name = "是否需要下达(0.否,1.是)")
    @TableField("transmit_state")
    private Integer transmitState;
    /**
     * 是否已下达干预方案(0.否,1.是)
     */
    @ApiModelProperty(value = "是否已下达干预方案(0.否,1.是)", hidden = false, required = false)
    @HistoryFieldLog("是否已下达干预方案(0.否,1.是)")
    @Excel(name = "是否已下达干预方案(0.否,1.是)")
    @TableField("transmit_already_state")
    private Integer transmitAlreadyState;
    /**
     * 方案下达期限
     */
    @ApiModelProperty(value = "方案下达期限", hidden = false, required = false)
    @HistoryFieldLog("方案下达期限")
    @Excel(name = "方案下达期限")
    @TableField(value = "transmit_deadline",updateStrategy=FieldStrategy.IGNORED)
    private Date transmitDeadline;
    /**
     * 法律风险描述
     */
    @ApiModelProperty(value = "法律风险描述", hidden = false, required = false)
    @HistoryFieldLog("法律风险描述")
    @Excel(name = "法律风险描述")
    @TableField("risk_describe_law")
    private String riskDescribeLaw;
    /**
     * 隐患风险描述
     */
    @ApiModelProperty(value = "隐患风险描述", hidden = false, required = false)
    @HistoryFieldLog("隐患风险描述")
    @Excel(name = "隐患风险描述")
    @TableField("risk_describe_hidden")
    private String riskDescribeHidden;
    /**
     * 合同风险描述
     */
    @ApiModelProperty(value = "合同风险描述", hidden = false, required = false)
    @HistoryFieldLog("合同风险描述")
    @Excel(name = "合同风险描述")
    @TableField("risk_describe_contract")
    private String riskDescribeContract;
    /**
     * 法律改进建议
     */
    @ApiModelProperty(value = "法律改进建议", hidden = false, required = false)
    @HistoryFieldLog("法律改进建议")
    @Excel(name = "法律改进建议")
    @TableField("improve_suggest_law")
    private String improveSuggestLaw;
    /**
     * 隐患改进建议
     */
    @ApiModelProperty(value = "隐患改进建议", hidden = false, required = false)
    @HistoryFieldLog("隐患改进建议")
    @Excel(name = "隐患改进建议")
    @TableField("improve_suggest_hidden")
    private String improveSuggestHidden;
    /**
     * 合同改进建议
     */
    @ApiModelProperty(value = "合同改进建议", hidden = false, required = false)
    @HistoryFieldLog("合同改进建议")
    @Excel(name = "合同改进建议")
    @TableField("improve_suggest_contract")
    private String improveSuggestContract;
    /**
     * 是否删除(0否,2是)
     */
    @ApiModelProperty(value = "是否删除(0否,2是)", hidden = false, required = false)
    @HistoryFieldLog("是否删除(0否,2是)")
    @Excel(name = "是否删除(0否,2是)")
    @TableField("deleted")
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


//    @ApiModelProperty(value = "附件", hidden = false, required = false)
//    @HistoryFieldLog("附件")
//    @Excel(name = "附件")
//    @TableField(exist = false)
//    private List<ProjectDocumentTemplate> templates;

    /**
     * 经营评估附件id
     */
    @ApiModelProperty(value = "经营评估附件id", hidden = false, required = false)
    @HistoryFieldLog("经营评估附件id")
    @Excel(name = "经营评估附件id")
    @TableField("assess_file_id")
    private String assessFileId;

    /**
     * 其他附件id
     */
    @ApiModelProperty(value = "其他附件id", hidden = false, required = false)
    @HistoryFieldLog("其他附件id")
    @Excel(name = "其他附件id")
    @TableField("rest_file_id")
    private String restFileId;

//    @ApiModelProperty(value = "经营评估附件id", hidden = false, required = false)
//    @HistoryFieldLog("经营评估附件id")
//    @Excel(name = "经营评估附件id")
//    @TableField(exist = false)
//    private List<String> assessFileIds;
//
//    @ApiModelProperty(value = "其他附件id", hidden = false, required = false)
//    @HistoryFieldLog("其他附件id")
//    @Excel(name = "其他附件id")
//    @TableField(exist = false)
//    private List<String> restFileIds;

    @ApiModelProperty(value = "风险评估", hidden = false, required = false)
    @HistoryFieldLog("风险评估")
    @Excel(name = "风险评估")
    @TableField(exist = false)
    private List<ProjectAssessSuggestVo> projectAssessSuggestVos;
}
