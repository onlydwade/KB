package com.bytefinger.toutuo.biz.workbrief.enums;

/**
 * 项目类型
 *
 * @author lin
 * @date 2023/01/03
 **/
public enum ProjectTypeEnum {

    COOPERATION(1, "战略合作"),
    EQUITY_COOPERATION(2, "股权合作"),
    KEY_PRODUCTION_AND_LOGISTICS_INDUSTRIES(3, "生产后勤重点业"),
    OTHER_KEY_PROJECTS(4, "其他重点项目")
    ;

    private Integer code;

    private String name;

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    ProjectTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumName(Integer code) {
        String name="";
        for (ProjectTypeEnum stateEnum: ProjectTypeEnum.values()) {
            if(stateEnum.code.equals(code)){
                name=stateEnum.name;
                break;
            }
        }
        return name;
    }

}
