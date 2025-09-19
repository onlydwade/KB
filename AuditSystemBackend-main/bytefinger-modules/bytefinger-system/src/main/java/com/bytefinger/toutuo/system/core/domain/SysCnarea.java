package com.bytefinger.toutuo.system.core.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 中国行政地区表
 * </p>
 *
 * @author patrick
 * @since 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_cnarea")
@ApiModel(value = "SysCnarea对象", description = "中国行政地区表")
public class SysCnarea extends Model<SysCnarea> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "层级", hidden = false, required = false)
    @JSONField(name = "level", serialize = true)
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "父级行政代码", hidden = false, required = false)
    @JSONField(name = "parentCode", serialize = true)
    @TableField("parent_code")
    private Long parentCode;

    @ApiModelProperty(value = "行政代码", hidden = false, required = false)
    @JSONField(name = "areaCode", serialize = true)
    @TableField("area_code")
    private Long areaCode;

    @ApiModelProperty(value = "邮政编码", hidden = false, required = false)
    @JSONField(name = "zipCode", serialize = true)
    @TableField("zip_code")
    private Integer zipCode;

    @ApiModelProperty(value = "区号", hidden = false, required = false)
    @JSONField(name = "cityCode", serialize = true)
    @TableField("city_code")
    private String cityCode;

    @ApiModelProperty(value = "名称", hidden = false, required = false)
    @JSONField(name = "name", serialize = true)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "简称", hidden = false, required = false)
    @JSONField(name = "shortName", serialize = true)
    @TableField("short_name")
    private String shortName;

    @ApiModelProperty(value = "组合名", hidden = false, required = false)
    @JSONField(name = "mergerName", serialize = true)
    @TableField("merger_name")
    private String mergerName;

    @ApiModelProperty(value = "拼音", hidden = false, required = false)
    @JSONField(name = "pinyin", serialize = true)
    @TableField("pinyin")
    private String pinyin;

    @ApiModelProperty(value = "经度", hidden = false, required = false)
    @JSONField(name = "lng", serialize = true)
    @TableField("lng")
    private BigDecimal lng;

    @ApiModelProperty(value = "纬度", hidden = false, required = false)
    @JSONField(name = "lat", serialize = true)
    @TableField("lat")
    private BigDecimal lat;

}