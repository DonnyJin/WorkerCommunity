package com.donny.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate template;

    @Test
    void testString() {
        String redisKey = "test:count";
        ValueOperations ops = template.opsForValue();
        ops.set(redisKey, 1);
        System.out.println(ops.get(redisKey));
        System.out.println(ops.increment(redisKey));
        System.out.println(ops.decrement(redisKey));
    }

    @Test
    void testHash() {
        String redisKey = "test:user";
        HashOperations ops = template.opsForHash();
        ops.put(redisKey, "id", 22);
        ops.put(redisKey, "username", "Donny");

        System.out.println(ops.get(redisKey, "username"));
        System.out.println(ops.get(redisKey, "id"));
    }

    @Test
    void testLists() {
        String redisKey = "test:ids";

        ListOperations ops = template.opsForList();
        ops.leftPush(redisKey, 101);
        ops.leftPush(redisKey, 102);
        ops.leftPush(redisKey, 103);

        System.out.println(ops.size(redisKey));
        System.out.println(ops.index(redisKey, 0) );
        System.out.println(ops.range(redisKey, 0, 2) );
    }

    @Test
    void testSets() {
        String redisKey = "test:teachers";
        SetOperations ops = template.opsForSet();
        ops.add(redisKey, "Donny", "Spring", "yxr", "Donny");
        System.out.println(ops.members(redisKey));
        System.out.println(ops.pop(redisKey));
        System.out.println(ops.members(redisKey));
    }
}
