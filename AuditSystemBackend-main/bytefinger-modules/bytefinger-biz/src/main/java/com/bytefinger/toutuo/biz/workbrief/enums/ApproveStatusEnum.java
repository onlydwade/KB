package com.bytefinger.toutuo.biz.workbrief.enums;

/**
 * 审批状态
 *
 * @author lin
 * @date 2023/01/03
 **/
public enum ApproveStatusEnum {

    UNAPPROVED(0, "未发起"),
    APPROVAL_DAI_QUE_REN(5, "审批待确认"),
    IN_APPROVAL(1, "审批中"),
    APPROVED(2, "审批通过"),
    APPROVAL_REJECTION(3, "审批驳回"),
    APPROVAL_FEI_QI(4, "审批废弃"),
    APPROVAL_SAHN_CHU(10, "审批已删除"),
    APPROVAL_REJECTION_XIAN_XIA(100, "线下审批通过")
    ;

    private Integer code;

    private String name;

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    ApproveStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getEnumName(Integer code) {
        String name="";
        for (ApproveStatusEnum stateEnum: ApproveStatusEnum.values()) {
            if(stateEnum.code.equals(code)){
                name=stateEnum.name;
                break;
            }
        }
        return name;
    }

}
