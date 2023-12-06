package com.donny.community.controller;


import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Event;
import com.donny.community.event.EventProducer;
import com.donny.community.service.CommentService;
import com.donny.community.service.DiscussPostService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.HostHolder;
import com.donny.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer producer;

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable Integer discussPostId, Comment comment) {
        Integer userId = hostHolder.getUser().getId();
        if (userId == null) throw new RuntimeException("未登录用户无法发表评论!");
        comment.setUserId(userId);
        comment.setStatus(1);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件(Kafka)
        Event event = new Event();
        event.setTopic(TOPIC_COMMENT);
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(comment.getEntityType());
        event.setEntityId(comment.getEntityId());
        event.setData("postId", discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPost(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = discussPostService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        producer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            Event event1 = new Event();
            event1.setTopic(TOPIC_PUBLISH);
            event1.setUserId(userId);
            event1.setEntityType(ENTITY_TYPE_POST);
            event1.setEntityId(discussPostId);

            producer.fireEvent(event1);

            // 计算帖子分数
            String key = RedisUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(key, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }


}
