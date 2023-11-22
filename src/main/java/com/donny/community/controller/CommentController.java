package com.donny.community.controller;


import com.donny.community.entity.Comment;
import com.donny.community.service.CommentService;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable Integer discussPostId, Comment comment) {
        Integer userId = hostHolder.getUser().getId();
        if (userId == null) throw new RuntimeException("未登录用户无法发表评论!");
        comment.setUserId(userId);
        comment.setStatus(1);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;
    }


}
