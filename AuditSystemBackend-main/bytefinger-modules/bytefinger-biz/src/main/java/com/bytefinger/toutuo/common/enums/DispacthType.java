package com.bytefinger.toutuo.common.enums;

/**
 * @author pat
 * @date 2022/10/21 10:43
 **/
public enum DispacthType {

    SHANG_JI_FEN_PEI("SHANG_JI_FEN_PEI", "上级分派"),
    ZHU_DONG_REN_LING("ZHU_DONG_REN_LING", "主动认领"),
    SHANG_JI_ZHUAN_PAI("SHANG_JI_ZHUAN_PAI", "上级转派"),
    TONG_JI_ZHUAN_PAI("TONG_JI_ZHUAN_PAI", "同级转派"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    DispacthType(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
