package com.bytefinger.toutuo.biz.project.enums;

import java.util.Objects;

/**
 * 状态
 *
 * @author pat
 * @date 2022/10/09 18:30
 **/
public enum ProjectStatus {

    LI_XIANG_ZHUN_BEI_ZHONG("LI_XIANG_ZHUN_BEI_ZHONG", "立项准备中"),
    LI_XIANG_CHENG_GONG("LI_XIANG_CHENG_GONG", "立项成功"),
    TUO_ZHAN_ZHONG("TUO_ZHAN_ZHONG", "拓展中"),
    TUO_ZHAN_CHENG_GONG("TUO_ZHAN_CHENG_GONG", "拓展成功"),
    ZAI_GUAN("ZAI_GUAN", "在管"),
    YI_ZHONG_ZHI("YI_ZHONG_ZHI", "已终止"),
    TOU_BIAO_ZHONG("TOU_BIAO_ZHONG", "投标中"),
    YI_ZHONG_BIAO("YI_ZHONG_BIAO", "已中标"),
    WEI_ZHONG_BIAO("WEI_ZHONG_BIAO", "未中标"),
    JIN_DIAO_ZHONG("JIN_DIAO_ZHONG", "尽调中"),
    PING_SHEN_ZHONG("PING_SHEN_ZHONG", "评审中"),
    WANG_CHENG_QIAN_YUE("WANG_CHENG_QIAN_YUE", "完成签约"),
    YUN_YING_QI("YUN_YING_QI", "运营期"),
    YI_DAO_QI("YI_DAO_QI", "已到期"),
    YI_WAN_JIE("YI_WAN_JIE", "已完结"),
    YI_FEI_ZHI("YI_FEI_ZHI", "已废止"),
    YI_SHI_XIAO("YI_SHI_XIAO", "已失效"),
    YOU_XIAO("YOU_XIAO", "有效"),
    CLOSE_FILE("CLOSE_FILE", "已归档"),

    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectStatus getByCode(String code) {
        for (ProjectStatus value : ProjectStatus.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectStatus isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
