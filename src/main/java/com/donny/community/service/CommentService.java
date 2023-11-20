package com.donny.community.service;

import com.donny.community.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    Integer getCommentCount(Integer entityType, Integer entityId);
}
