package com.donny.community.util;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    Integer ACTIVATION_SUCCESS = 1;
    /**
     * 重复激活
     */
    Integer ACTIVATION_REPEAT = 2;

    /**
     * 激活失败
     */
    Integer ACTIVATION_FAIL = 0;

    /**
     * 默认的登录凭证超时时间（秒）
     */
    Integer DEFAULT_EXPIRED = 3600 * 12;

    /**
     * 记住我勾选后的超时时间(秒)
     */
    Integer REMEMBER_EXPIRED = 3600 * 24 * 14;
}
