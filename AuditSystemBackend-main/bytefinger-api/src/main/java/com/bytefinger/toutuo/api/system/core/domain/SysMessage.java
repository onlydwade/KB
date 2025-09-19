package com.bytefinger.toutuo.api.system.core.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统个人消息
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_message")
@ApiModel(value = "SysMessage对象", description = "系统个人消息")
public class SysMessage extends BaseEntity {

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "用户id", hidden = false, required = false)
    @JSONField(name = "userId", serialize = true)
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "打开状态 0-未打开 1-已打开", hidden = false, required = false)
    @JSONField(name = "openStatus", serialize = true)
    @TableField("open_status")
    private Integer openStatus;

    @ApiModelProperty(value = "打开时间", hidden = false, required = false)
    @JSONField(name = "openTime", serialize = true)
    @TableField("open_time")
    private Date openTime;

    @ApiModelProperty(value = "消息类型", hidden = false, required = false)
    @JSONField(name = "messageType", serialize = true)
    @TableField("message_type")
    private String messageType;

    @ApiModelProperty(value = "消息图标 or 消息图片", hidden = false, required = false)
    @JSONField(name = "icon", serialize = true)
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "消息标题", hidden = false, required = false)
    @JSONField(name = "title", serialize = true)
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "消息内容", hidden = false, required = false)
    @JSONField(name = "message", serialize = true)
    @TableField("message")
    private String message;

    @ApiModelProperty(value = "模块", hidden = false, required = false)
    @JSONField(name = "module", serialize = true)
    @TableField("module")
    private String module;

    @ApiModelProperty(value = "模块对应数据id", hidden = false, required = false)
    @JSONField(name = "moduleId", serialize = true)
    @TableField("module_id")
    private Long moduleId;

    @TableField(exist = false)
    private String sendChannels;

}