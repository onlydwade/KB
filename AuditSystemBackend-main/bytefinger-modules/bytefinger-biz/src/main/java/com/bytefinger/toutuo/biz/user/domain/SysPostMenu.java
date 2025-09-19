package com.bytefinger.toutuo.biz.user.domain;

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

/**
 * <p>
 * 岗位和菜单关联表
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_post_menu")
@ApiModel(value = "SysPostMenu对象", description = "岗位和菜单关联表")
public class SysPostMenu extends Model<SysPostMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "岗位id", hidden = false, required = false)
    @JSONField(name = "postId", serialize = true)
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;

    @ApiModelProperty(value = "菜单id", hidden = false, required = false)
    @JSONField(name = "menuId", serialize = true)
    @TableField("menu_id")
    private Long menuId;

}