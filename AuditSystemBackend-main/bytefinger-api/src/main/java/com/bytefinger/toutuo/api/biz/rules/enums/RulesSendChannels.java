package com.bytefinger.toutuo.api.biz.rules.enums;

import java.util.Objects;

/**
 * 发送平台
 *
 * @author pat
 * @date 2022/11/15 13:47
 **/
public enum RulesSendChannels {

    ZHAI_XING_PING_TAI("ZHAI_XING_PING_TAI", "摘星平台"),
    RUN_GONG_ZUO("RUN_GONG_ZUO", "润工作"),
    YOU_JIAN_TONG_ZHI("YOU_JIAN_TONG_ZHI", "邮件通知"),
    ;

    private String dict;
    private String desc;

    public String getDict() {
        return this.dict;
    }

    public String getDesc() {
        return this.desc;
    }

    RulesSendChannels(final String dict, final String desc) {
        this.dict = dict;
        this.desc = desc;
    }

    public static RulesSendChannels getByCode(String dict) {
        for (RulesSendChannels value : values()) {
            if (Objects.equals(dict, value.getDict())) {
                return value;
            }
        }
        return null;
    }

}
