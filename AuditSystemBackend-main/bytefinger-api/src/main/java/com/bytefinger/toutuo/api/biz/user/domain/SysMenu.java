package com.bytefinger.toutuo.api.biz.user.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value = "SysMenu对象", description = "菜单权限表")
public class SysMenu {

    @ApiModelProperty(value = "菜单ID", hidden = false, required = false)
    @JSONField(name = "menuId", serialize = true)
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty(value = "菜单名称", hidden = false, required = false)
    @JSONField(name = "menuName", serialize = true)
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID", hidden = false, required = false)
    @JSONField(name = "parentId", serialize = true)
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序", hidden = false, required = false)
    @JSONField(name = "orderNum", serialize = true)
    @TableField("order_num")
    private Integer orderNum;

    @ApiModelProperty(value = "路由地址", hidden = false, required = false)
    @JSONField(name = "path", serialize = true)
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "组件路径", hidden = false, required = false)
    @JSONField(name = "component", serialize = true)
    @TableField("component")
    private String component;

    @ApiModelProperty(value = "路由参数", hidden = false, required = false)
    @JSONField(name = "query", serialize = true)
    @TableField("query")
    private String query;

    @ApiModelProperty(value = "是否为外链（0是 1否）", hidden = false, required = false)
    @JSONField(name = "isFrame", serialize = true)
    @TableField("is_frame")
    private Integer isFrame;

    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）", hidden = false, required = false)
    @JSONField(name = "isCache", serialize = true)
    @TableField("is_cache")
    private String isCache;

    @ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）", hidden = false, required = false)
    @JSONField(name = "menuType", serialize = true)
    @TableField("menu_type")
    private String menuType;

    @ApiModelProperty(value = "菜单状态（0显示 1隐藏）", hidden = false, required = false)
    @JSONField(name = "visible", serialize = true)
    @TableField("visible")
    private String visible;

    @ApiModelProperty(value = "菜单状态（0正常 1停用）", hidden = false, required = false)
    @JSONField(name = "status", serialize = true)
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "菜单是否可删除（0可删除 1不可删除）", hidden = false, required = false)
    @JSONField(name = "canDel", serialize = true)
    @TableField("can_del")
    private Integer canDel;

    @ApiModelProperty(value = "权限标识", hidden = false, required = false)
    @JSONField(name = "perms", serialize = true)
    @TableField("perms")
    private String perms;

    @ApiModelProperty(value = "菜单图标", hidden = false, required = false)
    @JSONField(name = "icon", serialize = true)
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "创建者", hidden = false, required = false)
    @JSONField(name = "createBy", serialize = true)
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间", hidden = false, required = false)
    @JSONField(name = "createTime", serialize = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者", hidden = false, required = false)
    @JSONField(name = "updateBy", serialize = true)
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", hidden = false, required = false)
    @JSONField(name = "updateTime", serialize = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "备注", hidden = false, required = false)
    @JSONField(name = "remark", serialize = true)
    @TableField("remark")
    private String remark;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children = Lists.newArrayList();

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }
}