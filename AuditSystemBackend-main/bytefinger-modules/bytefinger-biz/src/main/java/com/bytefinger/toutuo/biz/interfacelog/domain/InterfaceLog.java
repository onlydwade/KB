package com.bytefinger.toutuo.biz.interfacelog.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 接口日志记录表
 * </p>
 *
 * @author chengwei
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_interface_log")
@ApiModel(value = "interfaceLog对象", description = "接口日志记录表")
public class InterfaceLog {

    @ApiModelProperty(value = "日志主键", hidden = false, required = false)
    @JSONField(name = "id", serialize = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "方法名称", hidden = false, required = false)
    @JSONField(name = "name", serialize = true)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "请求方式", hidden = false, required = false)
    @JSONField(name = "requestMethod", serialize = true)
    @TableField("request_method")
    private String requestMethod;


    @ApiModelProperty(value = "请求URL", hidden = false, required = false)
    @JSONField(name = "url", serialize = true)
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "请求参数", hidden = false, required = false)
    @JSONField(name = "param", serialize = true)
    @TableField("param")
    private String param;

    @ApiModelProperty(value = "返回参数", hidden = false, required = false)
    @JSONField(name = "jsonResult", serialize = true)
    @TableField("json_result")
    private String jsonResult;

    @ApiModelProperty(value = "状态（1成功，0失败， 2异常）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "重调次数", hidden = false, required = false)
    @JSONField(name = "count", serialize = true)
    @TableField("count")
    private Integer count;

    @ApiModelProperty(value = "错误消息", hidden = false, required = false)
    @JSONField(name = "errorMsg", serialize = true)
    @TableField("error_msg")
    private String errorMsg;


    @ApiModelProperty(value = "'创建时间'", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @ApiModelProperty(value = "业务类型（ReimbursementApplication:报销申请）", hidden = false, required = false)
    @JSONField(name = "businessType", serialize = true)
    @TableField("business_type")
    private String businessType;

    @ApiModelProperty(value = "业务Id", hidden = false, required = false)
    @JSONField(name = "businessId", serialize = true)
    @TableField("business_id")
    private String businessId;

}