package com.bytefinger.toutuo.biz.keyword.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 业务关键词
 * </p>
 *
 * @author patrick
 * @since 2022-10-19
 */
@Data
@TableName("biz_keywords")
@ApiModel(value = "Keywords对象", description = "业务关键词")
public class Keywords extends BaseEntity {

    @ApiModelProperty(value = "关键词类型 1-线索 2-商机 3-项目", hidden = false, required = false)
    @HistoryFieldLog("关键词类型 1-线索 2-商机 3-项目")
    @TableField("keyword_type")
    private Integer keywordType;

    @ApiModelProperty(value = "关键词内容", hidden = false, required = false)
    @HistoryFieldLog("关键词内容")
    @TableField("keyword_content")
    private String keywordContent;

    @ApiModelProperty(value = "排序", hidden = false, required = false)
    @HistoryFieldLog("排序")
    @TableField("sorts")
    private Integer sorts;


}