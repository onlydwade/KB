package com.bytefinger.toutuo.biz.project.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocumentTemplate;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目补录
 *
 */
@Data
@TableName("biz_project_backlog")
public class ProjectBacklog extends BaseEntity {

    /**
     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 基础信息 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
     */

    @ApiModelProperty(value = "项目名称")
    @TableField(value = "project_name")
    private String projectName;

    @ApiModelProperty(value = "项目编码")
    @TableField(value = "project_no")
    private String projectNo;

    @ApiModelProperty(value = "归属人", hidden = false, required = false)
    @TableField("attributor_user_id")
    @HistoryFieldLog(value = "归属人", joinField = "attributorUser")
    @Excel(name = "归属人", dataType = Excel.DataType.USER, sort = 13)
    private Long attributorUserId;

    @ApiModelProperty(value = "归属人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "attributorUserId", fillMethod = FillMethod.USER)
    private UserVO attributorUser;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField("company_id")
    @HistoryFieldLog(value = "地区公司", joinField = "companyName")
    @Excel(name = "归属单位", dataType = Excel.DataType.DEPT, sort = 2)
    private Long companyId;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "companyId", fillMethod = FillMethod.DEPT)
    private String companyName;

    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField("region_id")
    @HistoryFieldLog(value = "所属大区", joinField = "regionName")
    @Excel(name = "大区", dataType = Excel.DataType.DEPT, sort = 1)
    private Long regionId;

    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "regionId", fillMethod = FillMethod.DEPT)
    private String regionName;

    @ApiModelProperty(value = "项目优先级")
    @Dict(type = "XIANG_MU_YOU_XIAN_JI")
    @TableField(value = "project_level")
    private String projectLevel;

    @ApiModelProperty(value = "项目类型")
    @TableField(value = "project_type")
    @Dict(type = "XIANG_MU_LEI_XING")
    private String projectType;

    @ApiModelProperty(value = "拓展模式", hidden = false, required = false)
    @TableField("expansion_mode")
    @HistoryFieldLog("拓展模式")
    @Dict(ptype = "XIANG_MU_LEI_XING")
    @Excel(name = "拓展模式", sort = 14)
    private String expansionMode;

    /**
     * 拓展模式
     */
    @ApiModelProperty(value = "拓展模式名称", hidden = false, required = false)
    @TableField(exist = false)
    private String expansionModeTitle;

    @ApiModelProperty(value = "是否招标")
    @TableField(value = "bided")
    @Dict(type = "SHI_FOU")
    private String bided;

    @ApiModelProperty(value = "合作方式")
    @TableField(value = "cooperation_mode")
    @Dict(type = "HE_ZUO_FANG_SHI")
    private String cooperationMode;

    @ApiModelProperty(value = "是否为续签项目")
    @TableField(value = "in_stock")
    @Dict(type = "SHI_FOU")
    private String inStock;

    @ApiModelProperty(value = "业态")
    @TableField(value = "business_type")
    private String businessType;

    @TableField(exist = false)
    @Excel(name = "业态", sort = 8)
    private String businessTypeStr;

    @ApiModelProperty(value = "服务内容")
    @TableField(value = "service_content")
    @Dict(type = "FU_WU_NEI_RONG")
    private String serviceContent;

    /**
     * 其它服务内容
     */
    @ApiModelProperty(value = "其它服务内容", hidden = false, required = false)
    @TableField("service_content_other")
    @HistoryFieldLog("其它服务内容")
    private String serviceContentOther;

    @ApiModelProperty(value = "关联客户", hidden = false, required = false)
    @TableField("customer_id")
    @HistoryFieldLog(value = "关联客户", joinField = "customer")
    @Excel(name = "关联客户", dataType = Excel.DataType.CUSTOMER, sort = 5)
    private Long customerId;


    @ApiModelProperty(value = "关联客户", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "customerId", fillMethod = FillMethod.CUSTOMER)
    private CommonNameVO customer;

    @ApiModelProperty(value = "建筑面积")
    @TableField(value = "construction_area")
    private BigDecimal constructionArea;

    /**
     * 省
     */
    @ApiModelProperty(value = "省", hidden = false, required = false)
    @TableField("province_code")
    @HistoryFieldLog("省")
    @Cnarea(type = CnareaEnum.PROVINCE)
    @Excel(name = "省份", sort = 28)
    private Long provinceCode;

    /**
     * 省名
     */
    @ApiModelProperty(value = "省名", hidden = false, required = false)
    @TableField(exist = false)
    private String provinceTitle;

    /**
     * 市
     */
    @ApiModelProperty(value = "市", hidden = false, required = false)
    @TableField("city_code")
    @HistoryFieldLog("市")
    @Cnarea(type = CnareaEnum.CITY)
    @Excel(name = "城市", sort = 29)
    private Long cityCode;

    /**
     * 市区名称
     */
    @ApiModelProperty(value = "市区名称", hidden = false, required = false)
    @TableField(exist = false)
    private String cityTitle;

    /**
     * 区
     */
    @ApiModelProperty(value = "区", hidden = false, required = false)
    @TableField("area_code")
    @HistoryFieldLog("区")
    @Cnarea(type = CnareaEnum.AREA)
    @Excel(name = "区/县", sort = 30)
    private Long areaCode;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道", hidden = false, required = false)
    @TableField("street_code")
    @HistoryFieldLog("街道")
    @Cnarea(type = CnareaEnum.STREET)
    @Excel(name = "街道", sort = 31)
    private Long streetCode;

    @TableField(value = "address")
    private String address;

    @ApiModelProperty(value = "预计进场时间")
    @TableField(value = "approach_time")
    private Date approachTime;

    @ApiModelProperty(value = "目预估金额(元)")
    @TableField(value = "biding_budget")
    private BigDecimal bidingBudget;

    @ApiModelProperty(value = "项目来源")
    @TableField(value = "source_name")
    private String sourceName;

    @ApiModelProperty(value = "关键字")
    @TableField(value = "keywords")
    private String keywords;

    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 招标信息 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
     */

    /**
     * 招标编号
     */
    @ApiModelProperty(value = "招标编号", hidden = false, required = false)
    @TableField("biding_no")
    @HistoryFieldLog("招标编号")
    private String bidingNo;

    /**
     * 招标类型
     */
    @ApiModelProperty(value = "招标类型", hidden = false, required = false)
    @TableField("biding_mode")
    @HistoryFieldLog("招标类型")
    @Dict(type = "ZHAO_BIAO_LEI_XING")
    @Excel(name = "招标类型", sort = 17)
    private String bidingMode;

    /**
     * 招标类型
     */
    @ApiModelProperty(value = "招标类型名称", hidden = false, required = false)
    @TableField(exist = false)
    private String bidingModeTitle;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期", hidden = false, required = false)
    @TableField("biding_publishtime")
    @HistoryFieldLog("发布日期")
    private Date bidingPublishtime;

    /**
     * 招标单位
     */
    @ApiModelProperty(value = "招标单位", hidden = false, required = false)
    @TableField("biding_company")
    @HistoryFieldLog("招标单位")
    private String bidingCompany;

    /**
     * 招标单位联系人
     */
    @ApiModelProperty(value = "招标单位联系人", hidden = false, required = false)
    @TableField("biding_company_contact")
    @HistoryFieldLog("招标单位联系人")
    private String bidingCompanyContact;

    /**
     * 招标单位联系方式
     */
    @ApiModelProperty(value = "招标单位联系方式", hidden = false, required = false)
    @TableField("biding_company_phone")
    @HistoryFieldLog("招标单位联系方式")
    private String bidingCompanyPhone;

    /**
     * 招标网址
     */
    @ApiModelProperty(value = "招标网址", hidden = false, required = false)
    @TableField("biding_website")
    @HistoryFieldLog("招标网址")
    private String bidingWebsite;

    /**
     * 代理机构
     */
    @ApiModelProperty(value = "代理机构", hidden = false, required = false)
    @TableField("biding_agency")
    @HistoryFieldLog("代理机构")
    private String bidingAgency;

    /**
     * 代理机构联系人
     */
    @ApiModelProperty(value = "代理机构联系人", hidden = false, required = false)
    @TableField("biding_agency_contact")
    @HistoryFieldLog("代理机构联系人")
    private String bidingAgencyContact;

    /**
     * 代理机构联系方式
     */
    @ApiModelProperty(value = "代理机构联系方式", hidden = false, required = false)
    @TableField("biding_agency_phone")
    @HistoryFieldLog("代理机构联系方式")
    private String bidingAgencyPhone;

    /**
     * 投标截止时间
     */
    @ApiModelProperty(value = "投标截止时间", hidden = false, required = false)
    @TableField("biding_endtime")
    @HistoryFieldLog("投标截止时间")
    private Date bidingEndtime;

    /**
     * 开标时间
     */
    @ApiModelProperty(value = "开标时间", hidden = false, required = false)
    @TableField("biding_opentime")
    @HistoryFieldLog("开标时间")
    private Date bidingOpentime;


    /**
     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 合同信息 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
     */

    @ApiModelProperty(value = "甲方公司名称")
    @TableField(value = "first_responsible_company")
    private String firstResponsibleCompany;

    @ApiModelProperty(value = "合同金额(元)")
    @TableField(value = "contract_amount")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "合同年度金额(元)")
    @TableField(value = "contract_annual_amount")
    private BigDecimal contractAnnualAmount;

    @ApiModelProperty(value = "当年转化收入金额(元)")
    @TableField(value = "annual_conversion_amount")
    private BigDecimal annualConversionAmount;

    @ApiModelProperty(value = "签约日期")
    @TableField(value = "sign_time")
    private Date signTime;

    @ApiModelProperty(value = "服务开始日期")
    @TableField(value = "service_begin_time")
    private Date serviceBeginTime;

    @ApiModelProperty(value = "合同到期日期")
    @TableField(value = "service_end_time")
    private Date serviceEndTime;

    @ApiModelProperty(value = "拟服务期限(月)")
    @TableField(value = "proposed_service_period")
    private Integer proposedServicePeriod;

    @ApiModelProperty(value = "是否有增量业绩确认")
    @Dict(type = "SHI_FOU")
    @TableField(value = "is_performance_increment")
    private String isPerformanceIncrement;

    @ApiModelProperty(value = "合同金额(元)")
    @TableField(value = "contract_amounts")
    private BigDecimal contractAmounts;

    @ApiModelProperty(value = "合同年度金额(元)")
    @TableField(value = "contract_annual_amounts")
    private BigDecimal contractAnnualAmounts;

    @ApiModelProperty(value = "当年转化收入金额(元)")
    @TableField(value = "annual_conversion_amounts")
    private BigDecimal annualConversionAmounts;

    @ApiModelProperty(value = "是否计算业绩")
    @Dict(type = "SHI_FOU")
    @TableField(value = "is_count_performance")
    private String isCountPerformance;

    @ApiModelProperty(value = "是否有效")
    @Dict(type = "SHI_FOU")
    @TableField(value = "validity_status")
    private String validityStatus;

    @ApiModelProperty(value = "补录状态",notes = "0草稿/1审批中/2已完成/3驳回")
    @TableField(value = "backfill_status")
    private Integer backfillStatus;

    @ApiModelProperty(value = "补录状态str",notes = "0草稿/1审批中/2已完成/3驳回")
    @TableField(exist = false)
    private String backfillStatusStr;

    @ApiModelProperty(value = "最新跟进时间", hidden = false, required = false)
    @TableField("follow_time")
    @HistoryFieldLog("最新跟进时间")
    @Excel(name = "最新跟进时间", sort = 35)
    private Date followTime;

    @ApiModelProperty(value = "是否计算增量年度转化金额(FOU:否,SHI:是),默认否或者空", hidden = false, required = false)
    @Dict(type = "SHI_FOU")
    @TableField(exist = false)
    private String isIncrement;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @HistoryFieldLog("创建人")
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Excel(name = "创建人", dataType = Excel.DataType.USER)
    private Long createUserId;

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @HistoryFieldLog("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @HistoryFieldLog(value ="修改人",joinField = "updateUser")
    @Excel(name = "修改人")
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    @HistoryFieldLog("修改时间")
    @Excel(name = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "业绩分配", hidden = false, required = false)
    @TableField(exist = false)
    private List<ProjectBacklogAchievement> projectBacklogAchievement;

    @ApiModelProperty(value = "附件", hidden = false, required = false)
    @TableField(exist = false)
    private List<ProjectBacklogFile> projectBacklogFiles;

    @ApiModelProperty(value = "附件分类", hidden = false, required = false)
    @TableField(exist = false)
    private List<AuditDocumentTemplate> projectBacklogFilesGroup;

    @ApiModelProperty(value = "是否超级管理员", hidden = false, required = false)
    @TableField(exist = false)
    private Boolean isAdministrators;


    /**
     * 公司标识
     */
    @ApiModelProperty(value = "公司标识", hidden = false, required = false)
    @TableField("corporate_identity")
    @Dict(type = "GONG_SI_BIAO_SHI")
    private String corporateIdentity;

    /**
     * 业务板块
     */
    @ApiModelProperty(value = "业务板块", hidden = false, required = false)
    @TableField("business_segments")
    @Dict(type = "YE_WU_BAN_KUAI")
    private String businessSegments;

    /**
     * 乙方单位名称
     */
    @ApiModelProperty(value = "乙方单位名称", hidden = false, required = false)
    @TableField("second_party")
    private String secondParty;

}
