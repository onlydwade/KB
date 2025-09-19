package com.bytefinger.toutuo.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业务基础实体
 *
 * @author pat
 * @date 2022/10/23 11:24
 **/
@Data
@ApiModel(value = "基础信息", description = "基础信息")
public class BizBaseEntity extends BizCommonBaseEntity {

    /****** 通用编号 **/
    @ApiModelProperty(value = "线索编号", hidden = false, required = false)
    @TableField("lead_no")
    @Excel(name = "线索编号")
    private String leadNo;

    @Excel(name = "关联商机编号")
    @ApiModelProperty(value = "关联商机编号", hidden = false, required = false)
    @TableField("opportunity_no")
    private String opportunityNo;

    @Excel(name = "关联项目编号")
    @ApiModelProperty(value = "关联项目编号", hidden = false, required = false)
    @TableField("project_no")
    private String projectNo;


    /****** 行政区划 **/
    @Excel(name = "省编号")
    @ApiModelProperty(value = "省编号", hidden = false, required = false)
    @HistoryFieldLog("省编号")
    @TableField("province_code")
    @NotNull(message = "请选择省份数据")
    @Cnarea(type = CnareaEnum.PROVINCE)
    private Long provinceCode;

    @Excel(name = "城市编号")
    @ApiModelProperty(value = "城市编号", hidden = false, required = false)
    @HistoryFieldLog("城市编号")
    @TableField("city_code")
    @Cnarea(type = CnareaEnum.CITY)
    @NotNull(message = "请选择城市数据")
    private Long cityCode;

    @Excel(name = "区编号")
    @ApiModelProperty(value = "区编号", hidden = false, required = false)
    @HistoryFieldLog("区编号")
    @TableField("area_code")
    @Cnarea(type = CnareaEnum.AREA)
    private Long areaCode;

    @Excel(name = "街道编号")
    @ApiModelProperty(value = "街道编号", hidden = false, required = false)
    @HistoryFieldLog("街道编号")
    @TableField("street_code")
    @Cnarea(type = CnareaEnum.STREET)
    private Long streetCode;

    @Excel(name = "街道政府编码")
    @ApiModelProperty(value = "街道政府编码", hidden = false, required = false)
    @HistoryFieldLog("街道政府编码")
    @TableField("street_zipcode")
    private String streetZipcode;

    @Excel(name = "详细地址")
    @ApiModelProperty(value = "详细地址", hidden = false, required = false)
    @HistoryFieldLog("详细地址")
    @TableField("address")
    private String address;


    /****** 通用枚举 **/
    @Excel(name = "关联信息")
    @ApiModelProperty(value = "关联信息", hidden = false, required = false)
    @HistoryFieldLog("关联信息")
    @TableField("info_join")
    private String infoJoin;

    @Excel(name = "信息来源")
    @ApiModelProperty(value = "信息来源(枚举)", hidden = false, required = false)
    @HistoryFieldLog("信息来源")
    @TableField("info_source")
    @Dict(type = "SHANG_JI_LAI_YUAN")
    private String infoSource;

    @Excel(name = "收费模式")
    @ApiModelProperty(value = "收费模式(枚举)", hidden = false, required = false)
    @HistoryFieldLog("收费模式")
    @TableField("charges_mode")
    @Dict(type = "SHOU_FEI_XING_SHI")
    private String chargesMode;

    @Excel(name = "业态")
    @ApiModelProperty(value = "业态(枚举)", hidden = false, required = false)
    @HistoryFieldLog("业态")
    @TableField("business_type")
    @Dict(type = "YE_TAI")
    private String businessType;

    @Excel(name = "业态备注说明")
    @ApiModelProperty(value = "业态备注说明(枚举)", hidden = false, required = false)
    @HistoryFieldLog("业态备注说明")
    @TableField("business_sub_type")
    @Dict(ptype = "YE_TAI")
    private String businessSubType;

    @Excel(name = "分发形式")
    @ApiModelProperty(value = "分发形式（枚举）", hidden = false, required = false)
    @HistoryFieldLog("分发形式")
    @TableField("dispatch_type")
    @Dict(type = "FEN_FA_XING_SHI")
    private String dispatchType;

    @Excel(name = "合作模式")
    @ApiModelProperty(value = "合作模式（枚举）", hidden = false, required = false)
    @HistoryFieldLog("合作模式")
    @TableField("cooperation_mode")
    @Dict(type = "HE_ZUO_MO_SHI")
    private String cooperationMode;

    @Excel(name = "项目类型")
    @ApiModelProperty(value = "项目类型（枚举）", hidden = false, required = false)
    @HistoryFieldLog("项目类型")
    @TableField("project_type")
    @Dict(type = "SHANG_JI_LEI_XING")
    private String projectType;


    /****** 通用业务字段 -- 招标信息 **/
    @Excel(name = "招标单位")
    @ApiModelProperty(value = "招标单位", hidden = false, required = false)
    @HistoryFieldLog("招标单位")
    @TableField("biding_company")
    private String bidingCompany;

    @Excel(name = "招标单位联系人")
    @ApiModelProperty(value = "招标单位联系人", hidden = false, required = false)
    @HistoryFieldLog("招标单位联系人")
    @TableField("biding_company_contact")
    private String bidingCompanyContact;

    @Excel(name = "招标单位联系方式")
    @ApiModelProperty(value = "招标单位联系方式", hidden = false, required = false)
    @HistoryFieldLog("招标单位联系方式")
    @TableField("biding_company_phone")
    private String bidingCompanyPhone;

    @Excel(name = "招标编号")
    @ApiModelProperty(value = "招标编号", hidden = false, required = false)
    @HistoryFieldLog("招标编号")
    @TableField("biding_no")
    private String bidingNo;

    @Excel(name = "招标方式")
    @ApiModelProperty(value = "招标方式（枚举）", hidden = false, required = false)
    @HistoryFieldLog("招标方式")
    @TableField("biding_mode")
    @Dict(type = "ZHAO_BIAO_FANG_SHI")
    private String bidingMode;

    @Excel(name = "招标代理机构")
    @ApiModelProperty(value = "招标代理机构", hidden = false, required = false)
    @HistoryFieldLog("招标代理机构")
    @TableField("biding_agency")
    private String bidingAgency;

    @Excel(name = "招标代理机构联系人")
    @ApiModelProperty(value = "招标代理机构联系人", hidden = false, required = false)
    @HistoryFieldLog("招标代理机构联系人")
    @TableField("biding_agency_contact")
    private String bidingAgencyContact;

    @Excel(name = "招标代理机构联系方式")
    @ApiModelProperty(value = "招标代理机构联系方式", hidden = false, required = false)
    @HistoryFieldLog("招标代理机构联系方式")
    @TableField("biding_agency_phone")
    private String bidingAgencyPhone;

    @Excel(name = "建筑面积")
    @ApiModelProperty(value = "建筑面积", hidden = false, required = false)
    @HistoryFieldLog("建筑面积")
    @TableField("construction_area")
    @DecimalMin(value = "1", message = "最小金额不得低于1平米")
    @DecimalMax(value = "99999999.99", message = "最大金额不得大于8位数")
    private BigDecimal constructionArea;

    @Excel(name = "发布日期")
    @ApiModelProperty(value = "发布日期", hidden = false, required = false)
    @HistoryFieldLog("发布日期")
    @TableField("release_date")
    private Date releaseDate;

    @Excel(name = "预计进场时间")
    @ApiModelProperty(value = "预计进场时间", hidden = false, required = false)
    @HistoryFieldLog("预计进场时间")
    @TableField("approach_time")
    private Date approachTime;

    @Excel(name = "预计合同金额")
    @ApiModelProperty(value = "预计合同金额", hidden = false, required = false)
    @HistoryFieldLog("预计合同金额")
    @TableField("biding_budget")
    private BigDecimal bidingBudget;

    @Excel(name = "中标服务费")
    @ApiModelProperty(value = "中标服务费", hidden = false, required = false)
    @HistoryFieldLog("中标服务费")
    @TableField("bided_service_charge")
    private String bidedServiceCharge;


    /****** 通用业务字段 -- 投标信息 **/
    @Excel(name = "开标时间")
    @ApiModelProperty(value = "开标时间", hidden = false, required = false)
    @HistoryFieldLog("开标时间")
    @TableField("biding_opentime")
    private Date bidingOpentime;

    @Excel(name = "投标时间")
    @ApiModelProperty(value = "投标时间", hidden = false, required = false)
    @HistoryFieldLog("投标时间")
    @TableField("biding_time")
    private Date bidingTime;

    @Excel(name = "投标结束时间")
    @ApiModelProperty(value = "投标结束时间", hidden = false, required = false)
    @HistoryFieldLog("投标结束时间")
    @TableField("biding_endtime")
    private Date bidingEndtime;

    @Excel(name = "投标开始时间")
    @ApiModelProperty(value = "投标开始时间", hidden = false, required = false)
    @HistoryFieldLog("投标开始时间")
    @TableField("biding_starttime")
    private Date bidingStarttime;


    /****** 通用业务字段 -- 合同信息 **/
    @Excel(name = "年度合同金额（元）含税")
    @ApiModelProperty(value = "年度合同金额（元）含税", hidden = false, required = false)
    @HistoryFieldLog("年度合同金额（元）含税")
    @TableField("amount_tex_year")
    private BigDecimal amountTexYear;

    @Excel(name = "年度合同金额（元） 不含税")
    @ApiModelProperty(value = "年度合同金额（元） 不含税", hidden = false, required = false)
    @HistoryFieldLog("年度合同金额（元） 不含税")
    @TableField("amount_year")
    private BigDecimal amountYear;

    @Excel(name = "合同期收入金额（元）含税")
    @ApiModelProperty(value = "合同期收入金额（元）含税", hidden = false, required = false)
    @HistoryFieldLog("合同期收入金额（元）含税")
    @TableField("amount_tex_income")
    private BigDecimal amountTexIncome;

    @Excel(name = "合同期收入税率")
    @ApiModelProperty(value = "合同期收入税率", hidden = false, required = false)
    @HistoryFieldLog("合同期收入税率")
    @TableField("amount_tex")
    private BigDecimal amountTex;

    @Excel(name = "合同期收入金额（元）不含税")
    @ApiModelProperty(value = "合同期收入金额（元）不含税", hidden = false, required = false)
    @HistoryFieldLog("合同期收入金额（元）不含税")
    @TableField("amount_income")
    private BigDecimal amountIncome;

    /****** 通用业务字段 **/
    @Excel(name = "关键词")
    @ApiModelProperty(value = "关键词", hidden = false, required = false)
    @HistoryFieldLog("关键词")
    @TableField("keywords")
    private String keywords;

    @Excel(name = "内容描述")
    @ApiModelProperty(value = "内容描述", hidden = false, required = false)
    @HistoryFieldLog("内容描述")
    @TableField("info_desc")
    private String infoDesc;

    @Excel(name = "备注")
    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @HistoryFieldLog("备注")
    @TableField("remarks")
    private String remarks;

    @Excel(name = "服务内容")
    @ApiModelProperty(value = "服务内容", hidden = false, required = false)
    @HistoryFieldLog("服务内容")
    @TableField("service_content")
    private String serviceContent;

    @Excel(name = "关联记录")
    @ApiModelProperty(value = "关联记录", hidden = false, required = false)
    @TableField("process_log")
    private String processLog;


    /****** 通用业务字段 推荐人**/
    @Excel(name = "推荐人")
    @ApiModelProperty(value = "推荐人-来源为全民营销时填写", hidden = false, required = false)
    @HistoryFieldLog("推荐人-来源为全民营销时填写")
    @TableField("referrer_contact")
    private String referrerContact;

    @Excel(name = "推荐人联系方式")
    @ApiModelProperty(value = "推荐人联系方式-来源为全民营销时填写", hidden = false, required = false)
    @HistoryFieldLog("推荐人联系方式-来源为全民营销时填写")
    @TableField("referrer_phone")
    private String referrerPhone;


    /****** 通用业务字段 客户、竞争对手**/
    @ApiModelProperty(value = "客户", hidden = false, required = false)
    @HistoryFieldLog("客户")
    @TableField("customer_id")
    private Long customerId;

    @Excel(name = "客户信息", targetAttr = "name")
    @ApiModelProperty(value = "客户信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "customerId", fillMethod = FillMethod.CUSTOMER)
    private CommonNameVO customer;

    @ApiModelProperty(value = "客户联系人信息", hidden = false, required = false)
    @TableField(exist = false)
    private List<Long> customerContactIds;

}
