package com.bytefinger.common.core.constant;

/**
 * 缓存常量信息
 *
 * @author patrick
 */
public class CacheConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    public final static long SSO_TOKEN_EXPIRATION = 60 * 12;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public final static int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    public final static long PASSWORD_LOCK_TIME = 10;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_CNAREA_KEY = "sys_cnarea:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_CNAREA_MAP_KEY = "sys_cnarea_map:";

    /**
     * 业务 cache key
     */
    public static final String BIZ_CODE_KEY = "biz_code:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * IP 尝试次数过多
     */
    public static final String IP_MORE_LOGIN_KEY = "ip_more_login_key:";

    /**
     * 审核消息标题
     */
    public static final String SYS_MESSAGE_APPROVAL_TITLE = "SYS_MESSAGE_APPROVAL_TITLE";


    /**
     * 审核消息标题
     */
    public static final String SYS_MESSAGE_APPROVAL_PROCESS_TITLE = "SYS_MESSAGE_APPROVAL_PROCESS_TITLE";


}
