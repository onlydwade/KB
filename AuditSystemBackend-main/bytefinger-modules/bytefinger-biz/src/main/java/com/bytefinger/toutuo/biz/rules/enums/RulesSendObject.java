package com.bytefinger.toutuo.biz.rules.enums;

import java.util.Objects;

/**
 * 发送对象
 *
 * @author pat
 * @date 2022/11/15 13:47
 **/
public enum RulesSendObject {

    //客户
    //客户维护人
    KE_HU_WEI_HU_REN("KE_HU_WEI_HU_REN", "maintenance_user_id", "客户维护人"),

    //投展项目的
    GUI_SHU_REN("GUI_SHU_REN", "attributor_user_id", "归属人"),
    TUO_ZHAN_GUI_SHU_REN("TUO_ZHAN_GUI_SHU_REN", "attributor_user_id", "拓展归属人"),
    //也叫团队成员
    CAN_YU_REN("CAN_YU_REN", "users", "参与人"),
    FU_ZE_REN("FU_ZE_REN", "company_id", "项目负责主体负责人"),


    //拓后负责人(拓展和投拓都需要)
    TUO_HOU_FU_ZE_REN("TUO_HOU_FU_ZE_REN", "principal_id", "拓后负责人"),

    XIANG_MU_CHUANG_JIAN_REN("XIANG_MU_CHUANG_JIAN_REN", "create_user_id", "项目创建人"),

    //投拓运营的
    //总部运营部管理负责人
    ZONG_BU_YUN_YING_BU_GUAN_LI_FU_ZE_REN("ZONG_BU_YUN_YING_BU_GUAN_LI_FU_ZE_REN", null, "总部运营部管理负责人"),
    //方案实施人
    FANG_AN_SHI_SHI_REN("FANG_AN_SHI_SHI_REN", "scheme_user_id", "方案实施人");


    private String dict;
    private String tableFieldName;
    private String desc;

    public String getDict() {
        return this.dict;
    }

    public String getTableFieldName() {
        return this.tableFieldName;
    }

    public String getDesc() {
        return this.desc;
    }

    RulesSendObject(final String dict, final String tableFieldName, final String desc) {
        this.dict = dict;
        this.tableFieldName = tableFieldName;
        this.desc = desc;
    }

    public static RulesSendObject getByCode(String dict) {
        for (RulesSendObject value : values()) {
            if (Objects.equals(dict, value.getDict())) {
                return value;
            }
        }
        return null;
    }

}
