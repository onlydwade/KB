package com.bytefinger.toutuo.board.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.annotation.Excel;
import com.bytefinger.common.security.enums.FillMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 描述:
 *      整体情况详情导出-大区
 * @author LaiDaCheng
 * @email dacheng.lai@bytefinger.com
 * @date 2024/3/15 9:37
 */
@Data
@ApiModel(value = "ProjectSituationThreeDetailsRegionExport对象", description = "整体情况详情按大区导出")
public class ProjectSituationThreeDetailsRegionExport {

    @ApiModelProperty(value = "所属大区", hidden = false, required = false)
    @Excel(name = "所属大区")
    private String regionName;

    @ApiModelProperty(value = "在管项目总数", hidden = false, required = false)
    @Excel(name = "在管项目总数")
    private String projectTotal;


    @ApiModelProperty(value = "在管项目面积总数", hidden = false, required = false)
    @Excel(name = "在管项目面积总数")
    private String waiProjectTotal;

    @ApiModelProperty(value = "新签项目总数", hidden = false, required = false)
    @Excel(name = "新签项目总数")
    private String signProjectTotal;


    @ApiModelProperty(value = "当年新增面积总数", hidden = false, required = false)
    @Excel(name = "当年新增面积总数")
    private String newWaiProjectTotal;

    @ApiModelProperty(value = "当年新增合同转化收入", hidden = false, required = false)
    @Excel(name = "当年新增合同转化收入")
    private String xzzhsr;


    @ApiModelProperty(value = "新增合同金额", hidden = false, required = false)
    @Excel(name = "新增合同金额")
    private String xzzje;

    @ApiModelProperty(value = "新增年度合同金额", hidden = false, required = false)
    @Excel(name = "新增年度合同金额")
    private String xzndzje;


    @ApiModelProperty(value = "续签项目总数", hidden = false, required = false)
    @Excel(name = "续签项目总数")
    private String signRenewalProjectTotal;

    @ApiModelProperty(value = "续签合同总金额", hidden = false, required = false)
    @Excel(name = "续签合同总金额")
    private String xqzje;

    @ApiModelProperty(value = "续签合同年度金额", hidden = false, required = false)
    @Excel(name = "续签合同年度金额")
    private String xqndzje;
}
