package com.bytefinger.toutuo.biz.project.enums;


/**
 * 项目通告类型
 *
 * @author lin
 * @date 2022/10/09 18:30
 **/
public enum NotifyTypeEnums {

    XIANG_MU_HE_TONG_DAO_QI_WEI_JI_SHI_CHU_LI("XIANG_MU_HE_TONG_DAO_QI_WEI_JI_SHI_CHU_LI", "项目合同到期未及时处理"),
    XIANG_MU_WEI_JIN_XING_BIAO_SHU_HE_GUI_PING_SHEN("XIANG_MU_WEI_JIN_XING_BIAO_SHU_HE_GUI_PING_SHEN", "项目未进行标书合规评审"),
    XIANG_MU_KAI_BIAO_WEI_JI_SHI_FU_PAN("XIANG_MU_KAI_BIAO_WEI_JI_SHI_FU_PAN", "项目开标未及时复盘")

    ;

    private final String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    NotifyTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code){
        for (NotifyTypeEnums typeEnums : NotifyTypeEnums.values()){
            if (code.equals(typeEnums.getCode())){
                return typeEnums.getDesc();
            }
        }
        return "";
    }

}
