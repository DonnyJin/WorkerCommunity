package com.donny.community.controller;

import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.User;
import com.donny.community.service.DiscussPostService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "用户还未登录!");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);
        // 报错的情况以后统一处理
        return CommunityUtil.getJSONString(0, "发布成功");
    }

    @GetMapping("/detail/{id}")
    public String getDiscussPost(@PathVariable Integer id, Model model) {
        DiscussPost post = discussPostService.findDiscussPost(id);
        model.addAttribute("post", post);
        // 作者
        User user = userService.getUser(post.getUserId());
        model.addAttribute("user", user);

        return "site/discuss-detail";

    }
}
