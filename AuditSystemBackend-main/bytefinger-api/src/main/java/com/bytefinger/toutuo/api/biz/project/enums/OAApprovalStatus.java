package com.bytefinger.toutuo.api.biz.project.enums;

import java.util.Objects;

/**
 * @author pat
 * @date 2022/10/26 10:06
 **/
public enum OAApprovalStatus {

    DEFAULT(0, "默认状态（数据尚未提交审批）"),
    SHEN_PI_ZHONG(1, "审批中"),
    TONG_GUO(2, "通过"),
    BO_HUI(3, "驳回"),
    ZUO_FEI(4, "作废"),
    CAO_GAO(5, "草稿"),
    XIAN_XIA_SHEN_PI(8, "线下审批通过"),
    WU_XU_SHEN_PI(9, "无需审批"),
    YI_SHAN_CHU(10, "已删除"),
    ;

    private final Integer code;
    private String desc;

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(final String desc) {
        this.desc = desc;
    }

    OAApprovalStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OAApprovalStatus getByCode(int code) {
        for (OAApprovalStatus value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

}
