package com.donny.community.service;

import com.donny.community.entity.LoginTicket;
import com.donny.community.entity.User;

import java.util.Map;

public interface UserService {

    User getUserById(Integer id);

    Map<String, Object> register(User user);

    Integer activate(Integer userId, String code);

    Map<String, Object> login(String username, String password, Integer expired);

    void logout(String ticket);

    LoginTicket getLoginTicket(String ticket);

    Integer updateHeader(int userId, String headerUrl);

}
