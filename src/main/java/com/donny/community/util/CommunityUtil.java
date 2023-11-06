package com.donny.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * key拼上一个随机字符串然后MD5加密
     * @param key
     * @return
     */
    public static String md5Encode(String key) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
