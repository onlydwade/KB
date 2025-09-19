package com.bytefinger.toutuo.biz.projectextension.enums;

import java.util.Objects;

/**
 * 风险评估
 *
 * @author ycj
 * @date 2023/3/23
 **/
public enum ProjectNotify {

    Interven("项目干预提醒通知", "请尽快执行干预方案"),
    IntervenExamineStair("干预情况一级检查提示", "请检查干预方案"),
    IntervenExaminesecond("干预情况二级检查提示", "请检查干预方案"),
    Pass("执行通过通知", "干预方案通过"),
    NotPass("执行不通过通知", "执行不通过通知"),
    ExtensionDelivery("项目已进入在管项目库中", "请进行管理维护"),
    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    ProjectNotify(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectNotify getByCode(int code) {
        for (ProjectNotify value : ProjectNotify.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public static ProjectNotify isExpire(int code) {
//        for (ProjectStatus value : new ProjectStatus[]{ProjectStatus.TOU_BIAO_SHI_BAI, ProjectStatus.ZAI_GUAN, ProjectStatus.YI_DAO_QI}) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
        return null;
    }
}
