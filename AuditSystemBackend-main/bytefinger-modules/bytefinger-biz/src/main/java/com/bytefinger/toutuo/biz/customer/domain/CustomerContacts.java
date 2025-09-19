package com.bytefinger.toutuo.biz.customer.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <p>
 * 联系人表
 * </p>
 *
 * @author chengwei
 * @since 2022-10-25
 */
@Data
@TableName("biz_customer_contacts")
@ApiModel(value = "联系人对象", description = "联系人表")
public class CustomerContacts extends BaseEntity {

    @ApiModelProperty(value = "客户id", hidden = false, required = false)
    @TableField("customer_id")
    @HistoryFieldLog("客户id")
    @Excel(name = "客户Id", sort = 1)
    private String customerId;

    @ApiModelProperty(value = "联络人", hidden = false, required = false)
    @TableField("contacts")
    @HistoryFieldLog("联络人")
    @Size(max = 200, message = "联络人长度过长")
    @Excel(name = "联络人", sort = 2)
    private String contacts;


    @ApiModelProperty(value = "单位负责人", hidden = false, required = false)
    @TableField("org_header")
    @HistoryFieldLog("单位负责人")
    @Excel(name = "单位负责人", sort = 3)
    private String orgHeader;

    @ApiModelProperty(value = "通讯地址", hidden = false, required = false)
    @TableField("phone_addr")
    @HistoryFieldLog("通讯地址")
    @Excel(name = "通讯地址",sort = 4)
    private String phoneAddr;

    @ApiModelProperty(value = "联系电话", hidden = false, required = false)
    @TableField("phone")
    @HistoryFieldLog("联系电话")
    @Excel(name = "联系电话", sort = 5)
    private String phone;

    @ApiModelProperty(value = "办公室电话", hidden = false, required = false)
    @TableField("office_phone")
    @HistoryFieldLog("办公室电话")
    @Excel(name = "办公室电话", sort = 6)
    private String officePhone;


    @ApiModelProperty(value = "负责采购", hidden = false, required = false)
    @TableField("buy")
    @HistoryFieldLog("负责采购")
    @Excel(name = "负责采购", sort = 7)
    private String buy;

    @ApiModelProperty(value = "负责项目", hidden = false, required = false)
    @TableField("project_count")
    @HistoryFieldLog("负责项目")
    @Excel(name = "负责项目", sort = 8)
    private Integer projectCount;


    @ApiModelProperty(value = "来源", hidden = false, required = false)
    @TableField("source")
    @HistoryFieldLog("来源")
    @Excel(name = "来源", sort = 9)
    private Integer source;


    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "最后修改人id", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "最后修改时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "删除", hidden = false, required = false)
    @TableField("deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;



}