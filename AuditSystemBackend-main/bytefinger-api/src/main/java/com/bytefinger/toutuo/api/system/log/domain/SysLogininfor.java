package com.bytefinger.toutuo.api.system.log.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 系统访问记录
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_logininfor")
@ApiModel(value = "SysLogininfor对象", description = "系统访问记录")
public class SysLogininfor {

    @ApiModelProperty(value = "访问ID", hidden = false, required = false)
    @JSONField(name = "infoId", serialize = true)
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    @ApiModelProperty(value = "用户账号", hidden = false, required = false)
    @JSONField(name = "userName", serialize = true)
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "登录IP地址", hidden = false, required = false)
    @JSONField(name = "ipaddr", serialize = true)
    @TableField("ipaddr")
    private String ipaddr;

    @ApiModelProperty(value = "登录状态（0成功 1失败）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "提示信息", hidden = false, required = false)
    @JSONField(name = "msg", serialize = true)
    @TableField("msg")
    private String msg;

    @ApiModelProperty(value = "访问时间", hidden = false, required = false)
    @JSONField(name = "accessTime", serialize = true)
    @TableField("access_time")
    private Date accessTime;

}