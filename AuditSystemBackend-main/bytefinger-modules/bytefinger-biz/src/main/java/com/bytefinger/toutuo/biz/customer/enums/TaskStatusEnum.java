package com.bytefinger.toutuo.biz.customer.enums;

/**
 * 客户跟进记录明细-任务情况
 *
 * @author lin
 * @date 2023/01/03
 **/
public enum TaskStatusEnum {

    CHI_XUN_GEN_JIN("CHI_XUN_GEN_JIN", "持续跟进"),
    TING_ZHI("TING_ZHI", "停止"),
    JIE_SHU_GEN_JIN("JIE_SHU_GEN_JIN", "结束跟进");

    private String code;

    private String name;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    TaskStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumName(Integer code) {
        String name="";
        for (TaskStatusEnum stateEnum: TaskStatusEnum.values()) {
            if(stateEnum.code.equals(code)){
                name=stateEnum.name;
                break;
            }
        }
        return name;
    }

}
