package com.bytefinger.toutuo.biz.rules.enums;

/**
 * 描述:
 *   规则数据内定义的规则与主表之间关联表枚举，用于判断发送多条消息
 * @author LaiDaCheng
 * @email dacheng.lai@bytefinger.com
 * @date 2023/4/27 11:09
 */
public enum RulesIsAssociationTableEnum {
    //无关联表
    NONE(""),
    //关联项目经营评估表
    BIZ_PROJECT_ASSESS("biz_project_assess"),
    //关联项目干预表
    BIZ_PROJECT_INTERVENE("biz_project_intervene"),
    //关联经营评估和项目干预表
    BIZ_PROJECT_ASSESS_AND_BIZ_PROJECT_INTERVENE("biz_project_assess,biz_project_intervene");
    private String code;

    RulesIsAssociationTableEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
