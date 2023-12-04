package com.donny.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
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

    /**
     * 统计20w个数据的独立总数
     */
    @Test
    void testHyberLogLog() {
        String redisKey = "test:hyberloglog:01";
        for (int i = 1 ; i <= 100000 ; i++) {
            template.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 1 ; i <= 100000 ; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            template.opsForHyperLogLog().add(redisKey, r);
        }

        System.out.println(template.opsForHyperLogLog().size(redisKey));
    }

    /**
     * 讲3组数据合并，再统计合并后的重读数据的独立总数
     */
    @Test
    public void testHyberLogLogUnit() {
        String redisKey2 = "test:hyberloglog:02";
        for (int i = 1 ; i <= 10000 ; i++) {
            template.opsForHyperLogLog().add(redisKey2, i);
        }

        String redisKey3 = "test:hyberloglog:03";
        for (int i = 5001 ; i <= 15000 ; i++) {
            template.opsForHyperLogLog().add(redisKey3, i);
        }
        String redisKey4 = "test:hyberloglog:04";
        for (int i = 10001 ; i <= 20000 ; i++) {
            template.opsForHyperLogLog().add(redisKey4, i);
        }
        String unionKey = "test:hyberloglog:union";
        template.opsForHyperLogLog().union(unionKey, redisKey4, redisKey2, redisKey3);
        System.out.println(template.opsForHyperLogLog().size(unionKey));
    }

    /**
     * 统计一组数据的bool
     */
    @Test
    void testBitmap() {
        String redisKey = "test:bm:01";

        template.opsForValue().setBit(redisKey, 1, true);
        template.opsForValue().setBit(redisKey, 4, true);
        template.opsForValue().setBit(redisKey, 7, true);

        Object obj = template.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }
}
