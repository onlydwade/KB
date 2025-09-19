package com.bytefinger.toutuo.biz.workbrief.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作简报
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_work_brief")
@ApiModel(value = "工作简报", description = "工作简报")
public class WorkBrief extends BaseEntity {

    @ApiModelProperty(value = "工作简报编码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "工作简报标题")
    @TableField("title")
    @Excel(name = "内容",sort = 3)
    private String title;

    @ApiModelProperty(value = "汇报单位")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty(value = "简报主题色")
    @TableField("theme")
    private String theme;

    @ApiModelProperty(value = "所属年份")
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "拓展合同(份)")
    @TableField("contract_count")
    private Integer contractCount;

    @ApiModelProperty(value = "拓展合同总金额（万）")
    @TableField("total_amount")
    private String totalAmount;

    @ApiModelProperty(value = "拓展合同年度总金额(万)")
    @TableField("total_year_amount")
    private String totalYearAmount;


    @ApiModelProperty(value = "拓展合同(份)")
    @TableField("renewal_contract_count")
    private Integer renewalContractCount;

    @ApiModelProperty(value = "拓展合同总金额（万）")
    @TableField("renewal_total_amount")
    private String renewalTotalAmount;

    @ApiModelProperty(value = "拓展合同年度总金额(万)")
    @TableField("renewal_total_year_amount")
    private String renewalTotalYearAmount;


    @ApiModelProperty(value = "审批人id")
    @TableField("approver_id")
    private Long approverId;

    @ApiModelProperty(value = "审批状态")
    @TableField("approve_status")
    private Integer approveStatus;

    @ApiModelProperty(value = "推送时间")
    @TableField("push_time")
    private Date pushTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time",  fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "简报发送时间", dateFormat = "yyyy-MM-dd HH:mm:ss",sort = 4)
    private Date updateTime;

    @ApiModelProperty(value = "是否已删除")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty(value = "最后更新人名称")
    @TableField(exist = false)
    private String updateUserName;

    @ApiModelProperty(value = "推送对象")
    @TableField(exist = false)
    private String pushUserName;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    @Excel(name = "简报单位",sort = 1)
    private String deptName;

    @ApiModelProperty(value = "审批状态")
    @TableField(exist = false)
    private String approveStatusName;

    @ApiModelProperty(value = "是否推送")
    @TableField(exist = false)
    private String isPush;

    @ApiModelProperty(value = "推送对象列表")
    @TableField(exist = false)
    private List<WorkPushUserDto> pushUserList;

}

