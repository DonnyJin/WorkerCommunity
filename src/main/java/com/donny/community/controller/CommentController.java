package com.donny.community.controller;


import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Event;
import com.donny.community.event.EventProducer;
import com.donny.community.service.CommentService;
import com.donny.community.service.DiscussPostService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable Integer discussPostId, Comment comment) {
        Integer userId = hostHolder.getUser().getId();
        if (userId == null) throw new RuntimeException("未登录用户无法发表评论!");
        comment.setUserId(userId);
        comment.setStatus(1);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
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

        return "redirect:/discuss/detail/" + discussPostId;
    }


}
