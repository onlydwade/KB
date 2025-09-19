package com.bytefinger.toutuo.biz.rules.enums;

import java.util.Objects;

/**
 * 规则对象
 *
 * @author pat
 * @date 2022/11/11 09:00
 **/
public enum RulesVal {

    RULES_MODE_NAME("RULES_MODE_NAME", "模块"),

    //拓展模块
    //模块枚举
    XIANG_MU("XIANG_MU", "项目"),
    RULES_FIELD_TYPE("RULES_FIELD_TYPE", "规则条件类型"),
    RULES_ACTION_TYPE("RULES_ACTION_TYPE", "规则动作类型"),
    YE_WU("YE_WU", "业务字段"),
    XI_TONG("XI_TONG", "系统字段"),
    SHI_JIAN("SHI_JIAN", "时间字段"),
    XIAO_XI_TONG_ZHI("XIAO_XI_TONG_ZHI", "消息通知"),
    BIAN_GENG_ZHI("BIAN_GENG_ZHI", "变更值"),
    DEPT_ID("DEPT_ID", "负责主体"),
    GUI_ZE_FU_HAO("GUI_ZE_FU_HAO", "规则符号"),
    SHI_JIAN_ZHOU_QI("SHI_JIAN_ZHOU_QI", "时间周期"),

    //投拓模块
    //模块枚举
    TOUTUO_OPERATE("TOUTUO_OPERATE", "投拓运营"),
    //时间字段
    SHI_JIAN_TOUTUO("SHI_JIAN_TOUTUO", "时间字段"),
    //业务字段
    YE_WU_TOUTUO("YE_WU_TOUTUO", "业务字段"),
    //消息通知
    XIAO_XI_TONG_ZHI_OPERATE("XIAO_XI_TONG_ZHI_TOUTUO", "消息通知"),

    //客户模块
    //模块枚举
    TOUTUO_CUSTOMER("CUSTOMER", "客户"),

    OA_TODO_REMIND("OA_TODO_REMIND", "项目OA待办提醒"),
    //时间字段
    SHI_JIAN_CUSTOMER("SHI_JIAN_CUSTOMER", "时间字段"),
    //业务字段
    YE_WU_CUSTOMER("YE_WU_CUSTOMER", "业务字段"),
    XIAO_XI_TONG_ZHI_CUSTOMER("XIAO_XI_TONG_ZHI_CUSTOMER", "消息通知"),

    XIAO_XI_TONG_ZHI_OA_TODO_REMIND("XIAO_XI_TONG_ZHI_OA_TODO_REMIND", "消息通知"),

    //特殊字段拼接SQL的判断枚举
    TOUTUO_OPERATE_HE_TONG_FU_WU_KAI_SHI_SHI_JIAN("TOUTUO_OPERATE_HE_TONG_FU_WU_KAI_SHI_SHI_JIAN", "合同服务开始时间"),
    //如果存在以下值需要联表查询
    TOUTUO_OPERATE_GAN_YU_XIA_DA_SHI_JIAN("TOUTUO_OPERATE_GAN_YU_XIA_DA_SHI_JIAN", "干预下达期限"),
    TOUTUO_OPERATE_GAN_YU_SHI_SHI_QI_XIAN("TOUTUO_OPERATE_GAN_YU_SHI_SHI_QI_XIAN", "干预实施期限"),
    TOUTUO_OPERATE_GAN_YU_ZHI_XING_ZHUANG_TAI("TOUTUO_OPERATE_GAN_YU_ZHI_XING_ZHUANG_TAI", "干预执行状态"),
    PROJECT_EXPIRATION_REMIND("PROJECT_EXPIRATION_REMIND", "项目合同到期监管提醒"),
    PROJECT_BE_ABOUT_TO_BID_OPENING_REMIND("PROJECT_BE_ABOUT_TO_BID_OPENING_REMIND", "开标评审监管提醒"),
    PROJECT_BID_REVIEW_REMIND("PROJECT_BID_REVIEW_REMIND", "投标复盘监管提醒"),
    PROJECT_COMPLETION_AND_HANDOVER_REMIND("PROJECT_COMPLETION_AND_HANDOVER_REMIND", "项目结项移交监管提醒"),
    PROJECT_LONG_TERM_UNMAINTAINED_REMIND("PROJECT_LONG_TERM_UNMAINTAINED_REMIND", "长期未维护项目监管提醒"),
    ;



    private String code;
    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    RulesVal(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code){
        for (RulesVal rule : RulesVal.values()){
            if (code.equals(rule.getCode())){
                return rule.getDesc();
            }
        }
        return "";
    }

    public static String getCodeByDesc(String desc){
        for (RulesVal rule : RulesVal.values()){
            if (desc.equals(rule.getDesc())){
                return rule.getCode();
            }
        }
        return "";
    }

}
