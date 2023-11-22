package com.donny.community.service;

import com.donny.community.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService {

    List<DiscussPost> getPosts(Integer userId, Integer offset, Integer limit);

    Integer getRowsCount(Integer userId);

    Integer addDiscussPost(DiscussPost discussPost);

    DiscussPost findDiscussPost(Integer userId);

    Integer updateCommentCount(Integer id, Integer commentCount);
}
