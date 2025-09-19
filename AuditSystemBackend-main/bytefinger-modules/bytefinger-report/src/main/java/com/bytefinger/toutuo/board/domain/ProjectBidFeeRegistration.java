package com.bytefinger.toutuo.board.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 项目投标费用台帐
 * </p>
 *
 * @author licheng
 * @since 2023-01-31
 */
@Data
@TableName("biz_project_bid_fee_registration")
@ApiModel(value = "ProjectBidFeeRegistration对象", description = "投标费用台帐")
public class ProjectBidFeeRegistration extends BaseEntity {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = false, required = false)
    @TableField("project_id")
    private Long projectId;

    /**
     * 所属大区
     */
    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @TableField("region_id")
    private Long regionId;

    @ApiModelProperty(value = "大区", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "regionId", fillMethod = FillMethod.DEPT)
    @Excel(name = "所属大区")
    private String regionName;

    /**
     * 地区公司
     */
    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField("company_id")
    private Long companyId;

    @ApiModelProperty(value = "地区公司", hidden = false, required = false)
    @TableField(exist = false)
    @DataFillField(dataField = "companyId", fillMethod = FillMethod.DEPT)
    @Excel(name = "归属单位")
    private String companyName;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编号", hidden = false, required = false)
    @TableField("project_no")
    @Excel(name = "项目编号")
    private String projectNo;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", hidden = false, required = false)
    @TableField(exist = false)
    @Excel(name = "项目名称",width = 64)
    private String projectName;

    /**
     * 费用类型
     */
    @ApiModelProperty(value = "费用类型", hidden = false, required = false)
    @TableField("free_type")
    @Dict(type = "TOU_BIAO_FEI_YONG_LEI_XING")
    @Excel(name = "费用名称")
    private String freeType;

    /**
     * 费用类型其它
     */
    @ApiModelProperty(value = "费用类型其它", hidden = false, required = false)
    @TableField("free_type_other")
    private String freeTypeOther;
    /**
     * 费用金额
     */
    @ApiModelProperty(value = "费用金额", hidden = false, required = false)
    @TableField("free_amount")
    @Excel(name = "费用金额")
    private BigDecimal freeAmount;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间", hidden = false, required = false)
    @TableField(value = "payment_time")
    @Excel(name = "支付时间",dateFormat = "YYYY-MM-DD")
    private Date paymentTime;

    /**
     * 缴费公司
     */
    @ApiModelProperty(value = "实际缴纳单位", hidden = false, required = false)
    @TableField("payment_company")
    @Excel(name = "实际缴纳单位")
    private String paymentCompany;

    /**
     * 收款公司
     */
    @ApiModelProperty(value = "收款单位", hidden = false, required = false)
    @TableField("payee_company")
    @Excel(name = "收款单位")
    private String payeeCompany;

    /**
     * 欠费公司
     */
    @ApiModelProperty(value = "欠款单位", hidden = false, required = false)
    @TableField("arrearage_company")
    @Excel(name = "欠款单位")
    private String arrearageCompany;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "登记人", hidden = false, required = false)
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    /**
     * 欠款状态(0:未追回,1:已追回)
     */
    @ApiModelProperty(value = "欠款状态(0:未追回,1:已追回)", hidden = false, required = false)
    @TableField("recover_status")
    @Dict(type = "QIAN_KUAN_ZHUANG_TAI")
    @Excel(name = "欠款状态")
    private String recoverStatus;

    /**
     * 扣除金额
     */
    @ApiModelProperty(value = "回款金额（元）", hidden = false, required = false)
    @TableField("deduct_amount")
    @Excel(name = "回款金额（元）")
    private BigDecimal deductAmount;

    /**
     * 回款状态
     */
    @ApiModelProperty(value = "回款状态", hidden = false, required = false)
    @TableField("deduct")
    @Excel(name = "回款状态")
    @Dict(type = "HUI_KUAN_ZHUANG_TAI")
    private String deduct;

    /**
     * 回款时间
     */
    @ApiModelProperty(value = "回款时间", hidden = false, required = false)
    @TableField("recover_time")
    @Excel(name = "回款时间",dateFormat = DateUtils.YYYY_MM_DD)
    private Date recoverTime;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @TableField("remark")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty(value = "登记人", hidden = false, required = false)
    @TableField(exist = false)
    @Excel(name = "登记人")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "最后修改时间", hidden = false, required = false)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "登记时间")
    private Date createTime;

}