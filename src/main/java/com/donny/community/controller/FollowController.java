package com.donny.community.controller;

import com.donny.community.annotation.LoginRequired;
import com.donny.community.entity.Event;
import com.donny.community.entity.Page;
import com.donny.community.entity.User;
import com.donny.community.event.EventProducer;
import com.donny.community.service.FollowService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer producer;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;

    @PostMapping("/follow")
    @ResponseBody
    @LoginRequired
    public String follow(Integer entityType, Integer entityId) {
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        // 触发关注事件
        Event event = new Event();
        event.setTopic(TOPIC_FOLLOW);
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setEntityUserId(entityId);

        producer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "已关注");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    @LoginRequired
    public String unFollow(Integer entityType, Integer entityId) {
        User user = hostHolder.getUser();

        followService.unFollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注");
    }

    @GetMapping("/followees/{userId}")
    @LoginRequired
    public String getFollowees(@PathVariable Integer userId, Page page, Model model) {
        User user = userService.getUserById(userId);
        if (user == null) throw new RuntimeException("该用户不存在!");
        model.addAttribute("user", user);
        page.setPageSize(5);
        page.setPath("/followees/" + userId);
        page.setRows(followService.findFolloweeCount(userId, ENTITY_TYPE_USER).intValue());
        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getPageSize());
        if (followees != null) {
            for (Map<String, Object> map : followees) {
                User user1 = (User) map.get("user");
                map.put("hasFollowed", followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, user1.getId()));
            }
        }
        model.addAttribute("followees", followees);
        return "site/followee";
    }

    @GetMapping("/followers/{userId}")
    @LoginRequired
    public String getFollowers(@PathVariable Integer userId, Page page, Model model) {
        User user = userService.getUserById(userId);
        if (user == null) throw new RuntimeException("该用户不存在!");
        model.addAttribute("user", user);
        page.setPageSize(5);
        page.setPath("/followers/" + userId);
        page.setRows(followService.findFollowerCount(ENTITY_TYPE_USER, userId).intValue());
        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getPageSize());
        if (followers != null) {
            for (Map<String, Object> map : followers) {
                User user1 = (User) map.get("user");
                map.put("hasFollowed", followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, user1.getId()));
            }
        }
        model.addAttribute("followers", followers);
        return "site/follower";
    }
}
