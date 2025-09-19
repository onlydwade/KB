package com.bytefinger.toutuo.biz.projectextension.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.enums.RoleKeys;
import com.bytefinger.toutuo.biz.project.domain.Project;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: ycj
 * @desc:
 * @date: 2023-03-10
 **/
@Data
@Schema(description = "在管项目查询")
@ApiModel(value = "在管项目查询", description = "在管项目查询")
public class ProjectQueryVo {


//    /**
//     * 是否承接查验
//     */
//    @ApiModelProperty(value = "是否承接查验", hidden = false, required = false)
//    @Schema(description = "是否承接查验", name = "continueIsNotCheck")
//    private String continueIsNotCheck;
    /**
     * id
     */
    @ApiModelProperty(value = "id", hidden = false, required = false)
    @Schema(description = "id", name = "id")
    private Long id;

    /**
     * 是否已评估
     */
    @ApiModelProperty(value = "是否已评估", hidden = false, required = false)
    @Schema(description = "是否已评估", name = "assessIsNot")
    @Excel(name = "是否已评估", sort = 10)
    @Dict(type = "SHI_FOU")
    private String assessIsNot;

    /**
     * 合同是否到期
     */
    @ApiModelProperty(value = "合同是否到期", hidden = false, required = false)
    @Schema(description = "合同是否到期", name = "contractIsNotExpire")
    @Excel(name = "合同是否到期", sort = 12)
    private String contractIsNotExpire;


    /**
     * 合同到期倒计时
     */
    @ApiModelProperty(value = "合同是否到期合同到期倒计时", hidden = false, required = false)
    @Schema(description = "合同到期倒计时", name = "contractExpireTime")
    @Excel(name = "合同到期倒计时", sort = 13)
    private String contractExpireTime;

    /**
     * 合同到期倒计时
     */
    @ApiModelProperty(value = "经营评估期限", hidden = false, required = false)
    @Schema(description = "经营评估期限", name = "manageAssessDeadline")
//    @Excel(name = "经营评估期限", sort = 15)
    private Date manageAssessDeadline;

    /**
     * 合同到期倒计时
     */
    @ApiModelProperty(value = "经营评估期限", hidden = false, required = false)
    @Schema(description = "经营评估期限", name = "manageAssessDeadline")
    @Excel(name = "经营评估期限", sort = 15)
    private String manageAssessDeadlineStr;

    /**
     *  * 0  未发起审批 1:审批中  2:审批通过 3:处理驳回
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态", hidden = false, required = false)
    @Schema(description = "处理状态", name = "processState")
//    @Excel(name = "处理状态", sort = 16)
    private Integer processState;

    /**
     *  * 0  未发起审批 1:审批中  2:审批通过 3:处理驳回
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态", hidden = false, required = false)
    @Schema(description = "处理状态", name = "processState")
    @Excel(name = "处理状态", sort = 16)
    private String processStateStr;

    /**
     * 处理方式
     */
    @ApiModelProperty(value = "处理方式", hidden = false, required = false)
    @Schema(description = "处理方式", name = "processMode")
//    @Excel(name = "处理方式", sort = 17)
    private Integer processMode;

    /**
     * 处理方式
     */
    @ApiModelProperty(value = "处理方式", hidden = false, required = false)
    @Schema(description = "处理方式", name = "processMode")
    @Excel(name = "处理方式", sort = 17)
    private String processModeStr;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人", hidden = false, required = false)
    @Schema(description = "处理人", name = "processPerson")
    @Excel(name = "处理人", sort = 19)
    private String processPerson;
    /**
     * 处理时间
     */
    @ApiModelProperty(value = "处理时间", hidden = false, required = false)
    @Schema(description = "处理时间", name = "disposeTime")
    @Excel(name = "处理日期", sort = 18)
    private Date disposeTime;


    /**
     * 关联新建项目
     */
    @ApiModelProperty(value = "关联新建项目", hidden = false, required = false)
    @Schema(description = "关联新建项目", name = "relevancePprojectId")
    private Long relevanceProjectId;

    /**
     * 关联新建项目
     */
    @ApiModelProperty(value = "关联新建项目", hidden = false, required = false)
    @Schema(description = "关联新建项目", name = "relevancePprojectNo")
    @Excel(name = "关联新建项目", sort = 20)
    private String relevanceProjectNo;

    /**
     * 退场描述
     */
    @ApiModelProperty(value = "退场描述", hidden = false, required = false)
    @Schema(description = "退场描述", name = "exitDescription")
    @Excel(name = "退场描述", sort = 21)
    private String exitDescription;

    /**
     * 退场描述
     */
    @ApiModelProperty(value = "退场时间", hidden = false, required = false)
    @Schema(description = "退场时间", name = "approvalSponsorTime")
    @Excel(name = "退场时间", sort = 21)
    private String approvalSponsorTime;

    /**
     * 对应OA审批流程
     */
    @ApiModelProperty(value = "对应OA审批流程", hidden = false, required = false)
    @Schema(description = "对应OA审批流程", name = "approvalProcessOA")
    @Excel(name = "对应OA审批流程", sort = 22)
    private String  approvalProcessOA;

    /**
     * 是否已承接查验
     */
    @ApiModelProperty(value = "是否已承接查验", hidden = false, required = false)
    @Schema(description = "是否已承接查验", name = "checkState")
    @Excel(name = "是否已承接查验", sort = 9)
    @Dict(type = "SHI_FOU")
    private String checkState;

    /**
     * 当前登入人是否是项目归属人、拓后负责人、项目团队成员
     */
    @ApiModelProperty(value = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", hidden = false, required = false)
    @Schema(description = "当前登入人是否是项目归属人、拓后负责人、项目团队成员", name = "checkState")
    private Boolean show;

    /**
     * 当前登入人是否是拓后负责人
     */
    @ApiModelProperty(value = "当前登入人是否是拓后负责人", hidden = false, required = false)
    @Schema(description = "当前登入人是否是拓后负责人", name = "principalShow")
    private Boolean principalShow;

    /**
     * 当前登入人是否是拓后负责人
     */
    @ApiModelProperty(value = "当前登入人是否是创建人和项目归属人", hidden = false, required = false)
    @Schema(description = "当前登入人是否是创建人和项目归属人", name = "principalShow")
    private Boolean attributorOrCreateShow;
    /**
     * 项目名称
     */

    @ApiModelProperty(value = "项目名称", hidden = false, required = false)
    @TableField("project_name")
    @HistoryFieldLog("项目名称")
    @Excel(name = "项目名称", sort = 3)
    private String projectName;

    /**
     * 项目来源Id
     */
    @ApiModelProperty(value = "项目来源Id", hidden = false, required = false)
    @TableField("source_id")
    private Long sourceId;

    /**
     * 项目来源名称
     */
    @ApiModelProperty(value = "项目来源名称", hidden = false, required = false)
    @TableField("source_name")
    @HistoryFieldLog("项目来源名称")
//    @Excel(name = "项目来源", sort = 19)
    private String sourceName;


    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码", hidden = false, required = false)
    @TableField("project_no")
    @HistoryFieldLog("项目编码")
    @Excel(name = "项目编号", sort = 2)
    private String projectNo;


    /**
     * 所属大区
     */
    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField("region_id")
    @HistoryFieldLog(value = "所属大区", joinField = "regionName")
    @Excel(name = "大区", dataType = Excel.DataType.DEPT, sort = 23)
    private Long regionId;

    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "regionId", fillMethod = FillMethod.DEPT)
    private String regionName;

    /**
     * 地区公司
     */
    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField("company_id")
    @HistoryFieldLog(value = "地区公司", joinField = "companyName")
    @Excel(name = "归属单位", dataType = Excel.DataType.DEPT, sort = 1)
    private Long companyId;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "companyId", fillMethod = FillMethod.DEPT)
    private String companyName;


    /**
     * 主拖项目团队
     */
    @ApiModelProperty(value = "主拖项目团队", hidden = false, required = false)
    @TableField("team_id")
    @HistoryFieldLog(value = "主拖项目团队", joinField = "teamName")
    private Long teamId;

    @ApiModelProperty(value = "主拖项目团队", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "teamId", fillMethod = FillMethod.DEPT)
    private String teamName;


    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级", hidden = false, required = false)
    @TableField("project_level")
    @HistoryFieldLog("优先级")
    @Dict(type = "XIANG_MU_YOU_XIAN_JI")
    @Excel(name = "项目优先级", sort = 4)
    private String projectLevel;

    /**
     * 归属人
     */
    @ApiModelProperty(value = "归属人", hidden = false, required = false)
    @TableField("attributor_user_id")
    @HistoryFieldLog(value = "归属人", joinField = "attributorUser")
    @Excel(name = "归属人", dataType = Excel.DataType.USER, sort = 24)
    private Long attributorUserId;

    @ApiModelProperty(value = "归属人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "attributorUserId", fillMethod = FillMethod.USER)
    private UserVO attributorUser;

    /**
     * 业务板块
     */
    @ApiModelProperty(value = "业务板块", hidden = false, required = false)
    @TableField("business_segments")
    @HistoryFieldLog("业务板块")
    @Dict(type = "YE_WU_BAN_KUAI")
    @Excel(name = "业务板块", sort = 5)
    private String businessSegments;

    /**
     * 拓展模式
     */
    @ApiModelProperty(value = "拓展模式", hidden = false, required = false)
    @TableField("expansion_mode")
    @HistoryFieldLog("拓展模式")
    @Dict(type = "TUO_ZHAN_MO_SHI")
    @Excel(name = "拓展模式", sort = 5)
    private String expansionMode;



    /**
     * 项目类型
     */
    @ApiModelProperty(value = "项目类型", hidden = false, required = false)
    @TableField("project_type")
    @HistoryFieldLog("项目类型")
    @Dict(type = "XIANG_MU_LEI_XING")
    private String projectType;

    /**
     * 是否招标
     */
    @ApiModelProperty(value = "是否招标", hidden = false, required = false)
    @TableField("bided")
    @HistoryFieldLog("是否招标")
    @Dict(type = "SHI_FOU")
    private String bided;

    /**
     * 合作方式
     */
    @ApiModelProperty(value = "合作方式", hidden = false, required = false)
    @TableField("cooperation_mode")
    @HistoryFieldLog("合作方式")
    @Dict(type = "HE_ZUO_FANG_SHI")
    private String cooperationMode;

    /**
     * 合作模式
     */
    @ApiModelProperty(value = "合作模式", hidden = false, required = false)
    @TableField("cooperation_type")
    @HistoryFieldLog("合作模式")
    @Dict(type = "HE_ZUO_MO_SHI")
    private String cooperationType;

    /**
     * 其它合作模式
     */
    @ApiModelProperty(value = "其它合作模式", hidden = false, required = false)
    @TableField("cooperation_type_other")
    @HistoryFieldLog("其它合作模式")
    private String cooperationTypeOther;

    /**
     * 是否存量项目
     */
    @ApiModelProperty(value = "是否存量项目", hidden = false, required = false)
    @TableField("in_stock")
    @HistoryFieldLog("是否存量项目")
    @Dict(type = "SHI_FOU")
    @Excel(name = "是否为续签项目", sort = 26)
    private String inStock;

    /**
     * 业态
     */
    @ApiModelProperty(value = "业态", hidden = false, required = false)
    @TableField("business_type")
    @HistoryFieldLog("业态")
    //@Dict(ptype = "XIANG_MU_YE_TAI")
    private String businessType;

    @Excel(name = "业态", sort = 6)
    private String businessTypeStr;

    /**
     * 服务内容
     */
    @ApiModelProperty(value = "服务内容", hidden = false, required = false)
    @TableField("service_content")
    @HistoryFieldLog("服务内容")
    @Excel(name = "服务内容", sort = 7)
    @Dict(type = "FU_WU_NEI_RONG")
    private String serviceContent;

    /**
     * 其它服务内容
     */
    @ApiModelProperty(value = "其它服务内容", hidden = false, required = false)
    @TableField("service_content_other")
    @HistoryFieldLog("其它服务内容")
    private String serviceContentOther;

    /**
     * 关联客户
     */
    @ApiModelProperty(value = "关联客户", hidden = false, required = false)
    @TableField("customer_id")
    @HistoryFieldLog(value = "关联客户", joinField = "customer")
    private Long customerId;


    @ApiModelProperty(value = "关联客户", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "customerId", fillMethod = FillMethod.CUSTOMER)
    private CommonNameVO customer;

    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积", hidden = false, required = false)
    @TableField("construction_area")
    @HistoryFieldLog("建筑面积")
    private BigDecimal constructionArea;

    /**
     * 省
     */
    @ApiModelProperty(value = "省", hidden = false, required = false)
    @TableField("province_code")
    @HistoryFieldLog("省")
    @Cnarea(type = CnareaEnum.PROVINCE)
    @Excel(name = "省份", sort = 27)
    private Long provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市", hidden = false, required = false)
    @TableField("city_code")
    @HistoryFieldLog("市")
    @Cnarea(type = CnareaEnum.CITY)
    @Excel(name = "城市", sort = 28)
    private Long cityCode;

    /**
     * 区
     */
    @ApiModelProperty(value = "区", hidden = false, required = false)
    @TableField("area_code")
    @HistoryFieldLog("区")
    @Cnarea(type = CnareaEnum.AREA)
    @Excel(name = "区/县", sort = 29)
    private Long areaCode;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道", hidden = false, required = false)
    @TableField("street_code")
    @HistoryFieldLog("街道")
    @Cnarea(type = CnareaEnum.STREET)
    @Excel(name = "街道", sort = 30)
    private Long streetCode;



    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址", hidden = false, required = false)
    @TableField("address")
    @HistoryFieldLog("详细地址")
    private String address;

    /**
     * 预计进场时间
     */
    @ApiModelProperty(value = "预计进场时间", hidden = false, required = false)
    @TableField("approach_time")
    @HistoryFieldLog("预计进场时间")
    private Date approachTime;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词", hidden = false, required = false)
    @TableField("keywords")
    @HistoryFieldLog("关键词")
    private String keywords;

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
    private String bidingMode;

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
     * 招标预算金额
     */
    @ApiModelProperty(value = "招标预算金额", hidden = false, required = false)
    @TableField("biding_budget")
    @HistoryFieldLog("招标预算金额")
    private String bidingBudget;

    /**
     * 碧拓报名已通过
     */
    @ApiModelProperty(value = "碧拓报名已通过", hidden = false, required = false)
    @TableField("bt_apply")
    @HistoryFieldLog("碧拓报名已通过")
    private String btApply;

    /**
     * 标书购买金额
     */
    @ApiModelProperty(value = "标书购买金额", hidden = false, required = false)
    @TableField("tender_amount")
    @HistoryFieldLog("标书购买金额")
    private String tenderAmount;

    /**
     * 最终投标报价金额
     */
    @ApiModelProperty(value = "最终投标报价金额", hidden = false, required = false)
    @TableField("biding_amount")
    @HistoryFieldLog("最终投标报价金额")
    private String bidingAmount;

    /**
     * 投标结果
     */
    @ApiModelProperty(value = "投标结果", hidden = false, required = false)
    @TableField("bided_result")
    @HistoryFieldLog("投标结果")
    @Dict(type = "TOU_BIAO_JIE_GUO")
    private String bidedResult;

    /**
     * 中标单位
     */
    @ApiModelProperty(value = "中标单位", hidden = false, required = false)
    @TableField("bided_company")
    @HistoryFieldLog("中标单位")
    private String bidedCompany;

    /**
     * 中标金额
     */
    @ApiModelProperty(value = "中标金额", hidden = false, required = false)
    @TableField("bided_amount")
    @HistoryFieldLog("中标金额")
    private String bidedAmount;

    /**
     * 中标 | 未中标原因
     */
    @ApiModelProperty(value = "中标 | 未中标原因", hidden = false, required = false)
    @TableField("bided_remark")
    @HistoryFieldLog("中标 | 未中标原因")
    private String bidedRemark;

    /**
     * 项目尽调形式
     */
    @ApiModelProperty(value = "项目尽调形式", hidden = false, required = false)
    @TableField("dd_mode")
    @HistoryFieldLog("项目尽调形式")
    @Dict(type = "JIN_DIAO_XING_SHI")
    private String ddMode;

    /**
     * 尽调费用预算
     */
    @ApiModelProperty(value = "尽调费用预算", hidden = false, required = false)
    @TableField("dd_amount")
    @HistoryFieldLog("尽调费用预算")
    private String ddAmount;


    /**
     * 参标单位
     */
    @ApiModelProperty(value = "参标单位", hidden = false, required = false)
    @TableField("bid_part_in_company")
    @HistoryFieldLog("参标单位")
    private String bidPartInCompany;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "createUserId", fillMethod = FillMethod.USER)
    private UserVO createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", hidden = false, required = false)
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = false, required = false)
    @TableField(value = "update_time",  fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "updateUserId", fillMethod = FillMethod.USER)
    private UserVO updateUser;

    /**
     * 最新跟进时间
     */
    @ApiModelProperty(value = "最新跟进时间", hidden = false, required = false)
    @TableField("follow_time")
    @HistoryFieldLog("最新跟进时间")
    @Excel(name = "最新跟进时间", sort = 31)
    private Date followTime;

    /**
     * 签约主体
     */
    @ApiModelProperty(value = "签约主体", hidden = false, required = false)
    @TableField("sign_dept_id")
    @HistoryFieldLog(value = "签约主体", joinField = "signDeptName")
    private Long signDeptId;

    @ApiModelProperty(value = "签约主体", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "signDeptId", fillMethod = FillMethod.DEPT)
    private String signDeptName;

    /**
     * 收费主体
     */
    @ApiModelProperty(value = "收费主体", hidden = false, required = false)
    @TableField("charge_dept_id")
    @HistoryFieldLog(value = "收费主体", joinField = "chargeDeptName")
    private Long chargeDeptId;

    @ApiModelProperty(value = "收费主体", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "chargeDeptId", fillMethod = FillMethod.DEPT)
    private String chargeDeptName;

    /**
     * 运营主体
     */
    @ApiModelProperty(value = "运营主体", hidden = false, required = false)
    @TableField("operation_dept_id")
    @HistoryFieldLog(value = "运营主体", joinField = "operationDeptName")
    private Long operationDeptId;

    @ApiModelProperty(value = "运营主体", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "operationDeptId", fillMethod = FillMethod.DEPT)
    private String operationDeptName;

    /**
     * 委托合作方式
     */
    @ApiModelProperty(value = "委托合作方式", hidden = false, required = false)
    @TableField("entrusted_cooperation_mode")
    @HistoryFieldLog("委托合作方式")
    private String entrustedCooperationMode;

    /**
     * 首次出资金额
     */
    @ApiModelProperty(value = "首次出资金额", hidden = false, required = false)
    @TableField("first_capital_amount")
    @HistoryFieldLog("首次出资金额")
    private BigDecimal firstCapitalAmount;

    /**
     * 首次出资时间
     */
    @ApiModelProperty(value = "首次出资时间", hidden = false, required = false)
    @TableField("first_capital_time")
    @HistoryFieldLog("首次出资时间")
    private Date firstCapitalTime;

    /**
     * 车位数量
     */
    @ApiModelProperty(value = "车位数量", hidden = false, required = false)
    @TableField("parking_number")
    @HistoryFieldLog("车位数量")
    private Integer parkingNumber;

    /**
     * 运营团队
     */
    @ApiModelProperty(value = "运营团队", hidden = false, required = false)
    @TableField("operation_team_id")
    @HistoryFieldLog(value = "运营团队", joinField = "operationTeamName")

    private Long operationTeamId;
    /**
     * 运营团队
     */
    @ApiModelProperty(value = "运营团队", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "operationTeamId", fillMethod = FillMethod.DEPT)
    @Excel(name = "运营团队", sort = 25)
    private String operationTeamName;

    /**
     * 拓后负责人
     */
    @ApiModelProperty(value = "拓后负责人", hidden = false, required = false)
    @TableField("principal_id")
    @HistoryFieldLog(value = "拓后负责人", joinField = "principal")
    @Excel(name = "拓后负责人", dataType = Excel.DataType.USER, sort = 8)
    private Long principalId;

    @ApiModelProperty(value = "拓后负责人", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "principalId", fillMethod = FillMethod.USER)
    private UserVO principal;

    /**
     * 交接日期
     */
    @ApiModelProperty(value = "交接日期", hidden = false, required = false)
    @TableField("handover_date")
    @HistoryFieldLog("交接日期")
    private Date handoverDate;

    /**
     * 甲方单位名称
     */
    @ApiModelProperty(value = "甲方单位名称", hidden = false, required = false)
    @TableField("first_responsible_company")
    @HistoryFieldLog("甲方单位名称")
    private String firstResponsibleCompany;

    /**
     * 甲方第一责任人姓名
     */
    @ApiModelProperty(value = "甲方第一责任人姓名", hidden = false, required = false)
    @TableField("first_responsible_name")
    @HistoryFieldLog("甲方第一责任人姓名")
    private String firstResponsibleName;

    /**
     * 甲方第一责任人职务
     */
    @ApiModelProperty(value = "甲方第一责任人职务", hidden = false, required = false)
    @TableField("first_responsible_post")
    @HistoryFieldLog("甲方第一责任人职务")
    private String firstResponsiblePost;

    /**
     * 甲方第一责任人移动电话
     */
    @ApiModelProperty(value = "甲方第一责任人移动电话", hidden = false, required = false)
    @TableField("first_responsible_phone")
    @HistoryFieldLog("甲方第一责任人移动电话")
    private String firstResponsiblePhone;

    /**
     * 甲方对接人
     */
    @ApiModelProperty(value = "甲方对接人", hidden = false, required = false)
    @TableField("handover_person")
    @HistoryFieldLog("甲方对接人")
    private String handoverPerson;

    /**
     * 甲方对接人职务
     */
    @ApiModelProperty(value = "甲方对接人职务", hidden = false, required = false)
    @TableField("handover_post")
    @HistoryFieldLog("甲方对接人职务")
    private String handoverPost;

    /**
     * 甲方对接人移动电话
     */
    @ApiModelProperty(value = "甲方对接人移动电话", hidden = false, required = false)
    @TableField("handover_phone")
    @HistoryFieldLog("甲方对接人移动电话")
    private String handoverPhone;

    /**
     * 实际进场时间
     */
    @ApiModelProperty(value = "实际进场时间", hidden = false, required = false)
    @TableField("enter_time")
    @HistoryFieldLog("实际进场时间")
    private Date enterTime;

    /**
     * 优惠承诺
     */
    @ApiModelProperty(value = "优惠承诺", hidden = false, required = false)
    @TableField("discounts_desc")
    @HistoryFieldLog("优惠承诺")
    private String discountsDesc;

    /**
     * 服务承诺
     */
    @ApiModelProperty(value = "服务承诺", hidden = false, required = false)
    @TableField("service_desc")
    @HistoryFieldLog("服务承诺")
    private String serviceDesc;

    /**
     * 主要条款
     */
    @ApiModelProperty(value = "主要条款", hidden = false, required = false)
    @TableField("main_clause")
    @HistoryFieldLog("主要条款")
    private String mainClause;

    /**
     * 其他风险提示
     */
    @ApiModelProperty(value = "其他风险提示", hidden = false, required = false)
    @TableField("risk_warning")
    @HistoryFieldLog("其他风险提示")
    private String riskWarning;

    /**
     * 添加微信好友
     */
    @ApiModelProperty(value = "添加微信好友", hidden = false, required = false)
    @TableField("wechat_friends")
    @HistoryFieldLog("添加微信好友")
    private String wechatFriends;

    /**
     * 现场走访
     */
    @ApiModelProperty(value = "现场走访", hidden = false, required = false)
    @TableField("interviewed")
    @HistoryFieldLog("现场走访")
    private String interviewed;

    /**
     * 其他补充
     */
    @ApiModelProperty(value = "其他补充", hidden = false, required = false)
    @TableField("other_desc")
    @HistoryFieldLog("其他补充")
    private String otherDesc;

    /**
     * 合同金额(单位：元)
     */
    @ApiModelProperty(value = "合同金额(单位：元)", hidden = false, required = false)
    @TableField("contract_amount")
    @HistoryFieldLog("合同金额(单位：元)")
    private BigDecimal contractAmount;

    /**
     * 合同年度金额(单位：元)
     */
    @ApiModelProperty(value = "合同年度金额(单位：元)", hidden = false, required = false)
    @TableField("contract_annual_amount")
    @HistoryFieldLog("合同年度金额(单位：元)")
    private BigDecimal contractAnnualAmount;

    /**
     * 当年转化金额(单位：元)
     */
    @ApiModelProperty(value = "当年转化金额(单位：元)", hidden = false, required = false)
    @TableField("annual_conversion_amount")
    @HistoryFieldLog("当年转化金额(单位：元)")
    private BigDecimal annualConversionAmount;

    /**
     * 测算利润率(%)
     */
    @ApiModelProperty(value = "测算利润率(%)", hidden = false, required = false)
    @TableField("calculate_profit_rate")
    @HistoryFieldLog("测算利润率(%)")
    private BigDecimal calculateProfitRate;

    /**
     * 签约时间
     */
    @ApiModelProperty(value = "签约时间", hidden = false, required = false)
    @TableField("sign_time")
    @HistoryFieldLog("签约时间")
    private Date signTime;

    /**
     * 服务开始时间
     */
    @ApiModelProperty(value = "服务开始时间", hidden = false, required = false)
    @TableField("service_begin_time")
    @HistoryFieldLog("服务开始时间")
    @Excel(name = "合同开始时间" , dateFormat = "yyyy-MM-dd" , sort = 32)
    private Date serviceBeginTime;

    /**
     * 服务结束时间
     */
    @ApiModelProperty(value = "服务结束时间", hidden = false, required = false)
    @TableField("service_end_time")
    @HistoryFieldLog("服务结束时间")
    @Excel(name = "合同结束时间" , dateFormat = "yyyy-MM-dd" , sort = 33)
    private Date serviceEndTime;

    /**
     * 服务结束时间
     */
    @ApiModelProperty(value = "服务结束时间", hidden = false, required = false)
    @TableField(exist = false)
    @Excel(name = "合同到期日期", sort = 14)
    private String serviceEndTimeStr;

    /**
     * 拟服务期限(单位:月)
     */
    @ApiModelProperty(value = "拟服务期限(单位:月)", hidden = false, required = false)
    @TableField("proposed_service_period")
    @HistoryFieldLog("拟服务期限(单位:月)")
    private Integer proposedServicePeriod;

    /**
     * 失效状态
     */
    @ApiModelProperty(value = "失效状态", hidden = false, required = false)
    @TableField("expire")
    @HistoryFieldLog("失效状态")
    @Dict(type = "XIANG_MU_SHI_XIAO_ZHUANG_TAI")
    private String expire;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间", hidden = false, required = false)
    @TableField("expire_time")
    @HistoryFieldLog("失效时间")
    private Date expireTime;

    /**
     * 失效原因
     */
    @ApiModelProperty(value = "失效原因", hidden = false, required = false)
    @TableField("expire_reason")
    @HistoryFieldLog("失效原因")
    private String expireReason;

    /**
     * 项目业务状态
     */
    @ApiModelProperty(value = "项目业务状态", hidden = false, required = false)
    @TableField("service_status")
    @HistoryFieldLog("项目业务状态")
    @Excel(name = "项目状态", sort = 11)
    @Dict(type = "XIANG_MU_ZHUANG_TAI")
    private String serviceStatus;

    /**
     * 终止时间
     */
    @ApiModelProperty(value = "终止时间", hidden = false, required = false)
    @TableField("termination_time")
    @HistoryFieldLog("终止时间")
    private Date terminationTime;

    /**
     * 终止发起人
     */
    @ApiModelProperty(value = "终止发起人", hidden = false, required = false)
    @TableField("termination_sponsor")
    @HistoryFieldLog("终止发起人")
    private String terminationSponsor;

    /**
     * 终止原因
     */
    @ApiModelProperty(value = "终止原因", hidden = false, required = false)
    @TableField("termination_reason")
    @HistoryFieldLog("终止原因")
    private String terminationReason;

    /**
     * 终止附件
     */
    @ApiModelProperty(value = "终止附件", hidden = false, required = false)
    @TableField("termination_document")
    @HistoryFieldLog("终止附件")
    private String terminationDocument;

    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明", hidden = false, required = false)
    @TableField("condition_explain")
    @HistoryFieldLog("情况说明")
    private String conditionExplain;

    /**
     * 业务所属部门
     */
    @ApiModelProperty(value = "业务所属部门", hidden = false, required = false)
    @TableField("business_dept_id")
    @HistoryFieldLog(value = "业务所属部门", joinField = "businessDeptName")
    private Long businessDeptId;

    @ApiModelProperty(value = "业务所属部门", hidden = false, required = false)
    @TableField("business_dept_name")
//    @DataFillField(dataField = "businessDeptId", fillMethod = FillMethod.DEPT)
    private String businessDeptName;

    @ApiModelProperty(value = "踏勘团队类型", hidden = false, required = false)
    @TableField("dd_team_type")
    @HistoryFieldLog("踏勘团队类型")
    @Dict(type = "TA_KAN_TUAN_DUI")
    private String ddTeamType;

    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第一年（%）", hidden = false, required = false)
    @TableField("collection_rate_first")
    @HistoryFieldLog("立项收缴率指标第一年(%)")
    private BigDecimal collectionRateFirst;

    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第二年（%）", hidden = false, required = false)
    @TableField("collection_rate_second")
    @HistoryFieldLog("立项收缴率指标第二年(%)")
    private BigDecimal collectionRateSecond;

    /**
     * 立项收缴率指标第一年(%)
     */
    @ApiModelProperty(value = "立项收缴率指标第三年（%）", hidden = false, required = false)
    @TableField("collection_rate_third")
    @HistoryFieldLog("立项收缴率指标第三年(%)")
    private BigDecimal collectionRateThird;

    /**
     * 业绩确认时间
     */
    @ApiModelProperty(value = "业绩确认时间", hidden = false, required = false)
    @TableField("performance_confirm_time")
    @HistoryFieldLog("业绩确认时间")
    private Date performanceConfirmTime;

    @ApiModelProperty(value = "续签项目", hidden = false, required = false)
    @TableField("renewal")
    @Dict(type = "SHI_FOU")
    @HistoryFieldLog("是否为续签项目")
    private String renewal;

    /**
     * 目标公司名称
     */
    @ApiModelProperty(value = "目标公司名称", hidden = false, required = false)
    @TableField("target_company_name")
    @HistoryFieldLog("目标公司名称")
    private String targetCompanyName;

    /**
     * 目标社会统一信用代码
     */
    @ApiModelProperty(value = "目标社会统一信用代码", hidden = false, required = false)
    @TableField("target_company_no")
    @HistoryFieldLog("目标社会统一信用代码")
    private String targetCompanyNo;


    /**
     * 行业分类
     */
    @ApiModelProperty(value = "目标公司类型", hidden = false, required = false)
    @TableField("target_company_type")
    @HistoryFieldLog("目标公司类型")
    @Dict(type = "GONG_SI_LEI_XING")
    private String targetCompanyType;

    /**
     * 注册资本(单位:万元)
     */
    @ApiModelProperty(value = "注册资本(单位:万元)", hidden = false, required = false)
    @TableField("target_registered_capital")
    @HistoryFieldLog("注册资本(单位:万元)")
    private BigDecimal targetRegisteredCapital;

    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址", hidden = false, required = false)
    @TableField("target_incorporation_address")
    @HistoryFieldLog("注册地址")
    private String targetIncorporationAddress;

    /**
     * 成立时间
     */
    @ApiModelProperty(value = "成立时间", hidden = false, required = false)
    @TableField("target_incorporation_time")
    @HistoryFieldLog("成立时间")
    private Date targetIncorporationTime;

    /**
     * 目标业态
     */
    @ApiModelProperty(value = "目标业态", hidden = false, required = false)
    @TableField("target_business_type")
    @HistoryFieldLog("目标业态")
    @Dict(type = "XIANG_MU_YE_TAI")
    private String targetBusinessType;

    /**
     * 目标企业人数
     */
    @ApiModelProperty(value = "目标企业人数", hidden = false, required = false)
    @TableField("target_personnel")
    @HistoryFieldLog("目标企业人数")
    private Integer targetPersonnel;

    /**
     * 目标法定代表人
     */
    @ApiModelProperty(value = "目标法定代表人", hidden = false, required = false)
    @TableField("target_legal_representative")
    @HistoryFieldLog("目标法定代表人")
    private String targetLegalRepresentative;

    /**
     * 目标企业介绍
     */
    @ApiModelProperty(value = "目标企业介绍", hidden = false, required = false)
    @TableField("target_introduce")
    @HistoryFieldLog("目标企业介绍")
    private String targetIntroduce;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @TableField("remark")
    private String remark;

    /**
     * 是否已完成子公司同步
     */
    @ApiModelProperty(value = "是否已完成子公司同步", hidden = false, required = false)
    @TableField("sync_company_process")
    private Boolean syncCompanyProcess;


    /**
     * 参与人
     */
//    @ApiModelProperty(value = "参与人", hidden = false, required = false)
//    @TableField(exist = false)
//    @DataFillField(dataField = "id", fillMethod = FillMethod.USERS)
//    private List<Long> userIds;

    @ApiModelProperty(value = "参与人ids", hidden = false, required = false)
    @TableField(exist = false)
    private List<Long> userIds;


    @ApiModelProperty(value = "参与人信息", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "id", fillMethod = FillMethod.USERS, joinWrite = "userIds")
    private List<UserVO> users = Lists.newArrayList();


    /**
     * 数据权限
     */
    @ApiModelProperty(value = "数据权限", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "id", fillMethod = FillMethod.ROLEKEYS, roleKeysField = {RoleKeys.APPROVAL, RoleKeys.USER_IDS, RoleKeys.ATTRIBUTOR_USER_ID, RoleKeys.DEPT_ID, RoleKeys.CREATE_USER_ID})
    private List<Integer> roleKeys = Lists.newArrayList();


    @ApiModelProperty(value = "状态", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "assessState", fillMethod = FillMethod.DEPT)
    private String assessState;


    @TableField(exist = false)
    @Excel(name = "完成节点", sort = 6)
    private String stepName;

    private String isSyncOperation;

    @TableField("corporate_identity")
    @Dict(type = "GONG_SI_BIAO_SHI")
    private String corporateIdentity;
}
