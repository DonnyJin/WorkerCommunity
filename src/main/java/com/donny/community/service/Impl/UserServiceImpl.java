package com.donny.community.service.Impl;

import com.donny.community.dao.UserMapper;
import com.donny.community.entity.User;
import com.donny.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Integer id) {
        return userMapper.selectById(id);
    }
}
