package com.bytefinger.toutuo.biz.customer.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.domain.TimeBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 业务客户表
 * </p>
 *
 * @author patrick
 * @since 2022-10-25
 */
@Data
@TableName("biz_customer")
@ApiModel(value = "Customer对象", description = "业务客户表")
public class Customer extends TimeBaseEntity {
    @ApiModelProperty(value = "客户名称", hidden = false, required = false)
    @TableField("customer_name")
    @HistoryFieldLog("客户名称")
    @Size(max = 100, message = "客户名称过长")
    @Excel(name = "客户全称", sort = 1)
    private String customerName;

    @ApiModelProperty(value = "客户编码", hidden = false, required = false)
    @TableField("customer_no")
    @HistoryFieldLog("客户编码")
    @Excel(name = "客户编码", sort = 0)
    private String customerNo;

    @ApiModelProperty(value = "客户社会信用统一代码", hidden = false, required = false)
    @TableField("customer_company_no")
    @HistoryFieldLog("客户社会信用统一代码")
    @Excel(name = "社会信用统一代码", sort = 13)
    private String customerCompanyNo;

    @ApiModelProperty(value = "客户分类（枚举）", hidden = false, required = false)
    @TableField("customer_type")
    @HistoryFieldLog("客户分类")
    @Dict(type = "KE_HU_FEN_LEI")
    @Excel(name = "客户类型",sort = 4)
    private String customerType;

    @ApiModelProperty(value = "合作类型", hidden = false, required = false)
    @TableField("cooperation_type")
    @HistoryFieldLog("合作类型")
    @Dict(type = "HE_ZUO_LEI_XING")
    @Excel(name = "合作类型", sort = 5)
    private String cooperationType;

    @ApiModelProperty(value = "客户行业（枚举）", hidden = false, required = false)
    @TableField("customer_industry")
    @Dict(type = "SUO_SHU_XING_YE")
    @HistoryFieldLog("客户行业")
    @Excel(name = "所属行业", sort = 6)
    private String customerIndustry;

    @ApiModelProperty(value = "跟进类型（枚举）", hidden = false, required = false)
    @TableField("customer_level")
    @HistoryFieldLog("跟进类型")
    @Dict(type = "KE_HU_DENG_JI")
    @Excel(name = "跟进类型", sort = 2)
    private String customerLevel;

    @ApiModelProperty(value = "省编号", hidden = false, required = false)
    @TableField("province_code")
    @HistoryFieldLog("省编号")
    @Cnarea(type = CnareaEnum.PROVINCE)
    @Excel(name = "客户所属省份", sort = 9)
    private Long provinceCode;

    @ApiModelProperty(value = "城市编号", hidden = false, required = false)
    @TableField("city_code")
    @HistoryFieldLog("城市编号")
    @Cnarea(type = CnareaEnum.CITY)
    @Excel(name = "客户所属城市", sort = 10)
    private Long cityCode;

    @ApiModelProperty(value = "区编号", hidden = false, required = false)
    @TableField("area_code")
    @HistoryFieldLog("区编号")
    @Cnarea(type = CnareaEnum.AREA)
    @Excel(name = "客户所属区/县", sort = 11)
    private Long areaCode;

    @ApiModelProperty(value = "详细地址", hidden = false, required = false)
    @TableField("address")
    @HistoryFieldLog("详细地址")
    @Excel(name = "客户详细地址", sort = 12)
    private String address;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @TableField("remark")
    @HistoryFieldLog("备注")
    @Excel(name = "备注", sort = 14)
    private String remark;

    @ApiModelProperty(value = "关键词", hidden = false, required = false)
    @TableField("keywords")
    @HistoryFieldLog("关键词")
    @Excel(name = "客户标签",sort = 7)
    private String keywords;

    @ApiModelProperty(value = "法人代表", hidden = false, required = false)
    @TableField("legal_entity")
    private String legalEntity;

    @ApiModelProperty(value = "注册资本", hidden = false, required = false)
    @TableField("registered_capital")
    private String registeredCapital;

    @ApiModelProperty(value = "人员规模", hidden = false, required = false)
    @TableField("personnel_size")
    private String personnelSize;

    @ApiModelProperty(value = "成立时间", hidden = false, required = false)
    @TableField("establishment_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date establishmentDate;

    @ApiModelProperty(value = "公司官网", hidden = false, required = false)
    @TableField("website")
    private String website;

    @ApiModelProperty(value = "注册地址", hidden = false, required = false)
    @TableField("registered_address")
    private String registeredAddress;

    @ApiModelProperty(value = "公司简介", hidden = false, required = false)
    @TableField("company_introduction")
    private String companyIntroduction;

    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Excel(name = "创建人", dataType = Excel.DataType.USER, sort = 15)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间", sort = 16)
    private Date createTime;

    @ApiModelProperty(value = "最后修改人id", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "最后修改人", dataType = Excel.DataType.USER, sort = 17)
    private Long updateUserId;

    @ApiModelProperty(value = "最后修改时间", hidden = false, required = false)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "最后修改时间", sort = 18)
    private Date updateTime;

    /**
     * 最新跟进时间
     */
    @ApiModelProperty(value = "最新跟进时间", hidden = false, required = false)
    @TableField("follow_time")
    @HistoryFieldLog("最新跟进时间")
    private Date followTime;

    @ApiModelProperty(value = "删除", hidden = false, required = false)
    @TableField("deleted")
    @TableLogic(value = Deleted.N, delval = Deleted.Y)
    private Integer deleted;

    @ApiModelProperty(value = "跟进人", hidden = false, required = false)
    @TableField(value = "follow_user_id")
    @HistoryFieldLog(value = "客户跟进人", joinField = "followUserVO")
    @Excel(name = "客户跟进人", dataType = Excel.DataType.USER, sort = 8)
    private Long followUserId;

    @ApiModelProperty(value = "跟进人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "followUserId", fillMethod = FillMethod.USER)
    private UserVO followUserVO;

    @ApiModelProperty(value = "单位负责人", hidden = false, required = false)
    @TableField(value = "owner_user_id")
    @HistoryFieldLog(value = "单位负责人", joinField = "ownerUserVO")
    private Long ownerUserId;

    @ApiModelProperty(value = "单位负责人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "ownerUserId", fillMethod = FillMethod.USER)
    private UserVO ownerUserVO;

    @ApiModelProperty(value = "维护人员", hidden = false, required = false)
    @TableField(value = "maintenance_user_id")
    @HistoryFieldLog(value = "维护人员", joinField = "maintenanceUserVO")
    private Long maintenanceUserId;

    @ApiModelProperty(value = "维护人员", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "maintenanceUserId", fillMethod = FillMethod.USER)
    private UserVO maintenanceUserVO;

    @ApiModelProperty(value = "联系人列表", hidden = false, required = false)
    @TableField(exist = false)
    private List<CustomerContacts> customerContactsList;

    @ApiModelProperty(value = "企业来源", hidden = false, required = false)
    @TableField("source")
    @HistoryFieldLog("企业来源")
    @Dict(type = "KE_HU_LAI_YUAN")
    private String source;

    @ApiModelProperty(value = "企业类型", hidden = false, required = false)
    @TableField("company_type")
    @HistoryFieldLog("企业类型")
    @Excel(name = "企业类型", sort = 3)
    @Dict(type = "QI_YE_LEI_XING")
    private String companyType;

    @ApiModelProperty(value = "是否工作简报", hidden = false, required = false)
    @TableField("work_brief")
    @Dict(type = "SHI_FOU")
    @HistoryFieldLog("是否工作简报")
    private String workBrief;


}