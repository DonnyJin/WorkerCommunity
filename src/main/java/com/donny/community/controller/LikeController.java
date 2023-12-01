package com.donny.community.controller;

import com.donny.community.annotation.LoginRequired;
import com.donny.community.entity.Event;
import com.donny.community.entity.User;
import com.donny.community.event.EventProducer;
import com.donny.community.service.LikeService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import com.sun.javafx.scene.EventHandlerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer producer;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    @LoginRequired
    public String like(Integer entityType, Integer entityId, Integer entityUserId, Integer postId) {
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        Integer likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event();
            event.setTopic(TOPIC_LIKE);
            event.setUserId(hostHolder.getUser().getId());
            event.setEntityId(entityId);
            event.setEntityType(entityType);
            event.setEntityUserId(entityUserId);
            event.setData("postId", postId);

            producer.fireEvent(event);
        }

        return CommunityUtil.getJSONString(0, null, map);
    }
}
