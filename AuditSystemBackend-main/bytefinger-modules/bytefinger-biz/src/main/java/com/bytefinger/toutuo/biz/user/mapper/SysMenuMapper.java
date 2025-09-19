package com.bytefinger.toutuo.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.api.biz.user.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据岗位获取菜单权限标识
     *
     * @param postId
     * @return
     */
    public List<String> selectMenuPermsByPostId(Long postId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu
     * @return
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * 查询系统菜单列表
     *
     * @param userId
     * @param deptId
     * @param postId
     * @return
     */
    public List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("deptId") Long deptId, @Param("postId") Long postId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 根据岗位ID查询菜单树信息
     *
     * @param postId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return
     */
    public List<Long> selectMenuListByPostId(@Param("postId") Long postId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int hasChildByMenuId(Long menuId);


    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
