package com.donny.community.service.Impl;

import com.donny.community.service.DataService;
import com.donny.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void recordUV(String ip) {
        String uvKey = RedisUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(uvKey, ip);
    }

    @Override
    public Long calculateUV(Date start, Date end) {
        if (start == null || end == null) throw new IllegalArgumentException("参数不能为空!");
        if (start.getTime() == end.getTime()) {
            String key = RedisUtil.getUVKey(df.format(start));
            return redisTemplate.opsForHyperLogLog().size(key);
        }
        List<String> keys = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        while(!calendar.getTime().after(end)) {
            String uvKey = RedisUtil.getUVKey(df.format(calendar.getTime()));
            keys.add(uvKey);
            calendar.add(Calendar.DATE, 1);
        }
        // 合并数据
        String key = RedisUtil.getUVKey(df.format(start), df.format(end));
        redisTemplate.opsForHyperLogLog().union(key, keys.toArray());

        return redisTemplate.opsForHyperLogLog().size(key);
    }

    @Override
    public void recordDAU(Integer userId) {
        String dauKey = RedisUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(dauKey, userId, true);
    }

    @Override
    public Long calculateDAU(Date start, Date end) {
        if (start == null || end == null) throw new IllegalArgumentException("参数不能为空!");
        List<byte[]> keys = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        while(!calendar.getTime().after(end)) {
            String uvKey = RedisUtil.getDAUKey(df.format(calendar.getTime()));
            keys.add(uvKey.getBytes());
            calendar.add(Calendar.DATE, 1);
        }
        // 合并数据
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                String dauKey = RedisUtil.getDAUKey(df.format(start), df.format(end));
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR, dauKey.getBytes(), keys.toArray(new byte[0][0]));
                return redisConnection.bitCount(dauKey.getBytes());
            }
        });
    }
}
