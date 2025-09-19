package com.bytefinger.toutuo.biz.rules.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * 动作条件
 *
 * @author pat
 * @date 2022/11/01 09:34
 **/
@Data
@ApiModel(value = "动作", description = "动作条件")
public class RuleActionDTO {

    @ApiModelProperty(value = "动作")
    private String actionType;

    @ApiModelProperty(value = "发送渠道，可多渠道，枚举：GUI_ZE_ZI_DUAN")
    private List<String> sendChannels = Lists.newArrayList();

    @ApiModelProperty(value = "发送对象，可多选（枚举）：GUI_ZE_CHU_LI_DUI_XIANG")
    private List<String> sendObjects = Lists.newArrayList();

    @ApiModelProperty(value = "发送频次 1-一次性发送 2-循环发送")
    private Integer sendType;

    @ApiModelProperty(value = "如果是循环发送，是发送频次")
    private Integer sendTime;

    @ApiModelProperty(value = "任务开始时间")
    private String startTime;

    @ApiModelProperty(value = "发送单位，枚举：SHI_JIAN_ZHOU_QI")
    private String sendUnit;

    @ApiModelProperty(value = "消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "需要修改的字段，枚举")
    private String updateField;

    @ApiModelProperty(value = "需要修改的字段目标值，枚举")
    private String updateValue;

}
