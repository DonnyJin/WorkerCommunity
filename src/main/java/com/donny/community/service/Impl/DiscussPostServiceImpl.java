package com.donny.community.service.Impl;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.DiscussPost;
import com.donny.community.service.DiscussPostService;
import com.donny.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

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

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    @Override
    public DiscussPost findDiscussPost(Integer id) {
        return discussPostMapper.selectDiscussPostById(id);
    }


}
