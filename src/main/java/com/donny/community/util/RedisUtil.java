package com.donny.community.util;

public class RedisUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";


    /**
     *某个实体的赞
     * like:entityType:entityId -> set(userId)
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(Integer entityType, Integer entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 获取某个用户的赞
     * like:user:userId -> int
     * @param userId
     * @return
     */
    public static String getUserLikeKey(Integer userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
}
