package com.donny.community.service.Impl;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Event;
import com.donny.community.service.DiscussPostService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService, CommunityConstant {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<DiscussPost> getPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectPostList(userId, offset, limit);
    }

    @Override
    public Integer getRowsCount(Integer userId) {
        return discussPostMapper.selectPostRows(userId);
    }

    @Override
    public Integer addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) throw new IllegalArgumentException("参数不能为空!");
        // 转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        discussPost.setStatus(1);
        discussPost.setType(0);
        discussPost.setCommentCount(0);

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    @Override
    public DiscussPost findDiscussPost(Integer id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    @Override
    public Integer updateCommentCount(Integer id, Integer commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    @Override
    public Comment findCommentById(Integer id) {
        return discussPostMapper.selectCommentById(id);
    }

    @Override
    public Integer updateTypeById(Integer id, Integer type) {
        return discussPostMapper.updateType(id, type);
    }

    @Override
    public Integer updateStatusById(Integer id, Integer status) {
        return discussPostMapper.updateStatus(id, status);
    }


}
