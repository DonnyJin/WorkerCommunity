package com.donny.community.controller;

import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Page;
import com.donny.community.entity.User;
import com.donny.community.service.DiscussPostService;
import com.donny.community.service.UserService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        // 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model
        // 所以在thymeleaf中可以直接访问Page
        page.setRows(discussPostService.getRowsCount(0));
        page.setPath("/index");
        List<DiscussPost> posts = discussPostService.getPosts(0,page.getOffset(), page.getPageSize());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(posts != null) {
            for(DiscussPost post : posts) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.getUser(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}