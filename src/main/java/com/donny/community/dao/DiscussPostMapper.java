package com.donny.community.dao;

import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectPostList(Integer userId, Integer offset, Integer limit, Integer orderMode);

    /**
     * @param注解用于给参数取别名，如果只有一个参数且在if标签中中使用必须添加别名
     */
    Integer selectPostRows(@Param("userId") Integer userId);

    Integer insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(Integer id);

    Integer updateCommentCount(Integer id, int commentCount);

    Comment selectCommentById(Integer id);

    Integer updateType(Integer id, Integer type);

    Integer updateStatus(Integer id, Integer status);

    Integer updateScore(Integer id, double score);
}
