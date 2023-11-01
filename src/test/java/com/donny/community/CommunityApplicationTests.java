package com.donny.community;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void postListTest() {
//        List<DiscussPost> list = discussPostMapper.selectPostList(101);
//        System.out.println(list);
    }

}
