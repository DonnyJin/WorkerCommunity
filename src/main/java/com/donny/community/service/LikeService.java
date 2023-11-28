package com.donny.community.service;

public interface LikeService {

    void like(Integer userId, Integer entityType, Integer entityId);

    Long findEntityLikeCount(Integer entityType, Integer entityId);

    // 查询某人对某实体的点赞状态
    Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId);
}
