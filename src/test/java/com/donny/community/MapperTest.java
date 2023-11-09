package com.donny.community;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.dao.LoginTicketMapper;
import com.donny.community.dao.UserMapper;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.LoginTicket;
import com.donny.community.entity.User;
import com.donny.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostService discussPostService;
    @Test
    void selectUserTest() {
//        User user = userMapper.selectById(101);
//        User liubei = userMapper.selectByName("liubei");
//        User liubei2 = userMapper.selectByEmail("nowcoder101@sina.com");
//        System.out.println(user);
//        System.out.println(liubei);
//        System.out.println(liubei2);
        List<DiscussPost> posts = discussPostService.getPosts(0,0,10);
        System.out.println(posts);
    }

    @Test
    void insertUserTest() {
        User user = new User();
        user.setPassword("123456");
        user.setSalt("123");
        user.setEmail("123@gmail.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(LocalDateTime.now());

        System.out.println(userMapper.insertUser(user));
    }

    @Test
    void updateUserTest() {
        System.out.println(userMapper.updateHeader(150, "http://www.nowcoder.com/102.png"));
    }

    @Test
    void loginTicketTest() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(LocalDateTime.now());
        loginTicket.setUserId(2222);
//        loginTicketMapper.insertLoginTicket(loginTicket);
        loginTicketMapper.updateStatus(loginTicket.getTicket(), 1);
        LoginTicket selected = loginTicketMapper.selectByTicket(loginTicket.getTicket());
        System.out.println(selected);
    }
}
