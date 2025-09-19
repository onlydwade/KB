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
 * 操作日志记录
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_oper_log")
@ApiModel(value = "SysOperLog对象", description = "操作日志记录")
public class SysOperLog {

    @ApiModelProperty(value = "日志主键", hidden = false, required = false)
    @JSONField(name = "operId", serialize = true)
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;

    @ApiModelProperty(value = "模块标题", hidden = false, required = false)
    @JSONField(name = "title", serialize = true)
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）", hidden = false, required = false)
    @JSONField(name = "businessType", serialize = true)
    @TableField("business_type")
    private Integer businessType;

    @ApiModelProperty(value = "方法名称", hidden = false, required = false)
    @JSONField(name = "method", serialize = true)
    @TableField("method")
    private String method;

    @ApiModelProperty(value = "请求方式", hidden = false, required = false)
    @JSONField(name = "requestMethod", serialize = true)
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）", hidden = false, required = false)
    @JSONField(name = "operatorType", serialize = true)
    @TableField("operator_type")
    private Integer operatorType;

    @ApiModelProperty(value = "操作人员", hidden = false, required = false)
    @JSONField(name = "operName", serialize = true)
    @TableField("oper_name")
    private String operName;

    @ApiModelProperty(value = "部门名称", hidden = false, required = false)
    @JSONField(name = "deptName", serialize = true)
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "请求URL", hidden = false, required = false)
    @JSONField(name = "operUrl", serialize = true)
    @TableField("oper_url")
    private String operUrl;

    @ApiModelProperty(value = "主机地址", hidden = false, required = false)
    @JSONField(name = "operIp", serialize = true)
    @TableField("oper_ip")
    private String operIp;

    @ApiModelProperty(value = "操作地点", hidden = false, required = false)
    @JSONField(name = "operLocation", serialize = true)
    @TableField("oper_location")
    private String operLocation;

    @ApiModelProperty(value = "请求参数", hidden = false, required = false)
    @JSONField(name = "operParam", serialize = true)
    @TableField("oper_param")
    private String operParam;

    @ApiModelProperty(value = "返回参数", hidden = false, required = false)
    @JSONField(name = "jsonResult", serialize = true)
    @TableField("json_result")
    private String jsonResult;

    @ApiModelProperty(value = "操作状态（0正常 1异常）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "错误消息", hidden = false, required = false)
    @JSONField(name = "errorMsg", serialize = true)
    @TableField("error_msg")
    private String errorMsg;

    @ApiModelProperty(value = "操作时间", hidden = false, required = false)
    @JSONField(name = "operTime", serialize = true)
    @TableField("oper_time")
    private Date operTime;

}