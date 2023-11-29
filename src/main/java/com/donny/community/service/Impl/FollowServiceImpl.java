package com.donny.community.service.Impl;

import com.donny.community.entity.User;
import com.donny.community.service.FollowService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.RedisUtil;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService, CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;

    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
                String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);

                redisOperations.multi();
                redisOperations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                redisOperations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return redisOperations.exec();
            }
        });
    }

    @Override
    public void unFollow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
                String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);

                redisOperations.multi();
                redisOperations.opsForZSet().remove(followeeKey, entityId);
                redisOperations.opsForZSet().remove(followerKey, userId);

                return redisOperations.exec();
            }
        });
    }

    @Override
    public Long findFolloweeCount(Integer userId, Integer entityType) {
        String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    @Override
    public Long findFollowerCount(Integer entityType, Integer entityId) {
        String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    @Override
    public Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId) {
        String followeeKey = RedisUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;

    }

    @Override
    public List<Map<String, Object>> findFollowees(Integer userId, Integer offset, Integer limit) {
        String followeeKey = RedisUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        if (targetIds == null) return null;
        List<Map<String, Object>> res = new ArrayList<>();
        for (Integer targetId :  targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.getUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> findFollowers(Integer userId, Integer offset, Integer limit) {
        String followerKey = RedisUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if (targetIds == null) return null;
        List<Map<String, Object>> res = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.getUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            res.add(map);
        }
        return res;
    }


}
