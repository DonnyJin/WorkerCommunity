package com.donny.community.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    void follow(Integer userId, Integer entityType, Integer entityId);

    void unFollow(Integer userId, Integer entityType, Integer entityId);

    /**
     * 查询关注的实体数量
     */
    Long findFolloweeCount(Integer userId, Integer entityType);

    /**
     * 找到实体的粉丝数量
     */
    Long findFollowerCount(Integer entityType, Integer entityId);


    Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId);

    /**
     * 查询某个用户的关注列表
     */
    List<Map<String, Object>> findFollowees(Integer userId, Integer offset, Integer limit);

    /**
     * 查询某个用户的粉丝列表
     */
    List<Map<String, Object>> findFollowers(Integer userId, Integer offset, Integer limit);

}
