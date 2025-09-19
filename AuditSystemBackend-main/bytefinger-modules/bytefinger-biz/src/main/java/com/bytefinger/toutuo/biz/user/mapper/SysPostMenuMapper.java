package com.bytefinger.toutuo.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.user.domain.SysPostMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 岗位和菜单关联表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-13
 */
@Mapper
public interface SysPostMenuMapper extends BaseMapper<SysPostMenu> {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistPost(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param postId 角色ID
     * @return 结果
     */
    public int deletePostMenuByPostId(Long postId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePostMenu(Long[] ids);

    /**
     * 批量新增角色菜单信息
     *
     * @param postMenuList 角色菜单列表
     * @return 结果
     */
    public int batchPostMenu(List<SysPostMenu> postMenuList);
}
