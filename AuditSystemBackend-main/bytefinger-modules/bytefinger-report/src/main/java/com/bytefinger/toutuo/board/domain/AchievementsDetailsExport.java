package com.bytefinger.toutuo.board.domain;

import com.bytefinger.common.security.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述:
 *      业绩完成情况详情导出
 * @author LaiDaCheng
 * @email dacheng.lai@bytefinger.com
 * @date 2024/3/15 9:37
 */
@Data
@ApiModel(value = "AchievementsDetailsExport对象", description = "业绩完成情况详情导出")
public class AchievementsDetailsExport {

    @ApiModelProperty(value = "上级", hidden = false, required = false)
    //@Excel(name = "上级")
    @Excel(sort = 1)
    private String regionName;

    @ApiModelProperty(value = "大区/单位", hidden = false, required = false)
    //@Excel(name = "大区/单位")
    @Excel(sort = 2)
    private String companyName;

    @ApiModelProperty(value = "合同总金额", hidden = false, required = false)
    //@Excel(name = "拓展合同金额")
    @Excel(sort = 3)
    private String htzje;

    @ApiModelProperty(value = "合同年度金额", hidden = false, required = false)
    //@Excel(name = "新增拓展合同年度金额")
    @Excel(sort = 4)
    private String htndje;

    @ApiModelProperty(value = "有效信息提报量", hidden = false, required = false)
    //@Excel(name = "有效信息填报量")
    @Excel(sort = 5)
    private String yxxxtbl;

    @ApiModelProperty(value = "主导拓展合同金额", hidden = false, required = false)
    //@Excel(name = "主导拓展合同金额")
    @Excel(sort = 6)
    private String zdtzhtje;

    @ApiModelProperty(value = "项目保留率", hidden = false, required = false)
    //@Excel(name = "项目保留率")
    @Excel(sort = 7)
    private String xmbll;

    private Long recordId;

    private Long parentId;

    private Integer level;
}
