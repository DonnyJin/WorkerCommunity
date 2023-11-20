package com.donny.community.controller;

import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Page;
import com.donny.community.entity.User;
import com.donny.community.service.CommentService;
import com.donny.community.service.DiscussPostService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    CommentService commentService;

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
    public String getDiscussPost(@PathVariable Integer id, Model model, Page page) {
        DiscussPost post = discussPostService.findDiscussPost(id);
        model.addAttribute("post", post);
        // 作者
        User user = userService.getUserById(post.getUserId());
        model.addAttribute("user", user);

        //评论: 给帖子的评论
        //回复: 给评论的评论
        page.setPageSize(5);
        page.setPath("/discuss/detail" + id);
        page.setRows(post.getCommentCount());
        List<Comment> comments = commentService.getCommentByEntity(ENTITY_TYPE_POST, post.getId(),
                page.getOffset(), page.getPageSize());
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                Map<String, Object> commentVO = new HashMap<>();
                commentVO.put("comment", comment);
                commentVO.put("user", userService.getUserById(comment.getUserId()));
                //查询回复, 不分页
                List<Comment> replies = commentService.getCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVOList = new ArrayList<>();
                if (replies != null) {
                    for (Comment reply : replies) {
                        Map<String, Object> replyVO = new HashMap<>();
                        replyVO.put("reply", reply);
                        replyVO.put("user", userService.getUserById(reply.getUserId()));
                        // 回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.getUserById(reply.getTargetId());
                        replyVO.put("target",target);

                        replyVOList.add(replyVO);
                    }
                }
                commentVO.put("replies", replyVOList);

                // 回复数量
                Integer replyCount = commentService.getCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("replyCount", replyCount);

                commentVOList.add(commentVO);
            }
        }
        model.addAttribute("comments", commentVOList);
        return "site/discuss-detail";
    }

}
