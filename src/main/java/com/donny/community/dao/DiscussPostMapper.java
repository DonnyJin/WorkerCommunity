package com.donny.community.dao;

import com.donny.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectPostList(Integer userId, Integer offset, Integer limit);

    Integer selectPostRows(@Param("userId") Integer userId);

    Integer insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(Integer id);

    Integer updateCommentCount(Integer id, int commentCount);

}
