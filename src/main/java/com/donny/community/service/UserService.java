package com.donny.community.service;

import com.donny.community.entity.User;

import java.util.Map;

public interface UserService {

    User getUser(Integer id);

    Map<String, Object> register(User user);

    Integer activate(Integer userId, String code);

}
