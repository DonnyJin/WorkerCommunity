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

    /**
     * 实体类型：帖子
     */
    Integer ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    Integer ENTITY_TYPE_COMMENT = 2;

    /**
     * 用户
     */
    Integer ENTITY_TYPE_USER = 3;

    /**
     * 主题:评论、点赞、关注、发帖、删帖
     */
    String TOPIC_COMMENT = "comment";
    String TOPIC_LIKE = "like";
    String TOPIC_FOLLOW = "follow";
    String TOPIC_PUBLISH = "publish";
    String TOPIC_DELETE = "delete";
    /**
     * 系统ID
     */
    Integer SYSTEM_USER_ID = 1;

    /**
     * 权限：普通用户
     */
    String AUTHORITY_USER = "user";
    /**
     * 权限: 管理员
     */
    String AUTHORITY_ADMIN = "admin";
    /**
     * 权限: 版主
     */
    String AUTHORITY_MODERATOR = "moderator";
}
