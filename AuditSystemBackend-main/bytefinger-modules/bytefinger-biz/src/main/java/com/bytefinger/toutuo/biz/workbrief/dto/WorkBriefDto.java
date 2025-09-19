package com.bytefinger.toutuo.biz.workbrief.dto;

import com.bytefinger.toutuo.biz.workbrief.domain.KeyCustomerFollow;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectUndertaking;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectWinBidder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作简报 详情 实体
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@ApiModel(value = "工作简报-详情-实体", description = "工作简报-详情-实体")
public class WorkBriefDto implements Serializable {

    @ApiModelProperty(value = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "工作简报编码")
    private String code;

    @ApiModelProperty(value = "工作简报标题")
    private String title;

    @ApiModelProperty(value = "汇报单位")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    @ApiModelProperty(value = "工作简报状态")
    private Integer approveStatus;

    @ApiModelProperty(value = "简报主题色")
    private String theme;

    @ApiModelProperty(value = "所属年份")
    private String year;

    @ApiModelProperty(value = "拓展合同(份)")
    private Integer contractCount;

    @ApiModelProperty(value = "拓展合同总金额（万）")
    private String totalAmount;

    @ApiModelProperty(value = "拓展合同年度总金额(万)")
    private String totalYearAmount;

    @ApiModelProperty(value = "当年转化收入")
    private String yearConversionAmount;

    @ApiModelProperty(value = "拓展合同(份)")
    private Integer renewalContractCount;

    @ApiModelProperty(value = "拓展合同总金额（万）")
    private String renewalTotalAmount;

    @ApiModelProperty(value = "拓展合同年度总金额(万)")
    private String renewalTotalYearAmount;

    @ApiModelProperty(value = "审批人id")
    private Long approverId;

    @ApiModelProperty(value = "发送对象")
    private List<String> pushUserList;

    @ApiModelProperty(value = "近期项目中标情况")
    private List<RecentProjectWinBidder> winBidderList;

    @ApiModelProperty(value = "近期项目承接情况")
    private List<RecentProjectUndertaking> undertakingList;

    @ApiModelProperty(value = "重点项目跟进情况")
    private List<KeyCustomerFollow> customerFollowList;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "汇报单位名称")
    private String deptName;

    @ApiModelProperty(value = "组织机构层级")
    private Integer deptLevel;

}

