package com.donny.community.service.Impl;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.DiscussPost;
import com.donny.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    DiscussPostMapper discussPostMapper;


    @Override
    public List<DiscussPost> getPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectPostList(userId, offset, limit);
    }

    @Override
    public Integer getRowsCount(Integer userId) {
        return discussPostMapper.selectPostRows(userId);
    }
}
