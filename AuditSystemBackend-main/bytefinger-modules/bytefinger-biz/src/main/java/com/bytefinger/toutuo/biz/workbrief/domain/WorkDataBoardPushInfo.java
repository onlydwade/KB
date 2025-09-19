package com.bytefinger.toutuo.biz.workbrief.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
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
 * 工作简报推送对象
 * </p>
 *
 * @author lin
 * @since 2024-01-03
 */
@Data
@TableName("biz_data_board_push_info")
@ApiModel(value = "工作简报推送对象", description = "工作简报推送对象")
public class WorkDataBoardPushInfo extends BaseEntity {

    @ApiModelProperty(value = "发送对象id（用户id）")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "发送状态")
    @TableField("dept_id")
    private String deptId;

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
    private Date updateTime;

    @ApiModelProperty(value = "推送对象列表")
    @TableField(exist = false)
    private List<WorkDataBoardPushUser> pushUserList;

    @TableField(exist = false)
    private String deptName;


}

