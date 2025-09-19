package com.bytefinger.toutuo.biz.rules.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 规则枚举数据
 * </p>
 *
 * @author patrick
 * @since 2022-11-15
 */
@Data
@Accessors(chain = true)
@TableName("biz_rules_dict")
@ApiModel(value = "规则枚举数据", description = "规则枚举数据")
public class RulesDict extends BaseEntity {

    @ApiModelProperty(value = "类型（用途）描述", hidden = false, required = false)
    @TableField("rule_group")
    private String ruleGroup;

    @ApiModelProperty(value = "规则的key枚举值", hidden = false, required = false)
    @TableField("rule_key")
    private String ruleKey;

    @ApiModelProperty(value = "此条对应的字段，或者model名字", hidden = false, required = false)
    @TableField("rule_field")
    private String ruleField;

    @ApiModelProperty(value = "字段对应的值", hidden = false, required = false)
    @TableField("rule_value")
    private String ruleValue;

    @ApiModelProperty(value = "规则描述", hidden = false, required = false)
    @TableField("rule_desc")
    private String ruleDesc;

    @ApiModelProperty(value = "显示排序", hidden = false, required = false)
    @TableField("sorts")
    private Integer sorts;

    @ApiModelProperty(value = "附加值", hidden = false, required = false)
    @TableField("other_val")
    private String otherVal;

}