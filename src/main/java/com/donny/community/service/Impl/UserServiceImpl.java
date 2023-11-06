package com.donny.community.service.Impl;

import com.donny.community.dao.UserMapper;
import com.donny.community.entity.User;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.MailClient;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine engine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.context-path}")
    private String contextPath;


    @Override
    public User getUser(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        HashMap<String, Object> map = new HashMap<>();
        if(user == null) throw new IllegalArgumentException("参数不能为空");
        if(!StringUtils.hasText(user.getUsername())) {
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(!StringUtils.hasText(user.getPassword())) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(!StringUtils.hasText(user.getEmail())) {
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //验证账号
        User user1 = userMapper.selectByName(user.getUsername());
        if(user1 != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }
        user1 = userMapper.selectByEmail(user.getEmail());
        if(user1 != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5Encode(user.getPassword()) + user.getSalt());
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(LocalDateTime.now());

        userMapper.insertUser(user);
        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8082/community/activation/101/code
        String url = domain + contextPath + "/activation" + user.getId() + "/" + user.getActivationCode();

        return map;
    }


}
