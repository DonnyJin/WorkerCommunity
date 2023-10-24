package com.donny.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPostMapper> selectPostList(Integer userId);

    Integer selectPostRows(@Param("userId") Integer userId);
}
