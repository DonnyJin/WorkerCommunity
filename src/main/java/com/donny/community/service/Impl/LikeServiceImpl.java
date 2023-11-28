package com.donny.community.service.Impl;

import com.donny.community.service.LikeService;
import com.donny.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {


    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;



    @Override
    public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisUtil.getUserLikeKey(entityUserId);
                // redis的查询操作不能放在事务中
                Boolean isMember = redisOperations.opsForSet().isMember(entityLikeKey, userId);
                redisOperations.multi();
                if (Boolean.TRUE.equals(isMember)) {
                    redisOperations.opsForSet().remove(entityLikeKey, userId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                } else {
                    redisOperations.opsForSet().add(entityLikeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }
                return redisOperations.exec();
            }
        });
    }

    @Override
    public Long findEntityLikeCount(Integer entityType, Integer entityId) {
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    @Override
    public Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    /**
     * 查询某个用户获得的赞
     * @param userId
     * @return
     */
    public Integer findUserLikeCount(Integer userId) {
        String userLikeKey = RedisUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }
}

