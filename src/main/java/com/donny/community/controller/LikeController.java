package com.donny.community.controller;

import com.donny.community.annotation.LoginRequired;
import com.donny.community.entity.User;
import com.donny.community.service.LikeService;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    @LoginRequired
    public String like(Integer entityType, Integer entityId, Integer entityUserId) {
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        Integer likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return CommunityUtil.getJSONString(0, null, map);
    }
}
