package com.donny.community.service;

import com.donny.community.entity.LoginTicket;
import com.donny.community.entity.User;
import com.donny.community.util.RedisUtil;

import javax.jws.soap.SOAPBinding;
import java.util.Map;

public interface UserService {

    User getUserById(Integer id);

    Map<String, Object> register(User user);

    Integer activate(Integer userId, String code);

    Map<String, Object> login(String username, String password, Integer expired);

    void logout(String ticket);

    LoginTicket getLoginTicket(String ticket);

    Integer updateHeader(int userId, String headerUrl);

    User findUserByName(String username);

    /**
     * 优先从缓存中取值
     */
    User getCache(Integer userId);

    /**
     * 取不到时初始化缓存
     */
    User initCache(Integer userId);

    /**
     * 数据变更时清楚缓存数据
     */
    void clearCache(Integer userId);
}
