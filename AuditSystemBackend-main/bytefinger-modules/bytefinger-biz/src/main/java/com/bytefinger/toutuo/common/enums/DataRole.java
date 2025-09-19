package com.bytefinger.toutuo.common.enums;

import com.bytefinger.common.security.auth.AuthUtil;

/**
 * 状态
 *
 * @author pat
 * @date 2022/10/09 18:30
 **/
public enum DataRole {

    SHOW_ALL("SHOW_ALL", "查看-全公司", "biz:data:show:all"),
    SHOW_SUB("SHOW_SUB", "查看-同级+下级", "biz:data:show:sub"),
    SHOW_SUB_GUI_SHU_JI_WO_XIA_JI_DE("GUI_SHU_JI_WO_XIA_JI_DE", "归属给我下级的", "biz:data:show:sub"),
    SHOW_SUB_XIA_JI_CAN_YU_DE("XIA_JI_CAN_YU_DE", "下级参与的", "biz:data:show:sub"),

    SHOW_ME("SHOW_ME", "查看-本人", "biz:data:show:me"),
    SHOW_ME_GUI_SHU_JI_WO_DE("GUI_SHU_JI_WO_DE", "归属给我的", "biz:data:show:me"),
    SHOW_ME_WO_CAN_YU_DE("WO_CAN_YU_DE", "我参与的", "biz:data:show:me"),
    SHOW_ME_WO_SHOU_CANG_DE("WO_SHOU_CANG_DE", "我收藏的", "biz:data:show:me"),
    ;

    private final String code;

    private String roleKey;

    public String getCode() {
        return this.code;
    }

    public String getRoleKey() {
        return this.roleKey;
    }

    DataRole(String code, String desc, String roleKey) {
        this.code = code;
        this.roleKey = roleKey;
    }

    /**
     * 当前登录人是否有查看权限
     *
     * @return
     */
    public static boolean currUserHasShowAllOrSub() {
        return AuthUtil.hasPermi(SHOW_ALL.getRoleKey()) || AuthUtil.hasPermi(SHOW_SUB.getRoleKey());
    }

    public static boolean currUserHasShowSub() {
        return AuthUtil.hasPermi(SHOW_SUB.getRoleKey());
    }


    public static boolean currUserHasShowAll() {
        return AuthUtil.hasPermi(SHOW_ALL.getRoleKey());
    }
}
